package com.MicroFinWay;

import com.MicroFinWay.dto.CollateralDTO;
import com.MicroFinWay.model.Collateral;
import com.MicroFinWay.model.CollateralCategory;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.CollateralCategoryRepository;
import com.MicroFinWay.repository.CollateralRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.service.CollateralService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollateralServiceTest {

    @Mock
    private CollateralRepository collateralRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CollateralCategoryRepository collateralCategoryRepository;

    @InjectMocks
    private CollateralService collateralService;

    @Test
    void createCollateral_Success() {
        String contractNumber = "99000648-1";

        User user = new User();
        user.setKod("99000648");

        Credit credit = new Credit();
        credit.setId(1L);
        credit.setUser(user);
        credit.setContractNumber(contractNumber);
        credit.setAccountLoanMain("12401000599000648001");

        CollateralCategory category = new CollateralCategory();
        category.setId(1L);
        category.setName("Авто");

        Collateral savedCollateral = new Collateral();
        savedCollateral.setId(10L);
        savedCollateral.setName("Test Collateral");
        savedCollateral.setCategory(category);
        savedCollateral.setCredit(credit);

        CollateralDTO collateralDTO = new CollateralDTO();
        collateralDTO.setName("Test Collateral");
        collateralDTO.setCollateralCategory(category);
        collateralDTO.setValue(new BigDecimal("5000"));

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));
        when(collateralCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(collateralRepository.save(any(Collateral.class))).thenReturn(savedCollateral);

        CollateralDTO result = collateralService.createCollateral(collateralDTO, contractNumber);

        assertNotNull(result);
        assertEquals("Test Collateral", result.getName());
        verify(creditRepository).save(any(Credit.class));
    }

    @Test
    void createCollateral_CreditNotFound() {
        String contractNumber = "INVALID-CONTRACT";

        CollateralDTO collateralDTO = new CollateralDTO();
        collateralDTO.setCollateralCategory(new CollateralCategory(1L, "Test", null, null));
        collateralDTO.setValue(new BigDecimal("5000"));

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> collateralService.createCollateral(collateralDTO, contractNumber),
                "Credit not found with contract number " + contractNumber);
    }

    @Test
    void createCollateral_CategoryNotFound() {
        String contractNumber = "99000648-1";

        User user = new User();
        user.setKod("99000648");

        Credit credit = new Credit();
        credit.setId(1L);
        credit.setUser(user);
        credit.setContractNumber(contractNumber);
        credit.setAccountLoanMain("12401000599000648001");

        CollateralDTO collateralDTO = new CollateralDTO();
        collateralDTO.setCollateralCategory(new CollateralCategory(99L, "NotExists", null, null));
        collateralDTO.setValue(new BigDecimal("5000"));

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));
        when(collateralCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> collateralService.createCollateral(collateralDTO, contractNumber),
                "Category not found with id 99");
    }

    @Test
    void createCollateral_InvalidLoanAccount() {
        String contractNumber = "99000648-1";

        User user = new User();
        user.setKod("99000648");

        Credit credit = new Credit();
        credit.setId(1L);
        credit.setUser(user);
        credit.setContractNumber(contractNumber);
        credit.setAccountLoanMain("123"); // Некорректный формат

        CollateralCategory category = new CollateralCategory();
        category.setId(1L);
        category.setName("Авто");

        CollateralDTO collateralDTO = new CollateralDTO();
        collateralDTO.setCollateralCategory(category);
        collateralDTO.setValue(new BigDecimal("5000"));

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));
        when(collateralCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(IllegalArgumentException.class,
                () -> collateralService.createCollateral(collateralDTO, contractNumber),
                "Invalid loan account number format");
    }
}
