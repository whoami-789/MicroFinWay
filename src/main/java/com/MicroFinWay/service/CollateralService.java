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
    private final AccountNumberGenerator accountNumberGenerator;

    public CollateralDTO createCollateral(CollateralDTO collateralDTO, String contractNumber) {
        // Находим кредит по номеру договора
        Credit credit = creditRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new IllegalArgumentException("Credit not found with contract number " + contractNumber));

        // Находим категорию
        CollateralCategory category = collateralCategoryRepository.findById(collateralDTO.getCollateralCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id " + collateralDTO.getCollateralCategory().getId()));

        // Берём существующий счёт кредита
        String loanAccountNumber = credit.getAccountLoanMain();
        if (loanAccountNumber == null || loanAccountNumber.length() < 20) {
            throw new IllegalArgumentException("Invalid loan account number format");
        }

        // Хвост счёта без префикса и контрольного ключа (берём с позиции 9)
        String tail = loanAccountNumber.substring(9);

        // Новый префикс для залогового счёта
        String newPrefix = "94502000";
        String baseString = newPrefix + tail;

        // Генерация контрольного ключа
        String controlKey = calculateControlKey(baseString);

        // Формируем новый счёт залога
        String collateralAccount = newPrefix + controlKey + tail;

        // Создаём Collateral из DTO
        Collateral collateral = new Collateral();
        collateral.setName(collateralDTO.getName());
        collateral.setCategory(category);
        collateral.setValue(collateralDTO.getValue());
        collateral.setDescription(collateralDTO.getDescription());
        collateral.setTakenFromClient(LocalDate.now());
        collateral.setCredit(credit);

        // Сохраняем залог
        Collateral saved = collateralRepository.save(collateral);

        // Обновляем поле account_collateral в кредите
        credit.setAccountCollateral(collateralAccount);
        creditRepository.save(credit);

        System.out.println(collateralAccount);
        // Возвращаем DTO
        return toCollateralDTO(saved, credit.getContractNumber());
    }


    private String calculateControlKey(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return String.valueOf(sum % 10);
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
