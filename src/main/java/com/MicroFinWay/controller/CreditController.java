package com.MicroFinWay.controller;

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
    public Credit createCredit(@RequestBody Credit credit, @RequestParam Long userId) {
        return creditService.createCredit(credit, userId);
    }

//    @GetMapping("/{id}")
//    public Credit getCredit(@PathVariable Long id) {
//        return creditService.getCredit(id);
//    }
}
