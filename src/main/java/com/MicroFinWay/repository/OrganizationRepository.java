package com.MicroFinWay.repository;

import com.MicroFinWay.model.Organization;
import com.MicroFinWay.service.OrganizationService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);

    Optional<Organization> findTopByOrderByIdAsc(); // берём первую

}
