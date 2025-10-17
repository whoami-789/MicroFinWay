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

    @Column(name = "cmmss", length = 50, nullable = false, unique = true)
    private String cmmss;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "template_code", length = 50, nullable = false)
    private String templateCode;  // Например: "CREDIT_BODY", "INTEREST", "OVERDUE", "COLLATERAL", "RESERVE"
}

