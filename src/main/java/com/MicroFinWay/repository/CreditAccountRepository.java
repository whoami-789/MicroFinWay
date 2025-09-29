package com.MicroFinWay.repository;

import com.MicroFinWay.model.CreditAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditAccountRepository extends JpaRepository<CreditAccount, Long> {
    Optional<CreditAccount> findByCredit_Code(String creditCode);

    Optional<CreditAccount> findByContractNumber(String contractNumber);

}

