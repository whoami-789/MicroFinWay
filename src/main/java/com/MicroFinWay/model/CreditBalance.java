package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    @Column(name = "contract_number", nullable = false, length = 20)
    private String contractNumber;

    @Column(name = "account_code", nullable = false, length = 20)
    private String accountCode; // 12401, 16307 и т.п.

    @Column(name = "balance", precision = 20, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}