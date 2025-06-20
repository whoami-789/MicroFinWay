package com.MicroFinWay.service;

import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

/**
 * Service class for handling accounting transactions specifically related to credits.
 * This service provides methods for managing various accounting actions such as credit issuance,
 * repayment, interest calculations, and account transfers.
 */
@Service
@RequiredArgsConstructor
public class AccountingService {

    private final CreditRepository creditRepository;
    private final AccountingRepository entryRepository;
    private final OrganizationService organizationService; // 🔹 добавлено

    /**
     * Универсальный метод создания бухгалтерской проводки.
     */
    public void createEntry(
            String contractNumber,
            BigDecimal amount,
            Function<Credit, String> debitResolver,
            Function<Credit, String> creditResolver,
            String transactionType,
            String description
    ) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit not found: " + contractNumber));

        Accounting entry = new Accounting();
        entry.setContractNumber(contractNumber);
        entry.setDebitAccount(debitResolver.apply(credit));
        entry.setCreditAccount(creditResolver.apply(credit));
        entry.setAmount(amount);

        // 🔹 Используем дату операционного дня, а не текущую дату сервера
        LocalDate operationalDate = organizationService.getCurrentOperationalDay();
        entry.setOperationDate(operationalDate);

        entry.setTransactionType(transactionType);
        entry.setStatus(0);
        entry.setDescription(description);

        entryRepository.save(entry);
    }

    /**
     * Выдача основного тела кредита
     */
    public void givenCreditMainLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(), // дебет: основной кредит
                credit -> "10101000904619251001",                     // кредит: касса/банк
                "",
                "Выдача кредита по договору " + contractNumber
        );
    }

    /**
     * Выдача на карту
     */
    public void givenCreditCardLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(),
                credit -> "10503000904619251001",
                "",
                "Выдача кредита по договору на карту " + contractNumber
        );
    }

    /**
     * Погашение основного тела кредита
     */
    public void creditPayedMainLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "10101000904619251001",                     // дебет: касса
                credit -> credit.getCreditAccount().getAccount12401(), // кредит: основной кредит
                "",
                "Погашение основного долга по договору " + contractNumber
        );
    }

    /**
     * Погашение картой основного тела кредита
     */
    public void creditPayedCardMainLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "10509000204619251002",                     // дебет: касса
                credit -> credit.getCreditAccount().getAccount12401(), // кредит: основной кредит
                "",
                "Погашение картой основного долга по договору " + contractNumber
        );
    }

    /**
     * Погашение процентов по кредиту
     */
    public void creditPayedInterestMain(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "10101000904619251001",                        // дебет: касса
                credit -> credit.getCreditAccount().getAccount16307(),  // кредит: счёт процентов
                "",
                "Погашение процентов по договору " + contractNumber
        );
    }

    /**
     * Погашение просроченных процентов
     */
    public void payOverdueInterest(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "10101000904619251001", // касса
                credit -> credit.getCreditAccount().getAccount16377(), // просроченные %
                "",
                "Погашение просроченных процентов по договору " + contractNumber
        );
    }

    /**
     * Начисление процентов
     *
     * @param contractNumber
     * @param amount
     */
    public void accrueInterest(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "42001000604619251004", // счёт начисления %
                credit -> credit.getCreditAccount().getAccount16307(),
                "",
                "Начисление процентов по договору " + contractNumber
        );
    }

    /**
     * Начисление процентов безналично выданного кредита
     *
     * @param contractNumber
     * @param amount
     */
    public void accrueInterestByCreditInTransitAccount(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "42001000604619251004", // счёт начисления %
                credit -> credit.getCreditAccount().getAccount16309(),
                "",
                "Начисление процентов по договору " + contractNumber
        );
    }

    /**
     * Начисление просроченных процентов
     *
     * @param contractNumber
     * @param amount
     */
    public void accrueInterestOverdue(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "42005000604619251004", // счёт начисления %
                credit -> credit.getCreditAccount().getAccount16307(),
                "",
                "Начисление просроченных процентов по договору " + contractNumber
        );
    }

    /**
     * Начисление просроченных процентов безналично выданного кредита
     *
     * @param contractNumber
     * @param amount
     */
    public void accrueByCreditInTransitAccountOverdue(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "42005000604619251004", // счёт начисления %
                credit -> credit.getCreditAccount().getAccount16307(),
                "",
                "Начисление просроченных процентов по договору " + contractNumber
        );
    }

    /**
     * Снятие с залога
     *
     * @param contractNumber
     * @param amount
     */
    public void releaseCollateral(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount94502(), // пример, можно 94501
                credit -> "96381000604619251005",
                "",
                "Снятие с залога по договору " + contractNumber
        );
    }

    /**
     * Учет залога
     *
     * @param contractNumber
     * @param amount
     */
    public void registerCollateral(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "96381000604619251005",
                credit -> credit.getCreditAccount().getAccount94502(),
                "",
                "Учёт залога по договору " + contractNumber
        );
    }

    /**
     * Переброска из основного в просроченный
     *
     * @param contractNumber
     * @param amount
     */
    public void moveMainToOverdue(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(),
                credit -> credit.getCreditAccount().getAccount12405(),
                "",
                "Переброска с основного счёта в просроченный " + contractNumber
        );
    }

    /**
     * Перенос в просроченные проценты
     *
     * @param contractNumber
     * @param amount
     */
    public void moveInterestToOverdue(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> credit.getCreditAccount().getAccount16377(),
                "",
                "Перенос процентов в просрочку по договору " + contractNumber
        );
    }

    /**
     * Перенос в просроченные проценты при кредите не наличкой
     *
     * @param contractNumber
     * @param amount
     */
    public void moveInterestToOverdueByCreditInTransitAccount(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16309(),
                credit -> credit.getCreditAccount().getAccount16377(),
                "",
                "Перенос процентов в просрочку по договору " + contractNumber
        );
    }



}
