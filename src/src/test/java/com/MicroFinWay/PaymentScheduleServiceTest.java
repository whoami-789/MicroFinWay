package com.MicroFinWay;

import com.MicroFinWay.dto.PaymentScheduleDTO;
import com.MicroFinWay.model.Credit;
import com.MicroFinWay.model.PaymentSchedule;
import com.MicroFinWay.repository.CreditRepository;
import com.MicroFinWay.repository.PaymentScheduleRepository;
import com.MicroFinWay.service.PaymentScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentScheduleServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private PaymentScheduleRepository scheduleRepository;

    @InjectMocks
    private PaymentScheduleService paymentScheduleService;

    @Test
    void generateSchedule_ShouldGenerateCorrectly() {
        // Мокаем кредит
        Credit credit = new Credit();
        credit.setContractNumber("12345");
        credit.setLoanTerm(12);
        credit.setContractDate(LocalDate.parse("2025-05-30"));
        credit.setAmount(new BigDecimal("8000000"));
        credit.setInterestRate(new BigDecimal("72")); // 12% годовых
        credit.setPaymentDay(25);

        when(creditRepository.findByContractNumber("12345")).thenReturn(Optional.of(credit));
        when(scheduleRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<PaymentScheduleDTO> schedule = paymentScheduleService.generateSchedule("12345", 11, false);

        assertEquals(12, schedule.size());

        verify(scheduleRepository, times(1)).saveAll(anyList());
    }

}
