package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ContractSearchDTO {
    private String contractNumber;
    private String clientName;
    private BigDecimal amount;
}