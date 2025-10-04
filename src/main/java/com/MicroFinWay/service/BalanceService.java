package com.MicroFinWay.service;

import com.MicroFinWay.model.*;
import com.MicroFinWay.repository.CreditBalanceHistoryRepository;
import com.MicroFinWay.repository.CreditBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final CreditBalanceRepository balanceRepo;
    private final CreditBalanceHistoryRepository historyRepo;

    /**
     * Обновление текущего сальдо по кредиту и счёту
     */
    public void updateBalance(Credit credit, String accountCode, BigDecimal delta, boolean isDebit) {
        CreditBalance balance = balanceRepo.findByCredit_IdAndAccountCode(credit.getId(), accountCode)
                .orElseGet(() -> CreditBalance.builder()
                        .credit(credit)
                        .contractNumber(credit.getContractNumber())
                        .accountCode(accountCode)
                        .balance(BigDecimal.ZERO)
                        .build()
                );

        BigDecimal newBalance = isDebit
                ? balance.getBalance().add(delta)   // приход
                : balance.getBalance().subtract(delta); // расход

        balance.setBalance(newBalance);
        balance.setUpdatedAt(LocalDateTime.now());
        balanceRepo.save(balance);
    }

    /**
     * Сохранение остатков в историю при закрытии операционного дня
     */
    public void snapshotDailyBalances(LocalDate date) {
        List<CreditBalance> all = balanceRepo.findAll();
        for (CreditBalance cb : all) {
            CreditBalanceHistory history = CreditBalanceHistory.builder()
                    .credit(cb.getCredit())
                    .contractNumber(cb.getContractNumber())
                    .accountCode(cb.getAccountCode())
                    .date(date)
                    .openingBalance(cb.getBalance()) // можно добавить расчёт, если нужно
                    .closingBalance(cb.getBalance())
                    .debitTurnover(BigDecimal.ZERO)
                    .creditTurnover(BigDecimal.ZERO)
                    .createdAt(LocalDateTime.now())
                    .build();
            historyRepo.save(history);
        }
    }

    public List<CreditBalance> getBalancesByContract(String contractNumber) {
        return balanceRepo.findByContractNumber(contractNumber);
    }

    public List<CreditBalanceHistory> getHistoryByContract(String contractNumber) {
        return historyRepo.findByContractNumberOrderByDateDesc(contractNumber);
    }
}