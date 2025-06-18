package com.MicroFinWay;

import com.MicroFinWay.model.Accounting;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.CreditAccount;
import com.MicroFinWay.repository.AccountingRepository;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.service.AccountingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountingServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private AccountingRepository accountingRepository;

    @InjectMocks
    private AccountingService accountingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenCreditMainLoan_ShouldCreateCorrectEntry() {
        // Arrange
        String contractNumber = "99001234-1";
        BigDecimal amount = new BigDecimal("1000000");

        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setAccount12401("12401000209900123001");

        Credit credit = new Credit();
        credit.setContractNumber(contractNumber);
        credit.setCreditAccount(creditAccount);

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));

        // Act
        accountingService.givenCreditMainLoan(contractNumber, amount);

        // Assert
        ArgumentCaptor<Accounting> captor = ArgumentCaptor.forClass(Accounting.class);
        verify(accountingRepository).save(captor.capture());

        Accounting savedEntry = captor.getValue();
        assertEquals(contractNumber, savedEntry.getContractNumber());
        assertEquals("12401000209900123001", savedEntry.getDebitAccount());
        assertEquals("10101000904619251001", savedEntry.getCreditAccount());
        assertEquals(amount, savedEntry.getAmount());
        assertEquals("Выдача кредита по договору " + contractNumber, savedEntry.getDescription());
    }

    @Test
    void creditPayedMainLoan_ShouldCreateCorrectEntry() {
        String contractNumber = "99001234-1";
        BigDecimal amount = new BigDecimal("500000");

        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setAccount12401("12401000209900123001");

        Credit credit = new Credit();
        credit.setContractNumber(contractNumber);
        credit.setCreditAccount(creditAccount);

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));

        accountingService.creditPayedMainLoan(contractNumber, amount);

        ArgumentCaptor<Accounting> captor = ArgumentCaptor.forClass(Accounting.class);
        verify(accountingRepository).save(captor.capture());

        Accounting entry = captor.getValue();
        assertEquals("10101000904619251001", entry.getDebitAccount());
        assertEquals("12401000209900123001", entry.getCreditAccount());
        assertEquals(amount, entry.getAmount());
        assertEquals("Погашение основного долга по договору " + contractNumber, entry.getDescription());
    }

    @Test
    void creditPayedInterestMain_ShouldCreateCorrectEntry() {
        String contractNumber = "99001234-1";
        BigDecimal amount = new BigDecimal("120000");

        CreditAccount creditAccount = new CreditAccount();
        creditAccount.setAccount16307("16307000209900123001");

        Credit credit = new Credit();
        credit.setContractNumber(contractNumber);
        credit.setCreditAccount(creditAccount);

        when(creditRepository.findByContractNumber(contractNumber)).thenReturn(Optional.of(credit));

        accountingService.creditPayedInterestMain(contractNumber, amount);

        ArgumentCaptor<Accounting> captor = ArgumentCaptor.forClass(Accounting.class);
        verify(accountingRepository).save(captor.capture());

        Accounting entry = captor.getValue();
        assertEquals("10101000904619251001", entry.getDebitAccount());
        assertEquals("16307000209900123001", entry.getCreditAccount());
        assertEquals(amount, entry.getAmount());
        assertEquals("Погашение процентов по договору " + contractNumber, entry.getDescription());
    }
}
