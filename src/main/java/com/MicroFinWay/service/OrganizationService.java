package com.MicroFinWay.service;

import com.MicroFinWay.model.Organization;
import com.MicroFinWay.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    private static final String DEFAULT_ORG_CODE = "06005";

    public LocalDate getCurrentOperationalDay() {
        return repository.findByCode(DEFAULT_ORG_CODE)
                .map(Organization::getCurrentOperationalDay)
                .orElseThrow(() -> new IllegalStateException("Organization not found"));
    }

    public void setCurrentOperationalDay(LocalDate date) {
        Organization org = repository.findByCode(DEFAULT_ORG_CODE)
                .orElseThrow(() -> new IllegalStateException("Organization not found"));
        org.setCurrentOperationalDay(date);
        repository.save(org);
    }

    public void initializeIfNotExists() {
        repository.findByCode(DEFAULT_ORG_CODE).orElseGet(() -> {
            Organization org = Organization.builder()
                    .code(DEFAULT_ORG_CODE)
                    .name("Main Organization")
                    .currentOperationalDay(LocalDate.now())
                    .build();
            return repository.save(org);
        });
    }

    public void setOperationalDayClosed(boolean closed) {
        Organization org = repository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Организация не найдена"));
        org.setOperationalDayClosed(closed);
        repository.save(org);
    }

    public boolean isOperationalDayClosed() {
        return repository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Организация не найдена")).isOperationalDayClosed();
    }


}
