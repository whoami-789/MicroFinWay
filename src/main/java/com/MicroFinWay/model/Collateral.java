package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "collateral")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collateral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CollateralCategory category;

    @Column(name = "value", precision = 20, scale = 2)
    private BigDecimal value;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "taken_from_client")
    private LocalDate takenFromClient;

    @Column(name = "given_to_bank")
    private LocalDate givenToBank;

    @Column(name = "taken_from_bank")
    private LocalDate takenFromBank;

    @Column(name = "given_to_client")
    private LocalDate givenToClient;

    @Column(name = "engine_number")
    private String engineNumber;

    @Column(name = "car_body_number")
    private String carBodyNumber;

    @Column(name = "car_year")
    private String carYear;

    @Column(name = "car_state_number")
    private String carStateNumber;

    @Column(name = "car_model")
    private String carModel;

    @Column(name = "car_chassi_number")
    private String carChassiNumber;

    @Column(name = "car_color")
    private String carColor;

    @Column(name = "car_passport")
    private String carPassport;

    @Column(name = "car_vin_number")
    private String carVinNumber;

    @Column(name = "contract_number")
    private String contractNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false, referencedColumnName = "id")
    private Credit credit;
}

