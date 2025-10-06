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
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollateralService {

    private final CollateralRepository collateralRepository;
    private final CollateralCategoryRepository collateralCategoryRepository;
    private final CreditRepository creditRepository;
    private final AccountingService accountingService;

    public CollateralDTO createCollateral(CollateralDTO dto, String contractNumber) {
        // 1. Находим кредит по номеру договора
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Кредит не найден по номеру договора " + contractNumber));

        // 2. Определяем категорию — ищем по коду, а не по объекту
        CollateralCategory category = collateralCategoryRepository.findByCode(dto.getCollateralCategoryCode());
        // 3. Получаем счёт залога
        String collateralAccount = credit.getCreditAccount().getAccount94502();
        if (collateralAccount == null || collateralAccount.length() < 10) {
            throw new IllegalArgumentException("Счёт залога не найден или некорректен для договора " + contractNumber);
        }

        // 4. Создаём сущность Collateral
        Collateral collateral = new Collateral();
        collateral.setName(dto.getName() != null ? dto.getName() : category.getName());
        collateral.setCategory(category);
        collateral.setValue(dto.getValue());
        collateral.setDescription(dto.getDescription());
        collateral.setTakenFromClient(dto.getTakenFromClient());
        collateral.setGivenToBank(dto.getGivenToBank());
        collateral.setTakenFromBank(dto.getTakenFromBank());
        collateral.setGivenToClient(dto.getGivenToClient());
        collateral.setEngineNumber(dto.getEngineNumber());
        collateral.setCarBodyNumber(dto.getCarBodyNumber());
        collateral.setCarYear(dto.getCarYear());
        collateral.setCarStateNumber(dto.getCarStateNumber());
        collateral.setCarModel(dto.getCarModel());
        collateral.setCarChassiNumber(dto.getCarChassiNumber());
        collateral.setCarColor(dto.getCarColor());
        collateral.setCarPassport(dto.getCarPassport());
        collateral.setCarVinNumber(dto.getCarVinNumber());
        collateral.setContractNumber(contractNumber);
        collateral.setCredit(credit);

        // 5. Сохраняем
        Collateral saved = collateralRepository.save(collateral);

        // 6. Регистрируем проводку по залогу
        accountingService.registerCollateral(contractNumber, collateral.getValue());

        // 7. Возвращаем DTO
        return toCollateralDTO(saved, contractNumber);
    }


    private CollateralDTO toCollateralDTO(Collateral collateral, String contractNumber) {
        CollateralDTO dto = new CollateralDTO();
        dto.setId(collateral.getId());
        dto.setName(collateral.getName());
        dto.setCollateralCategoryCode(collateral.getCategory().getCode());
        dto.setValue(collateral.getValue());
        dto.setDescription(collateral.getDescription());
        dto.setTakenFromClient(collateral.getTakenFromClient());
        dto.setCreditId(contractNumber);
        return dto;
    }

    public String getCollateralAccountByContract(String contractNumber) {
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Кредит не найден: " + contractNumber));

        if (credit.getCreditAccount() == null) {
            throw new IllegalStateException("Для данного кредита не найден счёт");
        }

        String account = credit.getCreditAccount().getAccount94502(); // ← счёт залога

        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Счёт залога отсутствует для этого кредита");
        }

        return account;
    }

    public Collateral save(Collateral collateral) {
        return collateralRepository.save(collateral);
    }
}
