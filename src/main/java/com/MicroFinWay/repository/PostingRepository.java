package com.MicroFinWay.repository;

import com.MicroFinWay.model.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByOrderType(Posting.OrderType orderType);

    List<Posting> findByDebitAndCredit(String debit, String credit);

    List<Posting> findByDescriptionContainingIgnoreCase(String keyword);
}