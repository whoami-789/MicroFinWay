package com.MicroFinWay.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Data
@Document(indexName = "credits") // имя индекса в эластике
public class CreditIndex {
    @Id
    private String id;

    private String contractNumber;
    private String clientName;
    private BigDecimal amount;

    public CreditIndex(String contractNumber, String clientName) {
        this.contractNumber = contractNumber;
        this.clientName = clientName;
    }
}