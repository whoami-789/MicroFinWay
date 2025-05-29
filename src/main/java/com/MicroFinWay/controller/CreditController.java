package com.MicroFinWay.controller;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.dto.CreditDetailsDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



/**
 * The CreditController class is a REST controller that provides endpoints for managing credits.
 * It handles HTTP requests related to credit creation and other credit-related operations.
 *
 * Mappings:
 * - Base Path: "/api/credits"
 *
 * Dependencies:
 * - {@link CreditService}: Service layer dependency responsible for credit-related business logic.
 *
 * Endpoints:
 * - POST "/api/credits": Creates a new credit based on the given CreditDTO.
 */
@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @PostMapping
    public CreditDTO createCredit(@RequestBody CreditDTO dto) {
        return creditService.createCredit(dto);
    }

//    @GetMapping("/{id}")
//    public CreditDetailsDTO getCreditDetails(@PathVariable Long id) {
//        return creditService.getCreditDetails(id);
//    }
}

