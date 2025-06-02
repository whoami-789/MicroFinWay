package com.MicroFinWay.controller;

import com.MicroFinWay.dto.PaymentScheduleDTO;
import com.MicroFinWay.service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService service;

    @GetMapping("/generate")
    public List<PaymentScheduleDTO> generateSchedule(
            @RequestParam String contractNumber,
            @RequestParam int gracePeriodMonths,
            @RequestParam boolean evenInterest
    ) {
        return service.generateSchedule(contractNumber, gracePeriodMonths, evenInterest);
    }

    @GetMapping("/{contractNumber}")
    public List<PaymentScheduleDTO> getSchedule(@PathVariable String contractNumber) {
        return service.getSchedule(contractNumber);
    }
}

