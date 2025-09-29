package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountBalanceDTO {
    private String account;
    private BigDecimal currentBalance;
    private BigDecimal afterOperation;
}
