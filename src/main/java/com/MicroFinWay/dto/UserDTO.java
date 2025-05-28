package com.MicroFinWay.dto;

import com.MicroFinWay.model.User;
import com.MicroFinWay.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String kod;
    private String fullName;
    private String phoneMobile;
    private String phoneHome;
    private String address;
    private UserType userType;
    private String notes;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String inn;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssuedDate;
    private String passportIssuedBy;
    private LocalDate birthDate;
    private String personalIdentificationNumber;
    private String shortName;
    private String okonx;
    private Integer registrationNumber;
    private LocalDate registrationDate;
    private String directorName;
    private String accountantName;
    private Integer ownershipType;
    private String opf;
    private String bankCardNumber;
    private LocalDate bankCardExpirationDate;
    private String bankCardHolderName;
    private String bankCardType;
    private String bankName;
    private String bankBic;
    private String bankCorrespondentAccount;
    private String legalAddress;
    private String directorPassportSeries;
    private String directorPassportNumber;
    private LocalDate directorBirthDate;
    private String directorIdentificationNumber;
    private Integer employmentStatus;
    private String socialSecurityNumber;
    private String gender;
    private String city;
    private String district;
    private String region;
    private Integer cityCode;
    private Integer districtCode;
    private Integer regionCode;
    private String katm_sir;
}
