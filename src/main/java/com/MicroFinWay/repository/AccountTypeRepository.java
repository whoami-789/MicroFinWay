package com.MicroFinWay.repository;

import com.MicroFinWay.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByCmmss(String cmmss);
    Optional<AccountType> findByTemplateCode(String accountPurpose);
}
