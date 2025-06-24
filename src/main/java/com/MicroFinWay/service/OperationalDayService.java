package com.MicroFinWay.service;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationalDayService {

    private final CreditRepository creditRepository;
    private final AccountingService accountingService;
    private final OrganizationService organizationService;

    public void openOperationalDay(LocalDate date) {
        LocalDate current = organizationService.getCurrentOperationalDay();

        while (!current.isAfter(date)) {
            processSingleOperationalDay(current);
            organizationService.setCurrentOperationalDay(current.plusDays(1));

            current = current.plusDays(1);
        }

        System.out.println("Операционный день успешно закрыт до " + date);
    }

    /**
     * Логика операционного дня на указанную дату (обрабатывает каждый день по отдельности)
     */
    public void closeOperationalDay(LocalDate toDate) {
        System.out.println("Операционный день открыт: " + toDate);
        organizationService.initializeIfNotExists();
    }

    private void processSingleOperationalDay(LocalDate currentDate) {
        updateOverdueStatusForCredits(currentDate);

        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            if (credit.getContractEndDate() != null && currentDate.isAfter(credit.getContractEndDate())) {
                continue;
            }

            BigDecimal dailyInterest = calculateDailyInterest(credit);
            boolean hasOverdue = hasOverdueInterest(credit, currentDate);
            boolean allOverduePaid = isAllOverdueInterestPaid(credit);

            if (hasOverdue) {
                // 🔸 Есть активная просрочка — начисляем просроченные проценты
                credit.setInterestIsOverdue(true);

                if (credit.getPaymentMethod() == Credit.PaymentMethod.BANK_TRANSFER) {
                    accountingService.accrueByCreditInTransitAccountOverdue(
                            credit.getContractNumber(), dailyInterest);
                } else {
                    accountingService.accrueInterestOverdue(
                            credit.getContractNumber(), dailyInterest);
                }

            } else if (!hasOverdue && !allOverduePaid) {
                // ⏸ Есть неоплаченные просрочки, но нет новых — ничего не делаем
                // Ждём полного погашения

            } else if (!hasOverdue && allOverduePaid) {
                // ✅ Все просроченные проценты оплачены — делаем возврат и начисляем обычные
                if (Boolean.TRUE.equals(credit.getInterestIsOverdue())) {
                    BigDecimal total = calculateTotalOverdueInterest(credit);
                    accountingService.returnOverdueInterestToNormal(
                            credit.getContractNumber(), total);
                    credit.setInterestIsOverdue(false);
                }

                if (credit.getPaymentMethod() == Credit.PaymentMethod.BANK_TRANSFER) {
                    accountingService.accrueInterestByCreditInTransitAccount(
                            credit.getContractNumber(), dailyInterest);
                } else {
                    accountingService.accrueInterest(
                            credit.getContractNumber(), dailyInterest);
                }
            }

            checkReserveTransfers(credit);
            credit.setLastUpdatedTime(LocalDateTime.now());
        }

        creditRepository.saveAll(activeCredits);
    }


    private BigDecimal calculateTotalOverdueInterest(Credit credit) {
        return credit.getPaymentSchedules().stream()
                .filter(ps -> Boolean.TRUE.equals(ps.getInterestOverdueMoved()))
                .map(ps -> ps.getInterestPayment() != null ? ps.getInterestPayment() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }




    private boolean hasOverdueInterest(Credit credit, LocalDate currentDate) {
        return credit.getPaymentSchedules().stream()
                .filter(ps -> ps.getInterestPayment() != null && ps.getInterestPayment().compareTo(BigDecimal.ZERO) > 0)
                .filter(ps -> ps.getPaymentStatus() == 0 || ps.getPaymentStatus() == 2)
                .anyMatch(ps -> ps.getDueDate() != null && ps.getDueDate().isBefore(currentDate));
    }


    private BigDecimal calculateDailyInterest(Credit credit) {
        BigDecimal rate = credit.getInterestRate();
        BigDecimal amount = credit.getAmount();

        return amount
                .multiply(rate)
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP);
    }

    private void updateOverdueStatusForCredits(LocalDate currentDate) {
        updatePrincipalOverdues(currentDate);
        updateInterestOverdues(currentDate);
    }

    private void updatePrincipalOverdues(LocalDate currentDate) {
        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            credit.getPaymentSchedules().stream()
                    .filter(ps -> ps.getPrincipalPayment() != null && ps.getPrincipalPayment().compareTo(BigDecimal.ZERO) > 0)
                    .filter(ps -> ps.getPaymentStatus() == 0 || ps.getPaymentStatus() == 2)
                    .filter(ps -> ps.getDueDate() != null && ps.getDueDate().isBefore(currentDate))
                    .filter(ps -> !Boolean.TRUE.equals(ps.getPrincipalOverdueMoved())) // 🔹 уже не переносили
                    .findFirst()
                    .ifPresent(overdue -> {
                        long days = ChronoUnit.DAYS.between(overdue.getDueDate(), currentDate);
                        credit.setOverdueDate(overdue.getDueDate());
                        credit.setOverdueDays((int) days);
                        credit.setOverdueStatus(Credit.OverdueStatus.ACTIVE);

                        accountingService.moveMainToOverdue(
                                credit.getContractNumber(),
                                overdue.getPrincipalPayment() != null ? overdue.getPrincipalPayment() : BigDecimal.ZERO
                        );

                        overdue.setPrincipalOverdueMoved(true); // 🔹 отметка что проводка сделана
                    });
        }

        creditRepository.saveAll(activeCredits);
    }


    private void updateInterestOverdues(LocalDate currentDate) {
        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            credit.getPaymentSchedules().stream()
                    .filter(ps -> ps.getInterestPayment() != null && ps.getInterestPayment().compareTo(BigDecimal.ZERO) > 0)
                    .filter(ps -> ps.getPaymentStatus() == 0 || ps.getPaymentStatus() == 2)
                    .filter(ps -> ps.getDueDate() != null && ps.getDueDate().isBefore(currentDate))
                    .filter(ps -> !Boolean.TRUE.equals(ps.getInterestOverdueMoved())) // 🔹 проводка ещё не делалась
                    .findFirst()
                    .ifPresent(overdue -> {
                        long days = ChronoUnit.DAYS.between(overdue.getDueDate(), currentDate);
                        credit.setOverdueDate(overdue.getDueDate());
                        credit.setOverdueDays((int) days);
                        credit.setOverdueStatus(Credit.OverdueStatus.ACTIVE);

                        accountingService.moveInterestToOverdue(
                                credit.getContractNumber(),
                                overdue.getInterestPayment() != null ? overdue.getInterestPayment() : BigDecimal.ZERO
                        );

                        overdue.setInterestOverdueMoved(true); // 🔹 помечаем как обработанный
                    });

        }

        creditRepository.saveAll(activeCredits);
    }

    private void checkReserveTransfers(Credit credit) {
        if (credit.getOverdueStatus() != Credit.OverdueStatus.ACTIVE || credit.getOverdueDays() == null) return;

        int days = credit.getOverdueDays();
        BigDecimal amount = credit.getAmount();
        String contract = credit.getContractNumber();

        if (days >= 180 && !Boolean.TRUE.equals(credit.getReserve100Done())) {
            accountingService.reserve100Percent(contract, amount);
            credit.setReserve100Done(true); // 🔹 флажок обновляется
        }

        if (days >= 90 && !Boolean.TRUE.equals(credit.getReserve50Done())) {
            accountingService.reserve50Percent(contract, amount);
            credit.setReserve50Done(true);
        }

        if (days >= 60 && !Boolean.TRUE.equals(credit.getReserve25Done())) {
            accountingService.reserve25Percent(contract, amount);
            credit.setReserve25Done(true);
        }
    }


    private boolean isAllOverdueInterestPaid(Credit credit) {
        return credit.getPaymentSchedules().stream()
                .filter(ps -> ps.getInterestPayment() != null && ps.getInterestPayment().compareTo(BigDecimal.ZERO) > 0)
                .filter(ps -> ps.getPaymentStatus() != null)
                .filter(ps -> Boolean.TRUE.equals(ps.getInterestOverdueMoved())) // значит, проводка 16307 → 16377 уже была
                .allMatch(ps -> ps.getPaymentStatus() == 1); // 1 = полностью оплачено
    }



}