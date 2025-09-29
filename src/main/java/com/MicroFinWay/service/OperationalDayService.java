package com.MicroFinWay.service;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.Organization;
import com.MicroFinWay.repository.AccountingRepository;
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
            organizationService.setCurrentOperationalDay(current);

            current = current.plusDays(1);
            organizationService.setOperationalDayClosed(false); // помечаем как открыт
        }

        System.out.println("Операционный день успешно открыт до " + date);
    }

    /**
     * Логика операционного дня на указанную дату (обрабатывает каждый день по отдельности)
     */
    public void closeOperationalDay(LocalDate toDate) {
        System.out.println("Операционный день закрыт: " + toDate);
        organizationService.setCurrentOperationalDay(toDate);

        organizationService.setOperationalDayClosed(true); // закрыт
    }

    private void processSingleOperationalDay(LocalDate currentDate) {
        updateOverdueStatusForCredits(currentDate);

        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            if (credit.getContractEndDate() != null && currentDate.isAfter(credit.getContractEndDate())) {
                continue;
            }

            // 👇 Обновляем длительность просрочки (даже если перенос уже был)
            updateOverdueDuration(credit, currentDate);

            BigDecimal dailyInterest = calculateDailyInterest(credit);
            boolean hasOverdue = hasOverdueInterest(credit, currentDate);
            boolean allOverduePaid = isAllOverdueInterestPaid(credit);
            boolean isAdvancePayment = credit.getAdvance().equals(true);

            if (hasOverdue) {
                credit.setInterestIsOverdue(true);
                if (credit.getPaymentMethod() == Credit.PaymentMethod.BANK_TRANSFER) {
                    accountingService.accrueByCreditInTransitAccountOverdue(credit.getContractNumber(), dailyInterest);
                } else {
                    accountingService.accrueInterestOverdue(credit.getContractNumber(), dailyInterest);
                }
            } else if ((hasOverdue && credit.getDecommissioned()) == true) {
                accountingService.decommissionedInterest(credit.getContractNumber(), dailyInterest);
            } else if (!hasOverdue && !allOverduePaid) {
                // ничего не делаем, ждём погашения всех просрочек
            } else if (!hasOverdue && allOverduePaid && credit.getInterestIsOverdue()) {
                // возврат с 16377 в 16307
                BigDecimal total = calculateTotalOverdueInterest(credit);
                accountingService.returnOverdueInterestToNormal(credit.getContractNumber(), total);
                credit.setInterestIsOverdue(false);

                // обычное начисление
                if (credit.getPaymentMethod() == Credit.PaymentMethod.BANK_TRANSFER) {
                    accountingService.accrueInterestByCreditInTransitAccount(credit.getContractNumber(), dailyInterest);
                } else {
                    accountingService.accrueInterest(credit.getContractNumber(), dailyInterest);
                }
            } else if (!hasOverdue && !credit.getInterestIsOverdue()) {
                // никогда не было просрочки — обычное начисление
                if (credit.getPaymentMethod() == Credit.PaymentMethod.BANK_TRANSFER) {
                    if (isAdvancePayment) {
                        BigDecimal advanceTotal = credit.getAdvanceAmount();
                        if (advanceTotal.doubleValue() > dailyInterest.doubleValue()) {
                            accountingService.accrueInterestByCreditInTransitAccount(credit.getContractNumber(), BigDecimal.valueOf(advanceTotal.doubleValue() - dailyInterest.doubleValue()));
                            accountingService.transferAdvanceToInterestAccountBankTransfer(credit.getContractNumber(), BigDecimal.valueOf(advanceTotal.doubleValue() - dailyInterest.doubleValue()));
                        } else {
                            accountingService.accrueInterestByCreditInTransitAccount(credit.getContractNumber(), BigDecimal.valueOf(dailyInterest.doubleValue() - advanceTotal.doubleValue()));
                            accountingService.transferAdvanceToInterestAccountBankTransfer(credit.getContractNumber(), BigDecimal.valueOf(0));
                        }
                    } else {
                        accountingService.accrueInterestByCreditInTransitAccount(credit.getContractNumber(), dailyInterest);
                    }
                } else {
                    if (isAdvancePayment) {
                        BigDecimal advanceTotal = credit.getAdvanceAmount();
                        if (advanceTotal.doubleValue() > dailyInterest.doubleValue()) {
                            accountingService.accrueInterest(credit.getContractNumber(), BigDecimal.valueOf(advanceTotal.doubleValue() - dailyInterest.doubleValue()));
                            accountingService.transferAdvanceToInterestAccount(credit.getContractNumber(), BigDecimal.valueOf(advanceTotal.doubleValue() - dailyInterest.doubleValue()));
                        } else {
                            accountingService.accrueInterest(credit.getContractNumber(), BigDecimal.valueOf(dailyInterest.doubleValue() - advanceTotal.doubleValue()));
                            accountingService.transferAdvanceToInterestAccount(credit.getContractNumber(), BigDecimal.valueOf(0));
                        }
                    } else {
                        accountingService.accrueInterest(credit.getContractNumber(), dailyInterest);
                    }
                }
            }


            // 🔄 Проверка на резерв
            checkReserveTransfers(credit);
            accruePenaltyIfNeeded(credit, currentDate);


            credit.setLastUpdatedTime(LocalDateTime.now());
        }

        creditRepository.saveAll(activeCredits);
    }


    private void updateOverdueDuration(Credit credit, LocalDate currentDate) {
        if (credit.getOverdueStatus() == Credit.OverdueStatus.ACTIVE && credit.getOverdueDate() != null) {
            long days = ChronoUnit.DAYS.between(credit.getOverdueDate(), currentDate);
            credit.setOverdueDays((int) days);
        }
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
                    .filter(ps -> !Boolean.TRUE.equals(ps.getPrincipalOverdueMoved()))
                    .findFirst()
                    .ifPresent(overdue -> {
                        // 👇 Устанавливаем overdueDate только если её ещё нет
                        if (credit.getOverdueDate() == null || overdue.getDueDate().isBefore(credit.getOverdueDate())) {
                            credit.setOverdueDate(overdue.getDueDate());
                        }

                        long days = ChronoUnit.DAYS.between(credit.getOverdueDate(), currentDate);
                        credit.setOverdueDays((int) days);
                        credit.setOverdueStatus(Credit.OverdueStatus.ACTIVE);

                        accountingService.moveMainToOverdue(
                                credit.getContractNumber(),
                                overdue.getPrincipalPayment() != null ? overdue.getPrincipalPayment() : BigDecimal.ZERO
                        );

                        overdue.setPrincipalOverdueMoved(true);
                    });
        }

        creditRepository.saveAll(activeCredits);
    }


    private void updateInterestOverdues(LocalDate currentDate) {
        List<Credit> activeCredits = creditRepository.findAllByStatus(Credit.CreditStatus.ACTIVE);

        for (Credit credit : activeCredits) {
            // Проводка делается только если ещё ни разу не было переноса процентов
            boolean alreadyMoved = credit.getPaymentSchedules().stream()
                    .anyMatch(ps -> Boolean.TRUE.equals(ps.getInterestOverdueMoved()));
            if (alreadyMoved) continue;

            // Находим первую просроченную строку
            credit.getPaymentSchedules().stream()
                    .filter(ps -> ps.getInterestPayment() != null && ps.getInterestPayment().compareTo(BigDecimal.ZERO) > 0)
                    .filter(ps -> ps.getPaymentStatus() == 0 || ps.getPaymentStatus() == 2)
                    .filter(ps -> ps.getDueDate() != null && ps.getDueDate().isBefore(currentDate))
                    .findFirst()
                    .ifPresent(overdue -> {
                        credit.setOverdueDate(overdue.getDueDate());
                        credit.setOverdueDays((int) ChronoUnit.DAYS.between(overdue.getDueDate(), currentDate));
                        credit.setOverdueStatus(Credit.OverdueStatus.ACTIVE);

                        accountingService.moveInterestToOverdue(
                                credit.getContractNumber(),
                                overdue.getInterestPayment() != null ? overdue.getInterestPayment() : BigDecimal.ZERO
                        );

                        // Помечаем все строки как перенесённые
                        credit.getPaymentSchedules().forEach(ps -> {
                            if (Boolean.FALSE.equals(ps.getInterestOverdueMoved())) {
                                ps.setInterestOverdueMoved(true);
                            }
                        });
                    });
        }

        creditRepository.saveAll(activeCredits);
    }


    private void checkReserveTransfers(Credit credit) {
        if (credit.getOverdueDate() == null || credit.getOverdueDays() == null) return;

        int days = credit.getOverdueDays();
        BigDecimal amount = credit.getAmount();
        String contract = credit.getContractNumber();

        // 📌 Новый блок: если срок возврата наступил и есть просрочка по телу — 100% резерв сразу
        if (credit.getContractEndDate() != null &&
                LocalDate.now().isAfter(credit.getContractEndDate()) &&
                days >= 1 &&
                !Boolean.TRUE.equals(credit.getReserve100Done())) {

            accountingService.reserve100Percent(contract, amount);
            credit.setReserve100Done(true);
            System.out.printf("🚨 Кредит %s просрочен по дате возврата — 100%% резерв отправлен%n", contract);
            return;
        }

        // 🔄 Обычные этапы (если правило выше не сработало)
        if (days >= 180 && !Boolean.TRUE.equals(credit.getReserve100Done())) {
            accountingService.reserve100Percent(contract, amount);
            credit.setReserve100Done(true);
            System.out.printf("✅ Проведена проводка 100%% резерва по кредиту %s%n", contract);
        }

        if (days >= 90 && !Boolean.TRUE.equals(credit.getReserve50Done())) {
            accountingService.reserve50Percent(contract, amount);
            credit.setReserve50Done(true);
            System.out.printf("✅ Проведена проводка 50%% резерва по кредиту %s%n", contract);
        }

        if (days >= 60 && !Boolean.TRUE.equals(credit.getReserve25Done())) {
            accountingService.reserve25Percent(contract, amount);
            credit.setReserve25Done(true);
            System.out.printf("✅ Проведена проводка 25%% резерва по кредиту %s%n", contract);
        }
    }


    private boolean isAllOverdueInterestPaid(Credit credit) {
        return credit.getPaymentSchedules().stream()
                .filter(ps -> ps.getInterestPayment() != null && ps.getInterestPayment().compareTo(BigDecimal.ZERO) > 0)
                .filter(ps -> ps.getPaymentStatus() != null)
                .filter(ps -> Boolean.TRUE.equals(ps.getInterestOverdueMoved())) // значит, проводка 16307 → 16377 уже была
                .allMatch(ps -> ps.getPaymentStatus() == 1); // 1 = полностью оплачено
    }

    private void accruePenaltyIfNeeded(Credit credit, LocalDate currentDate) {
        if (credit.getOverdueStatus() != Credit.OverdueStatus.ACTIVE || credit.getOverdueDate() == null) return;

        // Проверяем наличие ставки пени
        BigDecimal penaltyRate = credit.getPenaltyRate();
        if (penaltyRate == null || penaltyRate.compareTo(BigDecimal.ZERO) <= 0) return;

        credit.getPaymentSchedules().stream()
                .filter(ps -> ps.getDueDate() != null && ps.getDueDate().isBefore(currentDate))
                .filter(ps -> ps.getPaymentStatus() == 0 || ps.getPaymentStatus() == 2) // 0 = не оплачен, 2 = частично
                .findFirst()
                .ifPresent(schedule -> {
                    BigDecimal principal = schedule.getPrincipalPayment() != null ? schedule.getPrincipalPayment() : BigDecimal.ZERO;
                    BigDecimal interest = schedule.getInterestPayment() != null ? schedule.getInterestPayment() : BigDecimal.ZERO;
                    BigDecimal base = principal.add(interest);

                    // Формула: (база * ставка пени / 100) / 30 (в день)
                    BigDecimal dailyPenalty = base
                            .multiply(penaltyRate)
                            .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)
                            .divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);

                    accountingService.accruePenality(credit.getContractNumber(), dailyPenalty);

                    System.out.printf("⚠️ Начислена пеня %.2f по кредиту %s (ставка %s%%)%n",
                            dailyPenalty, credit.getContractNumber(), penaltyRate.toPlainString());
                });
    }


}