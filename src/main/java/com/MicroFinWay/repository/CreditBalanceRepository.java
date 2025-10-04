package com.MicroFinWay.repository;

import com.MicroFinWay.model.CreditBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditBalanceRepository extends JpaRepository<CreditBalance, Long> {
    Optional<CreditBalance> findByCredit_IdAndAccountCode(Long creditId, String accountCode);
    List<CreditBalance> findByContractNumber(String contractNumber);
}