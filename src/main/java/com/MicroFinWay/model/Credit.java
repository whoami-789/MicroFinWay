package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель для хранения информации о кредите.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Уникальный идентификатор кредита (генерируется автоматически)

    @Column(name = "code", length = 8, nullable = false)
    private String code;
    // Код кредита (например, CR123)

    @Column(name = "contract_number", length = 12, nullable = false)
    private String contractNumber;
    // Номер кредитного договора

    @Column(name = "contract_date")
    private LocalDate contractDate;
    // Дата заключения договора (также используется как дата создания)

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;
    // Дата окончания действия договора

    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;
    // Сумма кредита

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;
    // Код валюты (например, USD)

    @Column(name = "collateral_type")
    private Integer collateralType;
    // Тип залога

    @Column(name = "loan_term")
    private Integer loanTerm;
    // Срок кредита в месяцах

    @Column(name = "interest_rate", precision = 6, scale = 2, nullable = false)
    private BigDecimal interestRate;
    // Процентная ставка

    @Column(name = "purpose")
    private Integer purpose;
    // Цель кредита

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CreditStatus status;
    // Статус кредита (например, ACTIVE, CLOSED, REJECTED)

    @Column(name = "note", length = 160)
    private String note;
    // Дополнительные заметки

    @Column(name = "credit_type")
    private Integer creditType;
    // Тип кредита (например, потребительский, ипотечный)

    @Column(name = "credit_duration")
    private Integer creditDuration;
    // Длительность кредита в месяцах

    @Column(name = "last_updated_time")
    private LocalDateTime lastUpdatedTime;
    // Дата и время последнего обновления записи

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    // Способ оплаты (например, CASH, BANK_TRANSFER)

    @Column(name = "additional_agreements", length = 10)
    private String additionalAgreements;
    // Дополнительные соглашения

    @Column(name = "judicial_loan_account", length = 20)
    private String judicialLoanAccount;
    // Лицевой счёт для судебного кредита

    @Column(name = "min_payment", precision = 18, scale = 2)
    private BigDecimal minPayment;
    // Минимальный ежемесячный платеж

    @Column(name = "fees", precision = 4, scale = 1)
    private BigDecimal fees;
    // Комиссии по кредиту

    @Column(name = "is_law_eligible")
    private Integer isLawEligible;
    // Флаг соответствия специальным правовым условиям

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status")
    private ProgressStatus progressStatus;
    // Текущее состояние кредита (например, PENDING, APPROVED)

    @Column(name = "special_conditions")
    private Integer specialConditions;
    // Наличие специальных условий

    @Column(name = "penalty_account")
    private String penaltyAccount;
    // Счёт для штрафов

    @Column(name = "penalty_rate", precision = 10, scale = 2)
    private BigDecimal penaltyRate;
    // Ставка штрафных санкций

    @Column(name = "grki_claim_id", length = 18)
    private String grkiClaimId;
    // Идентификатор в системе GRKI (например, претензия)

    @Column(name = "grki_agreement_id", length = 50)
    private String grkiAgreementId;
    // Идентификатор соглашения в GRKI

    @Column(name = "grki_contract_id", length = 50)
    private String grkiContractId;
    // Идентификатор контракта в GRKI

    @Column(name = "closure_date")
    private LocalDate closureDate;
    // Дата закрытия кредита

    @Column(name = "overdue_date")
    private LocalDate overdueDate;
    // Дата начала просрочки

    @Column(name = "contract_modification_date")
    private LocalDate contractModificationDate;
    // Дата изменения условий кредитного договора

    @Column(name = "overdue_days")
    private Integer overdueDays;
    // Количество дней просрочки

    @Column(name = "penalty_amount", precision = 20, scale = 2)
    private BigDecimal penaltyAmount;
    // Сумма штрафа/пени за просрочку

    @Enumerated(EnumType.STRING)
    @Column(name = "overdue_status")
    private OverdueStatus overdueStatus;
    // Статус просрочки (например, NO_OVERDUE, ACTIVE)

    @Column(name = "grace_period")
    private Integer gracePeriod;
    // Льготный период в днях

    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    // Дата последнего платежа

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;
    // Дата следующего платежа

    @Column(name = "loan_paid_off_date")
    private LocalDate loanPaidOffDate;
    // Дата полного погашения кредита

    // Блок счетов для бухгалтерии (оставляем как есть)
    @Column(name = "account_loan_main", length = 20)
    private String accountLoanMain;  // Основной счёт кредита

    @Column(name = "account_interest_general", length = 20)
    private String accountInterestGeneral;  // Счёт процентов

    @Column(name = "account_collateral", length = 20)
    private String accountCollateral;  // Счёт процентов

    @Column(name = "account_overdue_loan", length = 50)
    private String accountOverdueLoan;  // Счёт просроченного кредита

    @Column(name = "account_outside_interest", length = 20)
    private String accountOutsideInterest;  // Внешний счёт процентов

    @Column(name = "account_assignment", length = 20)
    private String accountAssignment;  // Счёт переуступки

    @Column(name = "account_external_contr", length = 20)
    private String accountExternalContr;  // Внешние контрагенты

    @Column(name = "account_credit_writeoff", length = 50)
    private String accountCreditWriteoff;  // Счёт списания кредита

    @Column(name = "account_overdue_interest", length = 50)
    private String accountOverdueInterest;  // Счёт просроченных процентов

    @Column(name = "account_22812", length = 50)
    private String account22812;  // Специальный счёт 22812

    @Column(name = "account_penalty_additional", length = 50)
    private String accountPenaltyAdditional;  // Счёт пени

    @Column(name = "account_reserve", length = 50)
    private String accountReserve;  // Счёт резервов

    @Column(name = "account_income", length = 50)
    private String accountIncome;  // Счёт доходов

    @Column(name = "created_by")
    private Long createdBy;
    // Идентификатор администратора/сотрудника, который создал кредит

    @Column(name = "paid_amount", precision = 20, scale = 2)
    private BigDecimal paidAmount;
    // Сумма уже выплаченного по кредиту

    @Column(name = "source")
    private String source;
    // Источник оформления кредита (например, Офис, Онлайн)

    @Column(name = "legal_decision_number")
    private String legalDecisionNumber;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentSchedule> paymentSchedules;
    // Список графиков платежей

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // Ссылка на пользователя (клиента), который получил кредит

    // Enum для статусов
    public enum CreditStatus {
        INACTIVE, ACTIVE, CLOSED, REJECTED
    }

    public enum OverdueStatus {
        NO_OVERDUE, ACTIVE, CLOSED
    }

    public enum PaymentMethod {
        CASH, BANK_TRANSFER
    }

    public enum ProgressStatus {
        PENDING, APPROVED, MONITORING, COMPLETED
    }
}
