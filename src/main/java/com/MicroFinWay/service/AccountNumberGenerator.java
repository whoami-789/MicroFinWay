package com.MicroFinWay.service;

import com.MicroFinWay.model.AccountType;
import com.MicroFinWay.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



/**
 * Service class responsible for generating account numbers based on the CMMSS code,
 * currency, client code, and sequence number. It interacts with the {@code AccountTypeRepository}
 * to retrieve account type details and uses a custom algorithm to calculate the control key
 * as part of the account number generation process.
 */
@Service
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private final AccountTypeRepository accountTypeRepository;

    /**
     * Генерация номера счёта на основе кода CMMSS и других данных
     * @param templateCode назначение (например, "CREDIT_BODY")
     * @param currencyCode код валюты (например, "000")
     * @param clientCode уникальный код клиента (например, "990006480")
     * @param sequenceNumber порядковый номер (например, "01")
     * @return готовый номер счёта
     */
    public String generateAccountNumber(
            String templateCode,
            String currencyCode,
            String clientCode,
            String sequenceNumber
    ) {
        // Находим AccountType по templateCode
        AccountType accountType = accountTypeRepository.findByTemplateCode(templateCode)
                .orElseThrow(() -> new IllegalArgumentException("Account type not found for purpose: " + templateCode));

        String cmmss = accountType.getCmmss();

        // Формируем строку для вычисления контрольного ключа (без форматирования)
        String baseString = cmmss
                + String.format("%03d", Integer.parseInt(currencyCode))
                + clientCode
                + String.format("%03d", Integer.parseInt(sequenceNumber));

        // Вычисляем контрольный ключ
        String controlKey = calculateControlKey(baseString);

        // Формируем финальный номер счёта
        return cmmss
                + String.format("%03d", Integer.parseInt(currencyCode))
                + controlKey
                + String.format("%08d", Long.parseLong(clientCode))  // делаем clientCode фиксированным на 9 цифр
                + String.format("%03d", Integer.parseInt(sequenceNumber)); // делаем sequenceNumber фиксированным на 3 цифры
    }

    /**
     * Алгоритм вычисления контрольного ключа (сумма цифр % 10)
     */
    private String calculateControlKey(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return String.valueOf(sum % 10);
    }
}

