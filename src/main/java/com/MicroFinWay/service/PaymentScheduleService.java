package com.MicroFinWay.service;

import com.MicroFinWay.dto.PaymentScheduleDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.PaymentSchedule;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final CreditRepository creditRepository;

    public List<PaymentScheduleDTO> generateSchedule(String contractNumber, int gracePeriodMonths, boolean evenInterest) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit not found with contract number " + contractNumber));

        int term = credit.getLoanTerm();
        BigDecimal totalAmount = credit.getAmount();
        BigDecimal interestRate = credit.getInterestRate();

        LocalDate issueDate = credit.getContractDate();
        int issueDay = issueDate.getDayOfMonth();
        int paymentDay = (issueDay >= 25) ? 25 : Math.max(issueDay - 1, 1);
        LocalDate firstPaymentDate = issueDate.plusMonths(1).withDayOfMonth(paymentDay);
        if (paymentDay > firstPaymentDate.lengthOfMonth()) {
            firstPaymentDate = firstPaymentDate.withDayOfMonth(firstPaymentDate.lengthOfMonth());
        }

        List<PaymentSchedule> schedule = new ArrayList<>();
        List<BigDecimal> calculatedInterests = new ArrayList<>(); // Сохраним рассчитанные проценты

        BigDecimal remainingPrincipal = totalAmount;
        BigDecimal monthlyPrincipal = totalAmount.divide(BigDecimal.valueOf(term - gracePeriodMonths), 2, RoundingMode.HALF_UP);

        for (int month = 0; month < term; month++) {
            LocalDate dueDate = firstPaymentDate.plusMonths(month);
            long daysBetween = (month == 0)
                    ? ChronoUnit.DAYS.between(issueDate, dueDate)
                    : ChronoUnit.DAYS.between(dueDate.minusMonths(1), dueDate);

            BigDecimal interestPayment;
            if (month < gracePeriodMonths) {
                interestPayment = totalAmount.multiply(interestRate)
                        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(daysBetween)).setScale(2, RoundingMode.HALF_UP);
            } else {
                BigDecimal principalPayment = monthlyPrincipal.min(remainingPrincipal);
                remainingPrincipal = remainingPrincipal.subtract(principalPayment);
                interestPayment = remainingPrincipal.add(principalPayment).multiply(interestRate)
                        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                        .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(daysBetween)).setScale(2, RoundingMode.HALF_UP);
            }
            calculatedInterests.add(interestPayment);
        }

        // Если равномерное распределение процентов
        BigDecimal uniformInterest = null;
        if (evenInterest) {
            BigDecimal totalInterest = calculatedInterests.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            uniformInterest = totalInterest.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        }

        // Создаем график
        remainingPrincipal = totalAmount;
        for (int month = 0; month < term; month++) {
            LocalDate dueDate = firstPaymentDate.plusMonths(month);
            long daysBetween = (month == 0)
                    ? ChronoUnit.DAYS.between(issueDate, dueDate)
                    : ChronoUnit.DAYS.between(dueDate.minusMonths(1), dueDate);

            BigDecimal principalPayment = BigDecimal.ZERO;
            BigDecimal interestPayment;

            if (month < gracePeriodMonths) {
                interestPayment = evenInterest ? uniformInterest : calculatedInterests.get(month);
            } else {
                principalPayment = monthlyPrincipal.min(remainingPrincipal);
                remainingPrincipal = remainingPrincipal.subtract(principalPayment);
                interestPayment = evenInterest ? uniformInterest : calculatedInterests.get(month);
            }

            PaymentSchedule ps = new PaymentSchedule();
            ps.setCredit(credit);
            ps.setPaymentMonth(month + 1);
            ps.setDueDate(dueDate);
            ps.setPrincipalPayment(principalPayment);
            ps.setInterestPayment(interestPayment);
            ps.setRemainingBalance(remainingPrincipal);
            ps.setOutstandingAmount(principalPayment.add(interestPayment));
            ps.setGracePeriod(month < gracePeriodMonths ? principalPayment : BigDecimal.ZERO);
            ps.setPaymentStatus(0);

            schedule.add(ps);
            System.out.println("Месяц " + (month + 1) + ": дата " + dueDate + ", основной долг " + principalPayment + ", проценты " + interestPayment);
        }

        List<PaymentSchedule> savedList = paymentScheduleRepository.saveAll(schedule);
        List<PaymentScheduleDTO> resultList = new ArrayList<>();
        for (PaymentSchedule ps : savedList) {
            resultList.add(toDTO(ps));
        }

        return resultList;
    }



    public List<PaymentScheduleDTO> getSchedule(String contractNumber) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit not found with contract number " + contractNumber));
        List<PaymentSchedule> list = paymentScheduleRepository.findByCredit(credit);
        return list.stream().map(this::toDTO).toList();
    }

    private PaymentScheduleDTO toDTO(PaymentSchedule ps) {
        return new PaymentScheduleDTO(
                ps.getId(),
                ps.getDueDate(),
                ps.getPaymentDate(),
                ps.getPrincipalPayment(),
                ps.getInterestPayment(),
                ps.getRemainingBalance(),
                ps.getPaymentMonth(),
                ps.getOutstandingAmount(),
                ps.getGracePeriod(),
                ps.getPaymentStatus(),
                ps.getCredit().getContractNumber()
        );
    }
}

