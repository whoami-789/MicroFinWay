package com.MicroFinWay.service;

import com.MicroFinWay.model.AccountType;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.repository.AccountTypeRepository;
import com.MicroFinWay.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Генератор лицевых счетов в соответствии со стандартом CMMSSVVVKHNNN
 */
@Service
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private final AccountTypeRepository accountTypeRepository;
    private final CreditRepository creditRepository;

    /**
     * Генерация номера счёта:
     * 5 цифр CMMSS (бухг. счёт), 3 цифры валюты, 1 контрольная, 8 цифр кода клиента, 3 цифры номера счёта
     */
    public String generateAccountNumber(String templateCode, String clientCode, String creditNumber) {
//        AccountType accountType = accountTypeRepository.findByTemplateCode(templateCode)
//                .orElseThrow(() -> new IllegalArgumentException("Account type not found for: " + templateCode));

        // 5 цифр
        String vvv = "000";  // 3 цифры

        // Убедимся, что clientCode ровно 8 цифр
        if (clientCode.length() != 8) {
            throw new IllegalArgumentException("Client code must be exactly 8 digits");
        }


        // Генерация порядкового номера от 1 до 999

        // Собираем строку без контрольного ключа
        String baseString = templateCode + vvv + clientCode + creditNumber;

        // Рассчитываем контрольный ключ (K)
        String controlKey = calculateControlKey(baseString);

        // Финальный счёт: 5 + 3 + 1 + 8 + 3 = 20 цифр
        return templateCode + vvv + controlKey + clientCode + creditNumber;
    }

    /**
     * Контрольный ключ: сумма всех цифр baseString % 10
     */
    private String calculateControlKey(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
        return String.valueOf(sum % 10);
    }
}
