package com.MicroFinWay.repository;

import com.MicroFinWay.model.Accounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    List<Accounting> findByContractNumber(String contractNumber);


    @Query("select coalesce(sum(a.amount), 0) from Accounting a where a.debitAccount = :account")
    BigDecimal sumDebit(String account);

    @Query("select coalesce(sum(a.amount), 0) from Accounting a where a.creditAccount = :account")
    BigDecimal sumCredit(String account);

    List<Accounting> findByContractNumberOrderByOperationDateDesc(String contractNumber);

    List<Accounting> findByStatusOrderByOperationDateDesc(int status);

    @Query("""
        SELECT a FROM Accounting a
        WHERE LOWER(a.debitAccount) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(a.creditAccount) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(a.contractNumber) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY a.operationDate DESC
    """)
    List<Accounting> searchByDebitOrCredit(@Param("query") String query);

    // 🔹 Найти по номеру договора и части счёта (например, "16405" для пени)
    List<Accounting> findByContractNumberAndDebitAccountContaining(String contractNumber, String debitAccount);

    // 🔹 Альтернатива, если нужно по кредитовому счёту
    List<Accounting> findByContractNumberAndCreditAccountContaining(String contractNumber, String creditAccount);

    @Query("""
        SELECT a.debitAccount, SUM(a.amount)
        FROM Accounting a
        WHERE a.operationDate BETWEEN :from AND :to
        GROUP BY a.debitAccount
    """)
    List<Object[]> sumDebitByPeriod(LocalDate from, LocalDate to);

    @Query("""
        SELECT a.creditAccount, SUM(a.amount)
        FROM Accounting a
        WHERE a.operationDate BETWEEN :from AND :to
        GROUP BY a.creditAccount
    """)
    List<Object[]> sumCreditByPeriod(LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Accounting a WHERE a.debitAccount = :code AND a.operationDate = :date")
    BigDecimal sumDebit(@Param("code") String code, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Accounting a WHERE a.creditAccount = :code AND a.operationDate = :date")
    BigDecimal sumCredit(@Param("code") String code, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Accounting a WHERE a.debitAccount = :code AND a.operationDate < :date")
    BigDecimal sumDebitBefore(@Param("code") String code, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Accounting a WHERE a.creditAccount = :code AND a.operationDate < :date")
    BigDecimal sumCreditBefore(@Param("code") String code, @Param("date") LocalDate date);

}
