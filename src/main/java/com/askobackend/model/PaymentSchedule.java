package com.askobackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credit_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Уникальный идентификатор записи

    @Column(name = "due_date")
    private LocalDate dueDate; // Дата платежа

    @Column(name = "payment_date")
    private LocalDate paymentDate; // Дата фактического платежа

    @Column(name = "principal_payment")
    private BigDecimal principalPayment; // Сумма погашения основного долга

    @Column(name = "interest_payment")
    private BigDecimal interestPayment; // Сумма погашения процентов

    @Column(name = "remaining_balance")
    private BigDecimal remainingBalance; // Остаток по кредиту

    @Column(name = "payment_month", nullable = false)
    private int paymentMonth; // Номер месяца

    @Column(name = "status")
    private Integer status; // Статус (например, 0 - не погашено, 1 - погашено, 2 - просрочено)

    @Column(name = "contract_number", nullable = false)
    private String contractNumber; // Номер договора

    @Column(name = "penalty_amount")
    private BigDecimal penaltyAmount; // Сумма штрафа за просрочку

    @Column(name = "penalty_status")
    private Integer penaltyStatus; // Статус штрафа (например, 0 - не применен, 1 - применен)

    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount; // Оставшаяся сумма по договору

    @Column(name = "grace_period")
    private BigDecimal gracePeriod; // Период отсрочки

    @Column(name = "payment_status")
    private Integer paymentStatus; // Статус платежа (например, 0 - ожидает, 1 - оплачен, 2 - частично оплачен)

    @ManyToOne
    @JoinColumn(name = "contract_number", referencedColumnName = "contract_number", nullable = false)
    private Credit credit;

}

