package com.MicroFinWay.controller;

import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.service.AccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounting")
@RequiredArgsConstructor
public class AccountingController {

    private final AccountingService accountingService;

    @GetMapping
    public List<Accounting> getAllEntries() {
        return accountingService.getAll();
    }

    @GetMapping("/contract/{contractNumber}")
    public List<Accounting> getByContract(@PathVariable String contractNumber) {
        return accountingService.getByContract(contractNumber);
    }

    @GetMapping("/approved")
    public List<Accounting> getApproved() {
        return accountingService.getApproved();
    }

    @PostMapping
    public Accounting createEntry(@RequestBody Accounting accounting) {
        return accountingService.save(accounting);
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable Long id) {
        accountingService.delete(id);
    }

    @GetMapping("/search")
    public List<Accounting> search(@RequestParam String query) {
        return accountingService.search(query);
    }

    @GetMapping("/search-advanced")
    public List<Accounting> searchAdvanced(
            @RequestParam(required = false) String debit,
            @RequestParam(required = false) String credit,
            @RequestParam(required = false) String contract,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false, defaultValue = "false") boolean includePrev
    ) {
        return accountingService.advancedSearch(debit, credit, contract, from, to, includePrev);
    }
}