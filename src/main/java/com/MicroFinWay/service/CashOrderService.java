package com.MicroFinWay.service;

import com.MicroFinWay.dto.*;
import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.CreditAccount;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditAccountRepository;
import com.MicroFinWay.search.CreditIndex;
import com.MicroFinWay.repository.CreditSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashOrderService {

    private final AccountingRepository accountingRepository;
    private final CreditAccountRepository creditAccountRepository;
    private final CreditSearchRepository creditSearchRepository;

    // === Шаг 1: поиск договоров в эластике ===
    public List<ContractSearchDTO> searchContracts(String prefix) {
        List<CreditIndex> hits = creditSearchRepository.findByContractNumberStartingWith(prefix);
        return hits.stream()
                .map(c -> new ContractSearchDTO(
                        c.getContractNumber(),
                        c.getClientName(),
                        c.getAmount()
                ))
                .toList();
    }

    // === Шаг 2: поиск счетов по договору и префиксу ===
    public List<AccountOptionDTO> searchAccounts(String contractNumber, String prefix) {
        List<String> accounts = findPossibleAccounts(contractNumber, prefix);
        List<AccountOptionDTO> res = new ArrayList<>();

        for (String acc : accounts) {
            res.add(new AccountOptionDTO(acc, getAccountBalance(acc)));
        }
        return res;
    }

    // === Шаг 3: Preview ===
    public List<AccountBalanceDTO> previewBalances(String contractNumber, String debit, String credit, BigDecimal amount) {
        List<AccountBalanceDTO> results = new ArrayList<>();

        BigDecimal debitBal = getAccountBalance(debit);
        results.add(new AccountBalanceDTO(debit, debitBal, debitBal.add(amount)));

        BigDecimal creditBal = getAccountBalance(credit);
        results.add(new AccountBalanceDTO(credit, creditBal, creditBal.subtract(amount)));

        return results;
    }

    // === Шаг 4: Save ===
    @Transactional
    public Long createCashOrder(CashOrderDTO dto) {
        if (dto.getAmount() == null || dto.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        // проверка остатка при списании
        BigDecimal balance = getAccountBalance(dto.getCreditAccount());
        if (balance.compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Недостаточно средств на счёте " + dto.getCreditAccount());
        }

        Accounting entry = new Accounting();
        entry.setDebitAccount(dto.getDebitAccount());
        entry.setCreditAccount(dto.getCreditAccount());
        entry.setAmount(dto.getAmount());
        entry.setContractNumber(dto.getContractNumber());
        entry.setDescription(dto.getDescription());
        entry.setTransactionType("CASH_ORDER");
        entry.setOperationDate(dto.getOperationDate() != null ? dto.getOperationDate() : LocalDate.now());
        entry.setStatus(dto.isDraft() ? 0 : 1);

        return accountingRepository.save(entry).getId();
    }

    // === Утилиты ===
    private List<String> findPossibleAccounts(String contractNumber, String prefix) {
        CreditAccount ca = creditAccountRepository.findByContractNumber(contractNumber).orElse(null);
        List<String> res = new ArrayList<>();

        if (ca != null) {
            for (Field f : ca.getClass().getDeclaredFields()) {
                if (!f.getName().startsWith("account")) continue;
                f.setAccessible(true);
                try {
                    Object v = f.get(ca);
                    if (v instanceof String s && s.startsWith(prefix)) {
                        res.add(s);
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }

        if (res.isEmpty()) {
            res.add(prefix); // орг. счёт
        }
        return res;
    }

    private BigDecimal getAccountBalance(String account) {
        BigDecimal debit = accountingRepository.sumDebit(account);
        BigDecimal credit = accountingRepository.sumCredit(account);
        return debit.subtract(credit);
    }
}