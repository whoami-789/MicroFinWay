package com.MicroFinWay.repository;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    List<PaymentSchedule> findByCredit(Credit credit);
    // 🔹 Все незакрытые (не оплаченные) платежи по кредиту
    List<PaymentSchedule> findByCreditIdAndPaymentStatus(Long creditId, Integer paymentStatus);

    // 🔹 Все платежи по кредиту
    List<PaymentSchedule> findByCreditIdOrderByPaymentDateAsc(Long creditId);
}
