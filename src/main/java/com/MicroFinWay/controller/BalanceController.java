package com.MicroFinWay.controller;

import com.MicroFinWay.dto.BalanceDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.CreditBalance;
import com.MicroFinWay.model.CreditBalanceHistory;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для управления сальдо по кредитам.
 */
@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{date}")
    public List<BalanceDTO> getBalance(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return balanceService.calculateBalance(date);
    }
}