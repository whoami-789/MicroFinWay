package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    private String clientFullName;
    private String debitAccount;        // 10101…
    private BigDecimal mainDebt;        // Остаток по телу
    private BigDecimal overdueInterest; // Проценты (в т.ч. просроченные)
    private BigDecimal penalty;         // Пени
    private BigDecimal fine;            // Штрафы
    private BigDecimal other;           // Прочие платежи
}