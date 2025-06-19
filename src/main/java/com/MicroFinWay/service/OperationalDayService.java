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
     * Закрытие операционного дня на указанную дату (обрабатывает каждый день по отдельности)
     */
    public void closeOperationalDay(LocalDate toDate) {
        System.out.println("Операционный день открыт: " + toDate);
        organizationService.initializeIfNotExists();
    }

    /**
     * Логика закрытия одного операционного дня
     */
    private void processSingleOperationalDay(LocalDate currentDate) {
        updateOverdueStatusForCredits(currentDate);

        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            if (credit.getContractEndDate() != null && currentDate.isAfter(credit.getContractEndDate())) {
                continue;
            }

            BigDecimal dailyInterest = calculateDailyInterest(credit);

            if (hasOverdueInterest(credit, currentDate)) {
                accountingService.accrueInterestOverdue(credit.getContractNumber(), dailyInterest);
            } else {
                accountingService.accrueInterest(credit.getContractNumber(), dailyInterest);
            }

            credit.setLastUpdatedTime(LocalDateTime.now());
        }

        creditRepository.saveAll(activeCredits);
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
}
