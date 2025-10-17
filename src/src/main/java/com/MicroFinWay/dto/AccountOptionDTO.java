package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountOptionDTO {
    private String account;
    private BigDecimal balance;
}