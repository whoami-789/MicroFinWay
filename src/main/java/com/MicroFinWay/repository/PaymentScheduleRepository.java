package com.MicroFinWay.repository;

import com.MicroFinWay.model.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    // Можно добавить поиск по кредиту и датам
}
