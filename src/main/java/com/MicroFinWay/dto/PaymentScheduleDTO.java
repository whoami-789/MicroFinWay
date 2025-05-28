package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleDTO {
    private Long id;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal principalPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingBalance;
    private int paymentMonth;
    private BigDecimal outstandingAmount;
    private BigDecimal gracePeriod;
    private Integer paymentStatus;
    private String contractNumber;
}
