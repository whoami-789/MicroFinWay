package com.MicroFinWay;

import com.MicroFinWay.model.AccountType;
import com.MicroFinWay.repository.AccountTypeRepository;
import com.MicroFinWay.service.AccountNumberGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountNumberGeneratorTest {

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @InjectMocks
    private AccountNumberGenerator accountNumberGenerator;

    @Test
    void generateAccountNumber_Success() {
        // Мокаем возврат AccountType с cmmss
        AccountType mockAccountType = new AccountType();
        mockAccountType.setCmmss("12401");
        when(accountTypeRepository.findByTemplateCode("CREDIT_BODY")).thenReturn(Optional.of(mockAccountType));

        // Входные данные
        String templateCode = "CREDIT_BODY";
        String currencyCode = "000";
        String clientCode = "99000648";
        String sequenceNumber = "1";

        // Ожидаемое значение baseString = "124010860990006480001"
        // Контрольный ключ = сумма цифр % 10
        String expectedAccountNumber = "124010860" + "5" + "990006480" + "01"; // Пример (контрольный ключ надо подсчитать)

        String result = accountNumberGenerator.generateAccountNumber(templateCode, currencyCode, clientCode, sequenceNumber);

        System.out.println("Generated Account Number: " + result);
        assertNotNull(result);
        assertTrue(result.startsWith("12401000")); // Проверяем начало
        assertTrue(result.endsWith("99000648001")); // Проверяем конец
        assertEquals(20, result.length()); // Проверяем длину (6 + 3 + 1 + 9 + 3)
    }

    @Test
    void generateAccountNumber_AccountTypeNotFound() {
        when(accountTypeRepository.findByTemplateCode("INVALID_CODE")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                accountNumberGenerator.generateAccountNumber("INVALID_CODE", "000", "990006480", "01"));

        assertEquals("Account type not found for purpose: INVALID_CODE", exception.getMessage());
    }
}
