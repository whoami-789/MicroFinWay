package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_types")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cmmss", length = 5, nullable = false, unique = true)
    private String cmmss;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "account_purpose", length = 50, nullable = false)
    private String accountPurpose;  // Например: "CREDIT_BODY", "INTEREST", "OVERDUE", "COLLATERAL", "RESERVE"
}

