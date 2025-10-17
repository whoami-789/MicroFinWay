package com.MicroFinWay;

import com.MicroFinWay.dto.CreditDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.User;
import com.MicroFinWay.repository.AccountTypeRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.UserRepository;
import com.MicroFinWay.service.AccountNumberGenerator;
import com.MicroFinWay.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCredit_ShouldGenerateContractAndAccounts_WhenUserExistsAndHasTwoCredits() {
        // Arrange
        String clientKod = "99001234";
        long existingCredits = 2L;

        User user = new User();
        user.setKod(clientKod);
        when(userRepository.findByKod(clientKod)).thenReturn(Optional.of(user));
        when(creditRepository.countByUserKod(clientKod)).thenReturn(existingCredits);

        String expectedSequence = "003";
        String expectedContract = clientKod + "-3";

        // Мокаем генерацию счетов
        Map<String, String> expectedAccounts = Map.of(
                "MICROLOANS_2", "12401000099001234003",
                "НППМ", "16307000099001234003",
                "MICROLOANS_3", "12405000099001234003",
                "МТХФ", "16377000099001234003",
                "COLLATERAL", "94501000099001234003",
                "RESERVE", "22812000099001234003",
                "WRITE_OFF", "16308000099001234003",
                "PENALTY", "16309000099001234003",
                "INCOME", "42001000099001234003"
        );

        for (Map.Entry<String, String> entry : expectedAccounts.entrySet()) {
            when(accountNumberGenerator.generateAccountNumber(eq(entry.getKey()), eq(clientKod), eq(expectedSequence)))
                    .thenReturn(entry.getValue());
        }

        CreditDTO input = new CreditDTO();
        input.setCode(clientKod);
        input.setAmount(new BigDecimal("7000000"));
        input.setCurrencyCode("000");
        input.setLoanTerm(10);
        input.setInterestRate(new BigDecimal("48"));

        when(creditRepository.save(any(Credit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreditDTO result = creditService.createCredit(input);

        // Assert
        assertNotNull(result);
        assertEquals(expectedContract, result.getContractNumber());
        assertEquals(new BigDecimal("7000000"), result.getAmount());
        assertEquals("12401000099001234003", result.getAccountLoanMain());

        verify(accountNumberGenerator, times(9))
                .generateAccountNumber(any(), eq(clientKod), eq(expectedSequence));
        verify(creditRepository).save(any(Credit.class));
    }
}
