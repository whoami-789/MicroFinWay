package com.MicroFinWay.repository;

import com.MicroFinWay.model.Credit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    long countByUserKod(String user_kod);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Credit c SET c.accountCollateral = :account WHERE c.id = :creditId")
//    void updateAccountCollateral(Long creditId, String account);

    Optional<Credit> findByContractNumber(String contractNumber);

    List<Credit> findByStatus(Credit.CreditStatus status);

    List<Credit> findAllByStatus(Credit.CreditStatus status);

    @Query("SELECT c.contractDate, COUNT(c) " +
            "FROM Credit c " +
            "WHERE c.contractDate BETWEEN :start AND :end " +
            "GROUP BY c.contractDate " +
            "ORDER BY c.contractDate ASC")
    List<Object[]> countCreditsByDateRange(@Param("start") LocalDate start,
                                           @Param("end") LocalDate end);
}
