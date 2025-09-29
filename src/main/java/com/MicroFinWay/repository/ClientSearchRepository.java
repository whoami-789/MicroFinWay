package com.MicroFinWay.repository;

import com.MicroFinWay.search.ClientIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ClientSearchRepository extends ElasticsearchRepository<ClientIndex, String> {
    List<ClientIndex> findByFullNameContainingIgnoreCase(String prefix);
    List<ClientIndex> findByPassportContaining(String prefix);
    List<ClientIndex> findByPhoneContaining(String prefix);
}