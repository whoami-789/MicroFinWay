package com.MicroFinWay.service;

import com.MicroFinWay.dto.PaymentInfoDTO;
import com.MicroFinWay.dto.request.PaymentRequestDTO;
import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.PaymentSchedule;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CreditRepository creditRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final AccountingRepository accountingRepository;
    private final AccountingService accountingService;
    private final OrganizationService organizationService;

    /**
     * Получение информации по кредиту для формы
     */
    public PaymentInfoDTO getCreditInfo(String contractNumber) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Кредит не найден: " + contractNumber));

        // 🔹 Основной долг — сумма непогашенных платежей
        BigDecimal mainDebt = paymentScheduleRepository.findByCreditIdAndPaymentStatus(credit.getId(), 0).stream()
                .map(PaymentSchedule::getPrincipalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🔹 Проценты — начисленные, но не погашенные
        BigDecimal interest = paymentScheduleRepository.findByCreditIdAndPaymentStatus(credit.getId(), 0).stream()
                .map(PaymentSchedule::getInterestPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🔹 Пени и штрафы — из проводок
        List<Accounting> penaltyEntries = accountingRepository.findByContractNumberAndDebitAccountContaining(contractNumber, "16405");
        List<Accounting> fineEntries = accountingRepository.findByContractNumberAndDebitAccountContaining(contractNumber, "45994");

        BigDecimal penalty = penaltyEntries.stream()
                .map(Accounting::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal fine = fineEntries.stream()
                .map(Accounting::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PaymentInfoDTO(
                credit.getUser().getFullName(),
                "10101000904619251001", // 🔸 касса организации (в будущем можно подтянуть из таблицы счетов)
                mainDebt,
                interest,
                penalty,
                fine,
                BigDecimal.ZERO // other
        );
    }

    /**
     * Проведение платежа
     */
    public void processPayment(String contractNumber, PaymentRequestDTO dto) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Кредит не найден: " + contractNumber));

        LocalDate opDay = organizationService.getCurrentOperationalDay();

        // 🔹 1. Погашение основного долга
        if (dto.getMainDebt() != null && dto.getMainDebt().compareTo(BigDecimal.ZERO) > 0) {
            accountingService.creditPayedMainLoan(contractNumber, dto.getMainDebt());
        }

        // 🔹 2. Погашение процентов
        if (dto.getInterest() != null && dto.getInterest().compareTo(BigDecimal.ZERO) > 0) {
            accountingService.creditPayedInterestMain(contractNumber, dto.getInterest());
        }

        // 🔹 3. Погашение пени
        if (dto.getPenalty() != null && dto.getPenalty().compareTo(BigDecimal.ZERO) > 0) {
            accountingService.payOverdueInterest(contractNumber, dto.getPenalty());
        }

        // 🔹 4. Погашение штрафов
        if (dto.getFine() != null && dto.getFine().compareTo(BigDecimal.ZERO) > 0) {
            accountingService.createEntry(
                    contractNumber,
                    dto.getFine(),
                    c -> "10101000904619251001",
                    c -> "45994",
                    "",
                    "Погашение штрафа по договору " + contractNumber
            );
        }

        // 🔹 5. Прочие списания
        if (dto.getOther() != null && dto.getOther().compareTo(BigDecimal.ZERO) > 0) {
            accountingService.createEntry(
                    contractNumber,
                    dto.getOther(),
                    c -> "10101000904619251001",
                    c -> "45201",
                    "",
                    "Прочий платёж по договору " + contractNumber
            );
        }
    }
}