package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_balance_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditBalanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    @Column(name = "contract_number", nullable = false, length = 20)
    private String contractNumber;

    @Column(name = "account_code", nullable = false, length = 20)
    private String accountCode;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "opening_balance", precision = 20, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "debit_turnover", precision = 20, scale = 2)
    private BigDecimal debitTurnover;

    @Column(name = "credit_turnover", precision = 20, scale = 2)
    private BigDecimal creditTurnover;

    @Column(name = "closing_balance", precision = 20, scale = 2)
    private BigDecimal closingBalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}