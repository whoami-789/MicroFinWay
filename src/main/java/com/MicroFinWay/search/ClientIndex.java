package com.MicroFinWay.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "clients")
public class ClientIndex {
    @Id
    private String id;          // например, kod клиента
    private String fullName;    // ФИО
    private String passport;    // номер паспорта
    private String phone;       // телефон
}