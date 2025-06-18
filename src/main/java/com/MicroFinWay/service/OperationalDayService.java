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

    /**
     * Открытие операционного дня — пока ничего не делает, но можно добавить подготовку
     */
    public void openOperationalDay(LocalDate date) {
        System.out.println("Операционный день открыт: " + date);
        // Можно добавить логирование, проверки и т.п.
    }

    /**
     * Закрытие операционного дня — автоматические проводки
     */
    public void closeOperationalDay(LocalDate currentDate) {
        // Обновляем просрочки
        updateOverdueStatusForCredits(currentDate);

        // Обрабатываем активные кредиты
        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            if (credit.getContractEndDate() != null && currentDate.isAfter(credit.getContractEndDate())) {
                continue;
            }

            BigDecimal dailyInterest = calculateDailyInterest(credit);
            accountingService.accrueInterest(credit.getContractNumber(), dailyInterest);

            credit.setLastUpdatedTime(LocalDateTime.now());
        }

    }

    private BigDecimal calculateDailyInterest(Credit credit) {
        BigDecimal rate = credit.getInterestRate(); // Годовая ставка в %
        BigDecimal amount = credit.getAmount();

        return amount
                .multiply(rate)
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP) // Перевод в дробную ставку
                .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP); // Делим на количество дней
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

                        accountingService.movePrincipalToOverdue(
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
                    .findFirst()
                    .ifPresent(overdue -> {
                        long days = ChronoUnit.DAYS.between(overdue.getDueDate(), currentDate);
                        credit.setOverdueDate(overdue.getDueDate()); // Можно сделать отдельное поле для процентов при желании
                        credit.setOverdueDays((int) days);
                        credit.setOverdueStatus(Credit.OverdueStatus.ACTIVE);

                        accountingService.moveInterestToOverdue(
                                credit.getContractNumber(),
                                overdue.getInterestPayment() != null ? overdue.getInterestPayment() : BigDecimal.ZERO
                        );
                    });
        }

        creditRepository.saveAll(activeCredits);
    }


}
