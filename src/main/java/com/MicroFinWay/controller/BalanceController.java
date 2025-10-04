package com.MicroFinWay.controller;

import com.MicroFinWay.model.CreditBalance;
import com.MicroFinWay.model.CreditBalanceHistory;
import com.MicroFinWay.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{contractNumber}")
    public ResponseEntity<List<CreditBalance>> getBalances(@PathVariable String contractNumber) {
        return ResponseEntity.ok(balanceService.getBalancesByContract(contractNumber));
    }

    @GetMapping("/{contractNumber}/history")
    public ResponseEntity<List<CreditBalanceHistory>> getHistory(@PathVariable String contractNumber) {
        return ResponseEntity.ok(balanceService.getHistoryByContract(contractNumber));
    }

    @PostMapping("/snapshot")
    public ResponseEntity<String> snapshot(@RequestParam LocalDate date) {
        balanceService.snapshotDailyBalances(date);
        return ResponseEntity.ok("Срез балансов на " + date + " сохранён");
    }
}