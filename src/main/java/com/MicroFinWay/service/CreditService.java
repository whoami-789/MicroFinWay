package com.MicroFinWay.service;

import com.MicroFinWay.dto.*;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.CreditAccount;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.*;
import com.MicroFinWay.search.CreditIndex;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * The CreditService class is responsible for managing credit creation and processing in the system.
 * It utilizes various repository and utility services to create credit accounts, generate account numbers,
 * and map domain objects to data transfer objects (DTOs).
 *
 * Dependencies:
 * - {@link CreditRepository}: Repository interface for handling credit operations in the database.
 * - {@link UserRepository}: Repository interface for user-related database operations.
 * - {@link AccountNumberGenerator}: Utility service for generating unique account numbers.
 * - {@link AccountTypeRepository}: Repository interface for handling account type data.
 *
 * Methods:
 * - createCredit(CreditDTO): Creates a new credit entity in the system based on the provided DTO, generates
 *   associated accounts and values, and persists the information to the database.
 */
@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditAccountRepository creditAccountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountingService accountingService;
    private final CreditSearchRepository creditSearchRepository; // 👈 добавили сюда

    private final CollateralRepository collateralRepository;

    @PostConstruct
    public void initIndex() {
        reindexAllCredits();
    }

    public CreditDTO createCredit(CreditDTO creditDTO) {
        User user = userRepository.findByKod(creditDTO.getCode())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + creditDTO.getCode()));

        long creditCount = creditRepository.countByUserKod(user.getKod());
        String sequenceNumber = String.format("%03d", creditCount + 1);
        String contractNumber = user.getKod() + "-" + (creditCount + 1);

        // Создаем кредит
        Credit credit = new Credit();
        BeanUtils.copyProperties(creditDTO, credit, "contractNumber");
        credit.setContractNumber(contractNumber);
        credit.setUser(user);
        credit.setCode(user.getKod());
        credit.setCurrencyCode("000");
        credit.setStatus(Credit.CreditStatus.ACTIVE);
        credit.setContractDate(LocalDate.now());
        credit.setLastUpdatedTime(LocalDateTime.now());
        credit.setPaidAmount(BigDecimal.ZERO);

        credit = creditRepository.save(credit); // Сначала сохраняем кредит

        // Генерация счетов
        CreditAccount account = CreditAccount.builder()
                .credit(credit)
                .account12401(accountNumberGenerator.generateAccountNumber("12401", credit.getCode(), sequenceNumber))
                .account12405(accountNumberGenerator.generateAccountNumber("12405", credit.getCode(), sequenceNumber))
                .account12409(accountNumberGenerator.generateAccountNumber("12409", credit.getCode(), sequenceNumber))
                .account12501(accountNumberGenerator.generateAccountNumber("12501", credit.getCode(), sequenceNumber))
                .account14801(accountNumberGenerator.generateAccountNumber("14801", credit.getCode(), sequenceNumber))
                .account14899(accountNumberGenerator.generateAccountNumber("14899", credit.getCode(), sequenceNumber))
                .account15701(accountNumberGenerator.generateAccountNumber("15701", credit.getCode(), sequenceNumber))
                .account15799(accountNumberGenerator.generateAccountNumber("15799", credit.getCode(), sequenceNumber))
                .account16307(accountNumberGenerator.generateAccountNumber("16307", credit.getCode(), sequenceNumber))
                .account16377(accountNumberGenerator.generateAccountNumber("16377", credit.getCode(), sequenceNumber))
                .account16405(accountNumberGenerator.generateAccountNumber("16405", credit.getCode(), sequenceNumber))
                .account22812(accountNumberGenerator.generateAccountNumber("22812", credit.getCode(), sequenceNumber))
                .account94502(accountNumberGenerator.generateAccountNumber("94501", credit.getCode(), sequenceNumber))
                .account94503(accountNumberGenerator.generateAccountNumber("94503", credit.getCode(), sequenceNumber))
                .account91501(accountNumberGenerator.generateAccountNumber("91501", credit.getCode(), sequenceNumber))
                .account12499(accountNumberGenerator.generateAccountNumber("12499", credit.getCode(), sequenceNumber))
                .account16309(accountNumberGenerator.generateAccountNumber("16309", credit.getCode(), sequenceNumber))
                .account95413(accountNumberGenerator.generateAccountNumber("95413", credit.getCode(), sequenceNumber))
                .contractNumber(contractNumber)
                .build();

        credit.setCreditAccount(account); // <--- вот это нужно
        creditAccountRepository.save(account); // Сохраняем счета

        accountingService.givenCreditMainLoan(contractNumber, creditDTO.getAmount());

        // DTO обратно
        return toCreditDTO(credit);
    }

    private CreditDTO toCreditDTO(Credit credit) {
        CreditDTO dto = new CreditDTO();
        dto.setId(credit.getId());
        dto.setContractNumber(credit.getContractNumber());
        dto.setAmount(credit.getAmount());
        dto.setLoanTerm(credit.getLoanTerm());
        dto.setInterestRate(credit.getInterestRate());
        dto.setStatus(credit.getStatus());
        return dto;
    }

    public CreditDetailsDTO getDetails(String contractNumber) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new RuntimeException("Кредит не найден"));

        return toCreditDetailsDTO(credit);
    }

    /**
     * Возвращает детали по всем кредитам, хранящимся в базе.
     */
    public List<CreditDetailsDTO> getAllCredits() {
        return creditRepository.findAll()
                .stream()
                .map(this::toCreditDetailsDTO)
                .toList();
    }

    /**
     * Превращает сущность Credit в CreditDetailsDTO без использования маппера.
     */
    private CreditDetailsDTO toCreditDetailsDTO(Credit credit) {
        // --- Credit -> CreditDTO -----------------------------------------
        CreditDTO creditDto = toCreditDTO(credit);

        // --- CreditAccount -> CreditAccountDTO ---------------------------
        CreditAccountDTO accountDto = null;
        if (credit.getCreditAccount() != null) {
            accountDto = new CreditAccountDTO();
            BeanUtils.copyProperties(credit.getCreditAccount(), accountDto);
        }

        // --- PaymentSchedule -> List<PaymentScheduleDTO> -----------------
        List<PaymentScheduleDTO> scheduleDtos = credit.getPaymentSchedules()
                .stream()
                .map(ps -> {
                    PaymentScheduleDTO dto = new PaymentScheduleDTO();
                    BeanUtils.copyProperties(ps, dto);
                    return dto;
                })
                .toList();

        // --- Collateral -> List<CollateralDTO> ---------------------------
        List<CollateralDTO> collateralDtos = credit.getCollaterals()
                .stream()
                .map(col -> {
                    CollateralDTO dto = new CollateralDTO();
                    BeanUtils.copyProperties(col, dto);
                    return dto;
                })
                .toList();

        return new CreditDetailsDTO(
                creditDto,
                scheduleDtos,
                accountDto,
                collateralDtos
        );
    }

    // 🔄 Индексация всех кредитов
    public void reindexAllCredits() {
        List<Credit> credits = creditRepository.findAll();
        List<CreditIndex> indexList = credits.stream()
                .map(c -> new CreditIndex(c.getContractNumber(), c.getUser().getKod()))
                .toList();
        creditSearchRepository.saveAll(indexList);
    }

    public Map<LocalDate, Long> getCreditsCountByDate(LocalDate start, LocalDate end) {
        List<Object[]> results = creditRepository.countCreditsByDateRange(start, end);

        return results.stream()
                .collect(Collectors.toMap(
                        r -> (LocalDate) r[0],
                        r -> (Long) r[1]
                ));
    }
}