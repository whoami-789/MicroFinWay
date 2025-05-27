package com.MicroFinWay.repository;

import com.MicroFinWay.model.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Long> {
    // Можно добавить поиск по кредиту, категории и т.д.
}
