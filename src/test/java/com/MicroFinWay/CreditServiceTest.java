package com.MicroFinWay;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.AccountTypeRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.service.AccountNumberGenerator;
import com.MicroFinWay.service.CreditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountNumberGenerator accountNumberGenerator;
    @Mock
    private AccountTypeRepository accountTypeRepository;

    @InjectMocks
    private CreditService creditService;

    @Test
    void createCredit_Success() {
        // Mock входных данных
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setCode("99000001");
        creditDTO.setAmount(new BigDecimal("10000"));
        creditDTO.setCurrencyCode("USD");
        creditDTO.setLoanTerm(12);
        creditDTO.setInterestRate(new BigDecimal("12.5"));

        User user = new User();
        user.setId(1L);
        user.setKod("99000001");

        when(userRepository.findByKod("99000001")).thenReturn(Optional.of(user));
        when(creditRepository.countByUserKod("99000001")).thenReturn(0L);
        when(accountNumberGenerator.generateAccountNumber(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("ACC123");

        Credit savedCredit = new Credit();
        savedCredit.setId(1L);
        savedCredit.setContractNumber("99000001-1");
        savedCredit.setAmount(new BigDecimal("10000"));
        savedCredit.setCurrencyCode("USD");
        savedCredit.setLoanTerm(12);
        savedCredit.setInterestRate(new BigDecimal("12.5"));
        savedCredit.setStatus(Credit.CreditStatus.ACTIVE);
        savedCredit.setAccountLoanMain("ACC123");

        when(creditRepository.save(any(Credit.class))).thenReturn(savedCredit);

        // Вызов
        CreditDTO result = creditService.createCredit(creditDTO);

        // Проверки
        assertNotNull(result);
        assertEquals("99000001-1", result.getContractNumber());
        assertEquals(new BigDecimal("10000"), result.getAmount());
        assertEquals("USD", result.getCurrencyCode());
        assertEquals(Credit.CreditStatus.ACTIVE, result.getStatus());
        assertEquals("ACC123", result.getAccountLoanMain());

        // Проверяем вызовы
        verify(userRepository).findByKod("99000001");
        verify(creditRepository).save(any(Credit.class));
    }

    @Test
    void createCredit_UserNotFound() {
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setCode("INVALID");

        when(userRepository.findByKod("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> creditService.createCredit(creditDTO));

        verify(userRepository).findByKod("INVALID");
        verifyNoInteractions(creditRepository);
    }
}

