package com.MicroFinWay.controller;

import com.MicroFinWay.repository.ClientSearchRepository;
import com.MicroFinWay.search.ClientIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search/clients")
@RequiredArgsConstructor
public class ClientSearchController {

    private final ClientSearchRepository clientSearchRepository;

    @GetMapping
    public List<ClientIndex> searchClients(@RequestParam String q) {
        // ищем по ФИО, паспорту и телефону
        var result = clientSearchRepository.findByFullNameContainingIgnoreCase(q);
        result.addAll(clientSearchRepository.findByPassportContaining(q));
        result.addAll(clientSearchRepository.findByPhoneContaining(q));
        return result;
    }
}
