package com.MicroFinWay.repository;

import com.MicroFinWay.search.CreditIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CreditSearchRepository extends ElasticsearchRepository<CreditIndex, String> {

    // поиск по номеру договора (начинается с префикса)
    List<CreditIndex> findByContractNumberStartingWith(String prefix);

    // поиск по имени клиента
    List<CreditIndex> findByClientNameContainingIgnoreCase(String name);
}