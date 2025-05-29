package com.MicroFinWay.service;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


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
    private final UserRepository userRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountTypeRepository accountTypeRepository;

    public CreditDTO createCredit(CreditDTO creditDTO) {
        // Найдём клиента
        User user = userRepository.findByKod(creditDTO.getCode())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + creditDTO.getCode()));

        String clientCode = user.getKod();
        if (clientCode == null || clientCode.isEmpty()) {
            throw new IllegalArgumentException("User kod (client code) is not set");
        }

        long creditCount = creditRepository.countByUserId(user.getId());
        String sequenceNumber = String.format("%02d", creditCount + 1);
        String contractNumber = clientCode + "-" + (creditCount + 1);

        Credit credit = new Credit();
        credit.setContractNumber(contractNumber);
        credit.setAmount(creditDTO.getAmount());
        credit.setCurrencyCode(creditDTO.getCurrencyCode());
        credit.setLoanTerm(creditDTO.getLoanTerm());
        credit.setInterestRate(creditDTO.getInterestRate());
        credit.setUser(user);
        credit.setPaidAmount(BigDecimal.ZERO);
        credit.setStatus(Credit.CreditStatus.ACTIVE);
        credit.setLastUpdatedTime(java.time.LocalDateTime.now());

        // Генерация счетов
        credit.setAccountLoanMain(accountNumberGenerator.generateAccountNumber("CREDIT_BODY", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountInterestGeneral(accountNumberGenerator.generateAccountNumber("INTEREST", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountOverdueLoan(accountNumberGenerator.generateAccountNumber("OVERDUE", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountOverdueInterest(accountNumberGenerator.generateAccountNumber("OVERDUE_INTEREST", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountCollateral(accountNumberGenerator.generateAccountNumber("COLLATERAL", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountReserve(accountNumberGenerator.generateAccountNumber("RESERVE", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountCreditWriteoff(accountNumberGenerator.generateAccountNumber("WRITE_OFF", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountPenaltyAdditional(accountNumberGenerator.generateAccountNumber("PENALTY", credit.getCurrencyCode(), clientCode, sequenceNumber));
        credit.setAccountIncome(accountNumberGenerator.generateAccountNumber("INCOME", credit.getCurrencyCode(), clientCode, sequenceNumber));

        Credit savedCredit = creditRepository.save(credit);

        return toCreditDTO(savedCredit);
    }

    private CreditDTO toCreditDTO(Credit credit) {
        CreditDTO dto = new CreditDTO();
        dto.setId(credit.getId());
        dto.setContractNumber(credit.getContractNumber());
        dto.setAmount(credit.getAmount());
        dto.setCurrencyCode(credit.getCurrencyCode());
        dto.setLoanTerm(credit.getLoanTerm());
        dto.setInterestRate(credit.getInterestRate());
        dto.setStatus(credit.getStatus());
        dto.setAccountLoanMain(credit.getAccountLoanMain());
        // И так далее для всех счетов
        return dto;
    }


}
