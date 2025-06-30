package com.MicroFinWay.repository;

import com.MicroFinWay.model.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    List<Accounting> findByContractNumber(String contractNumber);

}
