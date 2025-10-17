package com.MicroFinWay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Accounting {
    @Id
    @GeneratedValue
    private Long id;

    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;

    private LocalDate operationDate;
    private String description;
    private String transactionType;
    private String contractNumber;
    private int status; // 0 - черновик, 1 - проведено
}
