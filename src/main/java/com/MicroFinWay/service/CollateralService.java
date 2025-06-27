package com.MicroFinWay.service;

import com.MicroFinWay.dto.CollateralDTO;
import com.MicroFinWay.model.Collateral;
import com.MicroFinWay.model.CollateralCategory;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.repository.CollateralCategoryRepository;
import com.MicroFinWay.repository.CollateralRepository;
import com.MicroFinWay.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CollateralService {

    private final CollateralRepository collateralRepository;
    private final CollateralCategoryRepository collateralCategoryRepository;
    private final CreditRepository creditRepository;
    private final AccountingService accountingService;

    public CollateralDTO createCollateral(CollateralDTO collateralDTO, String contractNumber) {
        // 1. Находим кредит по номеру договора
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit not found with contract number " + contractNumber));

        // 2. Находим категорию
        CollateralCategory category = collateralCategoryRepository.findById(collateralDTO.getCollateralCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id " + collateralDTO.getCollateralCategory().getId()));

        // 3. Берём счёт залога из связанной таблицы CreditAccount
        String collateralAccount = credit.getCreditAccount().getAccount94502();

        if (collateralAccount == null || collateralAccount.length() < 20) {
            throw new IllegalArgumentException("Collateral account not found or invalid format for contract " + contractNumber);
        }

        // 4. Создаём Collateral
        Collateral collateral = new Collateral();
        collateral.setName(collateralDTO.getName());
        collateral.setCategory(category);
        collateral.setValue(collateralDTO.getValue());
        collateral.setDescription(collateralDTO.getDescription());
        collateral.setTakenFromClient(LocalDate.now());
        collateral.setCredit(credit);

        // 5. Сохраняем
        Collateral saved = collateralRepository.save(collateral);
        accountingService.registerCollateral(contractNumber, collateral.getValue());

        // 6. Возврат DTO
        return toCollateralDTO(saved, contractNumber);
    }


    private CollateralDTO toCollateralDTO(Collateral collateral, String contractNumber) {
        CollateralDTO dto = new CollateralDTO();
        dto.setId(collateral.getId());
        dto.setName(collateral.getName());
        dto.setCollateralCategory(collateral.getCategory());
        dto.setValue(collateral.getValue());
        dto.setDescription(collateral.getDescription());
        dto.setTakenFromClient(collateral.getTakenFromClient());
        dto.setCreditId(contractNumber);
        return dto;
    }
}
