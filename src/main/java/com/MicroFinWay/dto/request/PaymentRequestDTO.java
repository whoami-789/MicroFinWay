package com.MicroFinWay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private String documentNumber;
    private boolean isCash;
    private String purpose;
    private BigDecimal mainDebt;
    private BigDecimal interest;
    private BigDecimal penalty;
    private BigDecimal fine;
    private BigDecimal other;
    private BigDecimal totalAmount;
}