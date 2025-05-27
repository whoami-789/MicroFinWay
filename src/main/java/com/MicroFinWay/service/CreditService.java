package com.MicroFinWay.service;

import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountTypeRepository accountTypeRepository;

    public Credit createCredit(Credit credit, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

        String clientCode = String.format("%09d", user.getId());
        String currencyCode = "000";  // UZS или подставить из кредитных данных

        // Находим количество уже существующих кредитов у данного клиента
        long existingCreditCount = creditRepository.countByUserId(user.getId());
        String sequenceNumber = String.format("%03d", existingCreditCount + 1);  // Пример: 001, 002, 003...

        // Генерация всех счетов для кредита
        credit.setAccountLoanMain(accountNumberGenerator.generateAccountNumber("CREDIT_BODY", currencyCode, clientCode, sequenceNumber));
        credit.setAccountInterestGeneral(accountNumberGenerator.generateAccountNumber("INTEREST", currencyCode, clientCode, sequenceNumber));
        credit.setAccountOverdueLoan(accountNumberGenerator.generateAccountNumber("OVERDUE", currencyCode, clientCode, sequenceNumber));
        credit.setAccountOverdueInterest(accountNumberGenerator.generateAccountNumber("OVERDUE_INTEREST", currencyCode, clientCode, sequenceNumber));
        credit.setAccountCollateral(accountNumberGenerator.generateAccountNumber("COLLATERAL", currencyCode, clientCode, sequenceNumber));
        credit.setAccountReserve(accountNumberGenerator.generateAccountNumber("RESERVE", currencyCode, clientCode, sequenceNumber));
        credit.setAccountCreditWriteoff(accountNumberGenerator.generateAccountNumber("WRITE_OFF", currencyCode, clientCode, sequenceNumber));
        credit.setAccountPenaltyAdditional(accountNumberGenerator.generateAccountNumber("PENALTY", currencyCode, clientCode, sequenceNumber));
        credit.setAccountIncome(accountNumberGenerator.generateAccountNumber("INCOME", currencyCode, clientCode, sequenceNumber));

        // Другие поля кредита
        credit.setUser(user);
        credit.setPaidAmount(BigDecimal.ZERO);
        credit.setStatus(Credit.CreditStatus.ACTIVE);
        credit.setLastUpdatedTime(java.time.LocalDateTime.now());

        return creditRepository.save(credit);
    }

}
