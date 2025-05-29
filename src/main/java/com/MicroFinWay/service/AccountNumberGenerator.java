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
     * @param accountTypeCode код CMMSS (например, "12401")
     * @param currencyCode код валюты (например, "860" для UZS)
     * @param clientCode уникальный код клиента (например, "990006480")
     * @param sequenceNumber порядковый номер (например, "001")
     * @return готовый номер счёта
     */
    public String generateAccountNumber(
            String template_code,    // Назначение счета: "CREDIT_BODY", "INTEREST" и др.
            String currencyCode,      // VVV
            String clientCode,        // XXXXXXXXX
            String sequenceNumber     // NNN
    ) {
        // Находим шаблон по назначению
        AccountType accountType = accountTypeRepository.findByTemplateCode(template_code)
                .orElseThrow(() -> new IllegalArgumentException("Account type not found for purpose: " + template_code));

        String cmmss = accountType.getCmmss();
        String baseString = cmmss + String.format("%03d", Integer.parseInt(currencyCode))
                + String.format("%09d", Long.parseLong(clientCode))
                + String.format("%03d", Integer.parseInt(sequenceNumber));

        String controlKey = calculateControlKey(baseString);

        return cmmss + String.format("%03d", Integer.parseInt(currencyCode))
                + controlKey
                + String.format("%09d", Long.parseLong(clientCode))
                + String.format("%03d", Integer.parseInt(sequenceNumber));
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
