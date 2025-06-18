package com.MicroFinWay.repository;

import com.MicroFinWay.model.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    List<Accounting> findByContractNumber(String contractNumber);
}
