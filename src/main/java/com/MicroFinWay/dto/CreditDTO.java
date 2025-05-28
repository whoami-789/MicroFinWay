package com.MicroFinWay.dto;

import com.MicroFinWay.model.Credit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    private Long id;
    private String code;
    private String contractNumber;
    private LocalDate contractDate;
    private LocalDate contractEndDate;
    private BigDecimal amount;
    private String currencyCode;
    private Integer collateralType;
    private Integer loanTerm;
    private BigDecimal interestRate;
    private Integer purpose;
    private Credit.CreditStatus status;
    private String note;
    private Integer creditType;
    private Integer creditDuration;
    private LocalDateTime lastUpdatedTime;
    private Credit.PaymentMethod paymentMethod;
    private String additionalAgreements;
    private String judicialLoanAccount;
    private BigDecimal minPayment;
    private BigDecimal fees;
    private Integer isLawEligible;
    private Credit.ProgressStatus progressStatus;
    private Integer specialConditions;
    private String penaltyAccount;
    private BigDecimal penaltyRate;
    private String grkiClaimId;
    private String grkiAgreementId;
    private String grkiContractId;
    private LocalDate closureDate;
    private LocalDate overdueDate;
    private LocalDate contractModificationDate;
    private Integer overdueDays;
    private BigDecimal penaltyAmount;
    private Credit.OverdueStatus overdueStatus;
    private Integer gracePeriod;
    private LocalDate lastPaymentDate;
    private LocalDate nextDueDate;
    private LocalDate loanPaidOffDate;
    private String accountLoanMain;
    private String accountInterestGeneral;
    private String accountCollateral;
    private String accountOverdueLoan;
    private String accountOutsideInterest;
    private String accountAssignment;
    private String accountExternalContr;
    private String accountCreditWriteoff;
    private String accountOverdueInterest;
    private String account22812;
    private String accountPenaltyAdditional;
    private String accountReserve;
    private String accountIncome;
    private Long createdBy;
    private BigDecimal paidAmount;
    private String source;
    private String legalDecisionNumber;
}
