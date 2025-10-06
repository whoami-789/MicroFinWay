package com.MicroFinWay.repository;

import com.MicroFinWay.model.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Long> {
    Optional<Collateral> findByContractNumber(String contractNumber);

    // 🔹 Поиск по счёту залога (например, 94502...)
    @Query("""
        SELECT c FROM Collateral c
        JOIN c.credit cr
        JOIN cr.creditAccount acc
        WHERE acc.account94502 LIKE %:account%
    """)
    List<Collateral> findByCollateralAccount(@Param("account") String account);
}
