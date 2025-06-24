package com.MicroFinWay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDetailsDTO {
    private CreditDTO credit;
    private List<PaymentScheduleDTO> schedules;
    private CreditAccountDTO account;
    private List<CollateralDTO> collaterals; // ⚠️ тут изменено
}



