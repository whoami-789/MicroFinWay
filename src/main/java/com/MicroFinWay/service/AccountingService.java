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
        entry.setOperationDate(LocalDate.now());
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
     * Погашение основного тела кредита
     */
    public void creditPayedCardMainLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "10101000904619251001",                     // дебет: касса
                credit -> credit.getCreditAccount().getAccount12405(), // кредит: основной кредит
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
     * Переброска остатка на кредитный счёт из просроченного
     */
    public void moveOverdueToMainLoan(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12405(), // дебет: просроченный
                credit -> credit.getCreditAccount().getAccount12401(), // кредит: основной
                "",
                "Переброска остатка с просроченного на основной счёт " + contractNumber
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
     * Переброска процентов в основной
     */
    public void moveInterestToPrincipal(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> credit.getCreditAccount().getAccount12401(),
                "",
                "Переброска процентов в основной долг " + contractNumber
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
     * Начисление просроченных процентов
     *
     * @param contractNumber
     * @param amount
     */
    public void accrueOverdueInterest(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> "42005000904619251001", // счёт начисления просрочки
                credit -> credit.getCreditAccount().getAccount16377(),
                "",
                "Начисление просроченных процентов по договору " + contractNumber
        );
    }

    /**
     * Погашение процентов из аванса (%% → 22812)
     *
     * @param contractNumber
     * @param amount
     */
    public void repayInterestFromAdvance(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount22812(), // авансовый
                credit -> credit.getCreditAccount().getAccount16307(), // проценты
                "",
                "Погашение процентов из аванса по договору " + contractNumber
        );
    }

    /**
     * Погашение просроченных процентов из аванса
     *
     * @param contractNumber
     * @param amount
     */
    public void repayOverdueFromAdvance(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount22812(),
                credit -> credit.getCreditAccount().getAccount16377(),
                "",
                "Погашение просроченных процентов из аванса по договору " + contractNumber
        );
    }

    /**
     * 🔹 Переброска на договор (12409 → 12401)
     *
     * @param contractNumber
     * @param amount
     */
    public void moveBalance12409to12401(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12409(),
                credit -> credit.getCreditAccount().getAccount12401(),
                "",
                "Переброска остатков с 12409 на основной счёт " + contractNumber
        );
    }

    /**
     * 🔹 Переброска на 15701
     *
     * @param contractNumber
     * @param amount
     */
    public void moveBalanceTo15701(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(),
                credit -> credit.getCreditAccount().getAccount15701(),
                "",
                "Переброска остатков с 12401 на 15701 по договору " + contractNumber
        );
    }

    /**
     * 🔹 Переброска в 15799
     *
     * @param contractNumber
     * @param amount
     */
    public void moveTo15799(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(),
                credit -> credit.getCreditAccount().getAccount15799(),
                "",
                "Переброска остатков с 12401 на 15799 по договору " + contractNumber
        );
    }

    /**
     * 🔹 Погашение процентов по договору через 10509000204619251002
     *
     * @param contractNumber
     * @param amount
     */
    public void payInterestVia10509(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> "10509000204619251002",
                "",
                "Погашение процентов по договору через расчётный счёт " + contractNumber
        );
    }

    /**
     * 🔹 Погашение просроченных процентов через 10503
     *
     * @param contractNumber
     * @param amount
     */
    public void payOverdueInterestVia10503(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16377(),
                credit -> "10503000904619251001",
                "",
                "Погашение просроченных процентов через 10503 по договору " + contractNumber
        );
    }

    /**
     * 🔹 Перенос на резерв (16307 → account_94502)
     *
     * @param contractNumber
     * @param amount
     */
    public void reserveTransfer94502(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> credit.getCreditAccount().getAccount94502(),
                "",
                "Перенос процентов в резерв 94502 по договору " + contractNumber
        );
    }

    /**
     * 🔹 Перенос на резерв (16307 → account_94503)
     *
     * @param contractNumber
     * @param amount
     */
    public void reserveTransfer94503(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> credit.getCreditAccount().getAccount94503(),
                "",
                "Перенос процентов в резерв 94503 по договору " + contractNumber
        );
    }

    /**
     * 🔹 Возврат процентов из аванса (16307 ← 22812)
     *
     * @param contractNumber
     * @param amount
     */
    public void returnInterestFromAdvance(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount22812(),
                credit -> credit.getCreditAccount().getAccount16307(),
                "",
                "Возврат процентов из аванса по договору " + contractNumber
        );
    }

    /**
     * 🔹 Погашение тела через 10503
     *
     * @param contractNumber
     * @param amount
     */
    public void repayPrincipalVia10503(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12401(),
                credit -> "10503000904619251001",
                "",
                "Погашение тела через счёт 10503 по договору " + contractNumber
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
     * Перенос в просроченный основной долг
     *
     * @param contractNumber
     * @param amount
     */
    public void movePrincipalToOverdue(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount12405(),
                credit -> credit.getCreditAccount().getAccount12401(),
                "",
                "Перенос основного долго в просрочку по договору " + contractNumber
        );
    }

    /**
     * Возврат излишне начисленных процентов (16307 → 22812)
     *
     * @param contractNumber
     * @param amount
     */
    public void refundInterestToAdvance(String contractNumber, BigDecimal amount) {
        createEntry(
                contractNumber,
                amount,
                credit -> credit.getCreditAccount().getAccount16307(),
                credit -> credit.getCreditAccount().getAccount22812(),
                "",
                "Возврат излишне начисленных процентов по договору " + contractNumber
        );
    }

}
