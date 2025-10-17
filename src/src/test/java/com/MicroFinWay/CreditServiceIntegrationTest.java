package com.MicroFinWay;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.model.enums.UserType;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditAccountRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static com.MicroFinWay.model.enums.UserType.INDIVIDUAL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditServiceIntegrationTest {

    @Autowired private CreditService creditService;
    @Autowired private CreditRepository creditRepository;
    @Autowired private CreditAccountRepository creditAccountRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AccountingRepository accountingRepository;

    @BeforeEach
    void setup() {
        accountingRepository.deleteAll();
        creditAccountRepository.deleteAll();
        creditRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setKod("99009999");
        user.setFullName("Интеграционный Пользователь");
        user.setUserType(INDIVIDUAL);
        userRepository.save(user);
    }

    @Test
    void testFullCreditCreationFlow() {
        // Arrange
        CreditDTO dto = new CreditDTO();
        dto.setCode("99009999");
        dto.setAmount(new BigDecimal("1000000"));
        dto.setLoanTerm(12);
        dto.setInterestRate(new BigDecimal("23.5"));

        // Act
        CreditDTO result = creditService.createCredit(dto);

        // Assert
        Optional<Credit> creditOpt = creditRepository.findByContractNumber(result.getContractNumber());
        assertTrue(creditOpt.isPresent());

        Credit credit = creditOpt.get();
        assertEquals("99009999-1", credit.getContractNumber());
        assertEquals(BigDecimal.valueOf(1000000), credit.getAmount());
        assertEquals(12, credit.getLoanTerm());
        assertEquals(new BigDecimal("23.5"), credit.getInterestRate());

        assertNotNull(credit.getCreditAccount());
        assertNotNull(credit.getCreditAccount().getAccount12401());

        // Проверка бухгалтерской проводки
        Accounting accounting = accountingRepository.findAll().stream()
                .filter(a -> a.getContractNumber().equals(credit.getContractNumber()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Проводка не найдена"));

        assertEquals(credit.getContractNumber(), accounting.getContractNumber());
        assertEquals(new BigDecimal("1000000.00"), accounting.getAmount());
        assertEquals("Выдача кредита по договору " + credit.getContractNumber(), accounting.getDescription());

        System.out.println(creditOpt);
    }
}
