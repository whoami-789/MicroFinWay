package com.MicroFinWay.repository;

import com.MicroFinWay.model.CollateralCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollateralCategoryRepository extends JpaRepository<CollateralCategory, Long> {
    // Можно добавить поиск по коду категории
}
