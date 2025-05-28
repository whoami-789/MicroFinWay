package com.MicroFinWay.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreditDetailsDTO {
    private CreditDTO credit;  // DTO для кредита
    private UserDTO user;      // DTO для клиента
    private List<CollateralDTO> collaterals;  // Залоги
    private List<PaymentScheduleDTO> paymentSchedules;  // График платежей
}

