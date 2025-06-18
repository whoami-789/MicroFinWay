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
        String clientCode = "99006480";

        // Ожидаемое значение baseString = "124010860990006480001"
        // Контрольный ключ = сумма цифр % 10
        String expectedAccountNumber = "12401000" + "5" + "99006480" + "02"; // Пример (контрольный ключ надо подсчитать)

        String result = accountNumberGenerator.generateAccountNumber(templateCode, clientCode, "002");

        System.out.println("Generated Account Number: " + result);
        assertNotNull(result);
        assertTrue(result.startsWith("12401000")); // Проверяем начало
        assertTrue(result.endsWith("99006480002")); // Проверяем конец
        assertEquals(20, result.length()); // Проверяем длину (6 + 3 + 1 + 9 + 3)
    }
}
