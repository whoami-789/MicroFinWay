package com.MicroFinWay.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashOrderPreviewRequest {
    private String contractNumber;
    private String debit;   // уже выбранный конкретный счёт, не префикс
    private String credit;  // уже выбранный конкретный счёт, не префикс
    private BigDecimal amount;
}