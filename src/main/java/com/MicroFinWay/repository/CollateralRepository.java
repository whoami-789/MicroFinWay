package com.MicroFinWay.repository;

import com.MicroFinWay.model.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Long> {
    Optional<Collateral> findByContractNumber(String contractNumber);

    // Можно добавить поиск по кредиту, категории и т.д.
}
