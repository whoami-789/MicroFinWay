package com.MicroFinWay.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CashOrderDTO {
    private String contractNumber;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private String description;
    private boolean draft;
    private LocalDate operationDate;
}