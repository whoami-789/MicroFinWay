package com.MicroFinWay.controller;

import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.service.ROrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders/ro")
@RequiredArgsConstructor
public class ROrderController {

    private final ROrderService rOrderService;

    @PostMapping("/create")
    public ResponseEntity<Accounting> createROrder(
            @RequestParam String contractNumber,
            @RequestParam BigDecimal amount,
            @RequestParam String contractDate
    ) {
        Accounting accounting = rOrderService.createROrder(
                contractNumber,
                amount,
                LocalDate.parse(contractDate)
        );
        return ResponseEntity.ok(accounting);
    }
}