package com.MicroFinWay.service;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.CreditBalance;
import com.MicroFinWay.model.CreditBalanceHistory;
import com.MicroFinWay.repository.CreditBalanceHistoryRepository;
import com.MicroFinWay.repository.CreditBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с сальдо кредитов.
 * Отвечает за обновление текущих остатков и создание ежедневных снимков (истории).
 */
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final CreditBalanceRepository balanceRepo;
    private final CreditBalanceHistoryRepository historyRepo;

    /**
     * Обновление текущего баланса по счёту кредита.
     * @param credit    кредит, к которому относится операция
     * @param accountCode код бухгалтерского счёта
     * @param delta     сумма изменения
     * @param isDebit   направление операции (true — дебет, false — кредит)
     */
    public void updateBalance(Credit credit, String accountCode, BigDecimal delta, boolean isDebit) {
        if (credit == null || accountCode == null || delta == null) {
            return;
        }

        // ищем существующий баланс или создаём новый
        CreditBalance balance = balanceRepo.findByCredit_IdAndAccountCode(credit.getId(), accountCode)
                .orElseGet(() -> CreditBalance.builder()
                        .credit(credit)
                        .contractNumber(credit.getContractNumber())
                        .accountCode(accountCode)
                        .balance(BigDecimal.ZERO)
                        .updatedAt(LocalDateTime.now())
                        .build()
                );

        // считаем новое сальдо
        BigDecimal newBalance = isDebit
                ? balance.getBalance().add(delta)
                : balance.getBalance().subtract(delta);

        // защита от отрицательных значений, если это активный счёт
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }

        balance.setBalance(newBalance);
        balance.setUpdatedAt(LocalDateTime.now());
        balanceRepo.save(balance);
    }

    /**
     * Сохранение снимка всех текущих сальдо при закрытии операционного дня.
     * @param date дата операционного дня
     */
    public void snapshotDailyBalances(LocalDate date) {
        List<CreditBalance> allBalances = balanceRepo.findAll();

        for (CreditBalance cb : allBalances) {
            CreditBalanceHistory history = CreditBalanceHistory.builder()
                    .credit(cb.getCredit())
                    .contractNumber(cb.getContractNumber())
                    .accountCode(cb.getAccountCode())
                    .date(date)
                    .openingBalance(cb.getBalance()) // можно добавить расчёт при необходимости
                    .closingBalance(cb.getBalance())
                    .debitTurnover(BigDecimal.ZERO)  // TODO: добавить расчёт при наличии данных
                    .creditTurnover(BigDecimal.ZERO)
                    .createdAt(LocalDateTime.now())
                    .build();

            historyRepo.save(history);
        }
    }

    /**
     * Получить все активные сальдо по номеру договора.
     */
    public List<CreditBalance> getBalancesByContract(String contractNumber) {
        return balanceRepo.findByContractNumber(contractNumber);
    }

    /**
     * Получить историю остатков по договору.
     */
    public List<CreditBalanceHistory> getHistoryByContract(String contractNumber) {
        return historyRepo.findByContractNumberOrderByDateDesc(contractNumber);
    }

    /**
     * Полное обновление балансов по всем кредитам (на случай пересчёта или восстановления).
     * Можно вызывать при запуске системы или после крупных изменений.
     */
    public void rebuildAllBalances(List<Credit> allCredits) {
        for (Credit credit : allCredits) {
            // Здесь можно пройтись по всем счетам из CreditAccount и пересчитать баланс из истории проводок
            // Примерно:
            // BigDecimal sum = accountingRepo.sumByCreditAndAccount(credit.getId(), accountCode);
            // updateBalance(credit, accountCode, sum, true);
        }
    }
}