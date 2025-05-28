package com.MicroFinWay.controller;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.dto.CreditDetailsDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

