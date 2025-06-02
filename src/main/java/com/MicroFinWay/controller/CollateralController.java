package com.MicroFinWay.controller;

import com.MicroFinWay.dto.CollateralDTO;
import com.MicroFinWay.service.CollateralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
