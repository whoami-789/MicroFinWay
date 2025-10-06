package com.MicroFinWay.dto;

import com.MicroFinWay.model.CollateralCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollateralDTO {
    private Long id;
    private String name;
    private String collateralCategoryCode; // теперь просто код, а не объект
    private BigDecimal value;
    private String description;
    private LocalDate takenFromClient;
    private LocalDate givenToBank;
    private LocalDate takenFromBank;
    private LocalDate givenToClient;
    private String engineNumber;
    private String carBodyNumber;
    private String carYear;
    private String carStateNumber;
    private String carModel;
    private String carChassiNumber;
    private String carColor;
    private String carPassport;
    private String carVinNumber;
    private String creditId;
}
