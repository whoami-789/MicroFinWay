package com.MicroFinWay.controller;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.CreditBalance;
import com.MicroFinWay.model.CreditBalanceHistory;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.service.BalanceService;
import lombok.RequiredArgsConstructor;
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
    private final CreditRepository creditRepository;

    /**
     * 📊 Получить текущее сальдо по конкретному кредиту
     */
    @GetMapping("/current/{contractNumber}")
    public ResponseEntity<List<CreditBalance>> getCurrentBalances(
            @PathVariable String contractNumber
    ) {
        return ResponseEntity.ok(balanceService.getBalancesByContract(contractNumber));
    }

    /**
     * 📈 Получить историю сальдо по кредиту
     */
    @GetMapping("/history/{contractNumber}")
    public ResponseEntity<List<CreditBalanceHistory>> getHistory(
            @PathVariable String contractNumber
    ) {
        return ResponseEntity.ok(balanceService.getHistoryByContract(contractNumber));
    }

    /**
     * 🧾 Ручное обновление баланса по счёту кредита
     * (Например, если бухгалтер хочет внести корректировку)
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateBalanceManually(
            @RequestParam String contractNumber,
            @RequestParam String accountCode,
            @RequestParam BigDecimal amount,
            @RequestParam boolean isDebit
    ) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElse(null);

        if (credit == null) {
            return ResponseEntity.badRequest().body("Кредит с номером " + contractNumber + " не найден.");
        }

        balanceService.updateBalance(credit, accountCode, amount, isDebit);
        return ResponseEntity.ok("Баланс обновлён успешно для счёта " + accountCode);
    }

    /**
     * 📅 Сохранить снимок всех остатков (при закрытии операционного дня)
     */
    @PostMapping("/snapshot")
    public ResponseEntity<String> snapshotBalances(@RequestParam(required = false) String date) {
        LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        balanceService.snapshotDailyBalances(targetDate);
        return ResponseEntity.ok("Снимок остатков за " + targetDate + " успешно сохранён.");
    }

    /**
     * ⚙️ Массовый пересчёт всех балансов (при необходимости восстановления данных)
     */
    @PostMapping("/rebuild")
    public ResponseEntity<String> rebuildAllBalances() {
        List<Credit> allCredits = creditRepository.findAll();
        balanceService.rebuildAllBalances(allCredits);
        return ResponseEntity.ok("Все балансы пересчитаны успешно.");
    }
}