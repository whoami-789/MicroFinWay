package com.MicroFinWay.controller;

import com.MicroFinWay.dto.PaymentInfoDTO;
import com.MicroFinWay.dto.request.PaymentRequestDTO;
import com.MicroFinWay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Получение данных для формы оплаты
    @GetMapping("/credit-info/{contractNumber}")
    public ResponseEntity<PaymentInfoDTO> getCreditInfo(@PathVariable String contractNumber) {
        return ResponseEntity.ok(paymentService.getCreditInfo(contractNumber));
    }

    // Проведение оплаты
    @PostMapping("/process/{contractNumber}")
    public ResponseEntity<String> processPayment(
            @PathVariable String contractNumber,
            @RequestBody PaymentRequestDTO dto
    ) {
        paymentService.processPayment(contractNumber, dto);
        return ResponseEntity.ok("Платёж успешно проведён");
    }
}