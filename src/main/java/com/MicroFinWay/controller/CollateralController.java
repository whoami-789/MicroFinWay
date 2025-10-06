package com.MicroFinWay.controller;

import com.MicroFinWay.dto.CollateralDTO;
import com.MicroFinWay.dto.CreditDetailsDTO;
import com.MicroFinWay.model.Collateral;
import com.MicroFinWay.service.CollateralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaterals")
@RequiredArgsConstructor
public class CollateralController {

    private final CollateralService collateralService;

    /**
     * Создание залога для указанного кредита по номеру договора
     * @param contractNumber номер договора
     * @param collateralDTO данные залога
     * @return созданный CollateralDTO
     */
    @PostMapping("/create/{contractNumber}")
    public ResponseEntity<CollateralDTO> createCollateral(
            @PathVariable String contractNumber,
            @RequestBody CollateralDTO collateralDTO) {

        CollateralDTO createdCollateral = collateralService.createCollateral(collateralDTO, contractNumber);
        return ResponseEntity.ok(createdCollateral);
    }

    @GetMapping("/collateral-account/{contractNumber}")
    public ResponseEntity<String> getCollateralAccountByContract(@PathVariable String contractNumber) {
        String account = collateralService.getCollateralAccountByContract(contractNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public Collateral createCollateral(@RequestBody Collateral collateral) {
        return collateralService.save(collateral);
    }
}
