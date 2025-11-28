package com.MicroFinWay.service;

import com.MicroFinWay.dto.BalanceDTO;
import com.MicroFinWay.model.AccountPlan;
import com.MicroFinWay.repository.AccountPlanRepository;
import com.MicroFinWay.repository.AccountingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountingRepository accountingRepository;
    private final AccountPlanRepository accountPlanRepository;

    public List<BalanceDTO> calculateBalance(LocalDate date) {
        List<AccountPlan> plans = accountPlanRepository.findAll();
        List<BalanceDTO> result = new ArrayList<>();

        for (AccountPlan plan : plans) {
            String code = plan.getCode();

            // Обороты за день
            BigDecimal debit = accountingRepository.sumDebit(code, date);
            BigDecimal credit = accountingRepository.sumCredit(code, date);

            // Остаток на начало = все проводки ДО этой даты
            BigDecimal totalDebitBefore = accountingRepository.sumDebitBefore(code, date);
            BigDecimal totalCreditBefore = accountingRepository.sumCreditBefore(code, date);

            BigDecimal beginActive = BigDecimal.ZERO;
            BigDecimal beginPassive = BigDecimal.ZERO;
            BigDecimal endActive = BigDecimal.ZERO;
            BigDecimal endPassive = BigDecimal.ZERO;

            if (plan.getType() == AccountPlan.Type.ACTIVE) {
                beginActive = totalDebitBefore.subtract(totalCreditBefore);
                endActive = beginActive.add(debit).subtract(credit);
            } else {
                beginPassive = totalCreditBefore.subtract(totalDebitBefore);
                endPassive = beginPassive.add(credit).subtract(debit);
            }

            result.add(new BalanceDTO(code, plan.getName(),
                    beginActive, beginPassive, debit, credit, endActive, endPassive));
        }

        return result;
    }
}