package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceDTO {
    private String accountCode;
    private String accountName;
    private BigDecimal beginActive;
    private BigDecimal beginPassive;
    private BigDecimal debitTurnover;
    private BigDecimal creditTurnover;
    private BigDecimal endActive;
    private BigDecimal endPassive;
}