package com.MicroFinWay.service;

import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.CreditAccount;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ROrderService {

    private final AccountingRepository accountingRepository;
    private final CreditAccountRepository creditAccountRepository;

    /**
     * Создание расходного ордера (RO)
     * Дт — счёт кредита 12401
     * Кт — касса 10101
     */
    public Accounting createROrder(String contractNumber, BigDecimal amount, LocalDate contractDate) {

        // 🔹 Ищем счета по договору
        CreditAccount creditAccount = creditAccountRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Не найдены счета для договора " + contractNumber));

        // 🔹 Берём конкретный счёт кредита (12401)
        String debitAccount = creditAccount.getAccount12401();
        String creditAccountStatic = "10101"; // касса

        // 🔹 Формируем описание
        String description = String.format(
                "Выдача микрозайма по договору №%s от %s",
                contractNumber,
                contractDate
        );

        // 🔹 Создаём запись в Accounting
        Accounting accounting = new Accounting();
        accounting.setDebitAccount(debitAccount);
        accounting.setCreditAccount(creditAccountStatic);
        accounting.setAmount(amount);
        accounting.setOperationDate(LocalDate.now());
        accounting.setContractNumber(contractNumber);
        accounting.setDescription(description);
        accounting.setTransactionType("Расходный ордер");
        accounting.setStatus(0); // черновик

        return accountingRepository.save(accounting);
    }
}