package com.MicroFinWay.repository;

import com.MicroFinWay.model.CreditBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CreditBalanceHistoryRepository extends JpaRepository<CreditBalanceHistory, Long> {
    List<CreditBalanceHistory> findByContractNumberOrderByDateDesc(String contractNumber);
    List<CreditBalanceHistory> findByDate(LocalDate date);
}