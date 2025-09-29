package com.MicroFinWay.controller;

import com.MicroFinWay.dto.*;
import com.MicroFinWay.service.CashOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cash-orders")
@RequiredArgsConstructor
public class CashOrderController {

    private final CashOrderService cashOrderService;

    // === 1. Поиск договоров в эластике ===
    @GetMapping("/contracts")
    public List<ContractSearchDTO> searchContracts(@RequestParam String prefix) {
        return cashOrderService.searchContracts(prefix);
    }

    // === 2. Поиск счетов по договору и префиксу ===
    @GetMapping("/accounts")
    public List<AccountOptionDTO> searchAccounts(
            @RequestParam String contractNumber,
            @RequestParam String prefix
    ) {
        return cashOrderService.searchAccounts(contractNumber, prefix);
    }

    // === 3. Preview (остатки до/после операции) ===
    @PostMapping("/preview")
    public List<AccountBalanceDTO> preview(@RequestBody CashOrderPreviewRequest request) {
        return cashOrderService.previewBalances(
                request.getContractNumber(),
                request.getDebit(),
                request.getCredit(),
                request.getAmount()
        );
    }

    // === 4. Сохранение кассового ордера ===
    @PostMapping
    public Long save(@RequestBody CashOrderDTO dto) {
        return cashOrderService.createCashOrder(dto);
    }
}