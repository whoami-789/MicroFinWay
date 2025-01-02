package com.askobackend.model;

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
    // Дата заключения договора

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

    @Column(name = "status")
    private Integer status;
    // Статус кредита (например, 0 - не активен, 1 - активен)

    @Column(name = "note", length = 160)
    private String note;
    // Дополнительные заметки

    @Column(name = "credit_type")
    private Integer creditType;
    // Тип кредита (например, потребительский, ипотечный)

    @Column(name = "credit_duration")
    private Integer creditDuration;
    // Дополнительное поле длительности кредита (в месяцах)

    @Column(name = "user_id")
    private Long userId;
    // Идентификатор пользователя (внешний ключ)

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    // Ссылка на сущность User, которая оформила кредит

    @Column(name = "loan_account", length = 20)
    private String loanAccount;
    // Номер лицевого счета по кредиту

    @Column(name = "interest_account", length = 20)
    private String interestAccount;
    // Номер лицевого счета по процентам

    @Column(name = "last_updated_time")
    private LocalDateTime lastUpdatedTime;
    // Дата и время последнего обновления записи

    @Column(name = "sms_flag")
    private Integer smsFlag;
    // Флаг отправки SMS-уведомлений

    @Column(name = "phone_flag")
    private Integer phoneFlag;
    // Флаг уведомлений по телефону

    @Column(name = "payment_method")
    private Integer paymentMethod;
    // Способ оплаты (например, 0 - наличные, 1 - безнал)

    @Column(name = "can_use_non_cash")
    private Integer canUseNonCash;
    // Признак возможности безналичных платежей

    @Column(name = "additional_agreements", length = 10)
    private String additionalAgreements;
    // Дополнительные соглашения

    @Column(name = "judicial_loan_account", length = 20)
    private String judicialLoanAccount;
    // Лицевой счет для судебного кредита (при взыскании)

    @Column(name = "min_payment", precision = 18, scale = 2)
    private BigDecimal minPayment;
    // Минимальный ежемесячный платеж

    @Column(name = "fees", precision = 4, scale = 1)
    private BigDecimal fees;
    // Сборы и комиссии по кредиту

    @Column(name = "is_law_eligible")
    private Integer isLawEligible;
    // Флаг, показывающий соответствие специальным правовым условиям

    @Column(name = "progress_status")
    private Integer progressStatus;
    // Текущее состояние выполнения условий кредита

    @Column(name = "special_conditions")
    private Integer specialConditions;
    // Наличие специальных условий

    @Column(name = "penalty_account")
    private String penaltyAccount;
    // Счет для штрафных санкций

    @Column(name = "penalty_rate", precision = 10, scale = 2)
    private BigDecimal penaltyRate;
    // Ставка пени/штрафов

    @Column(name = "penalty_status")
    private Integer penaltyStatus;
    // Статус пени/штрафов

    @Column(name = "grki_claim_id", length = 18)
    private String grkiClaimId;
    // Идентификатор претензии в системе GRKI

    @Column(name = "grki_agreement_id", length = 50)
    private String grkiAgreementId;
    // Идентификатор соглашения в системе GRKI

    @Column(name = "grki_contract_id", length = 50)
    private String grkiContractId;
    // Идентификатор контракта в системе GRKI

    @Column(name = "closure_date")
    private LocalDate closureDate;
    // Дата закрытия кредита

    @Column(name = "overdue_date")
    private LocalDate overdueDate;
    // Дата, с которой началась просрочка

    @Column(name = "contract_modification_date")
    private LocalDate contractModificationDate;
    // Дата изменения условий кредитного договора

    // Поля для отслеживания просрочки и дополнительных деталей
    @Column(name = "overdue_days")
    private Integer overdueDays;
    // Количество дней просрочки

    @Column(name = "penalty_amount", precision = 20, scale = 2)
    private BigDecimal penaltyAmount;
    // Сумма штрафа/пени за просрочку

    @Column(name = "overdue_status")
    private Integer overdueStatus;
    // Статус просроченной задолженности (например, 0 - нет просрочки, 1 - активная)

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

    //----------------------------------------------------------------

    // Блок полей для различных счетов по кредиту:

    @Column(name = "account_loan_main", length = 20)
    private String accountLoanMain;
    // Основной счёт кредита (ранее lskred)

    @Column(name = "account_interest_general", length = 20)
    private String accountInterestGeneral;
    // Счёт процентов (ранее lsproc)

    @Column(name = "account_overdue_loan", length = 50)
    private String accountOverdueLoan;
    // Счёт просроченного кредита (ранее lsprosr_kred)

    @Column(name = "account_outside_interest", length = 20)
    private String accountOutsideInterest;
    // "Внешний" счёт для просроченных процентов (ранее lsprocvne)

    @Column(name = "account_assignment", length = 20)
    private String accountAssignment;
    // Счёт для переуступки / иных операций (ранее ls_peres)

    @Column(name = "account_external_contr", length = 20)
    private String accountExternalContr;
    // Счёт для встречных внешних операций (ранее ls_kontrvne)

    @Column(name = "account_credit_writeoff", length = 50)
    private String accountCreditWriteoff;
    // Счёт для списания кредита (ранее ls_spiskred)

    @Column(name = "account_overdue_interest", length = 50)
    private String accountOverdueInterest;
    // Счёт просроченных процентов (ранее lsprosr_proc)

    @Column(name = "account_22812", length = 50)
    private String account22812;
    // Специальный счёт (ранее ls22812)

    @Column(name = "account_penalty_additional", length = 50)
    private String accountPenaltyAdditional;
    // Счёт для пени (ранее lspeni)

    @Column(name = "account_reserve", length = 50)
    private String accountReserve;
    // Счёт резервов (ранее lsrezerv)

    @Column(name = "account_income", length = 50)
    private String accountIncome;
    // Счёт доходов по кредиту (ранее ls_dox)

    //----------------------------------------------------------------


    // Связь один-ко-многим: один кредит - много графиков платежей
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentSchedule> paymentSchedules;
}
