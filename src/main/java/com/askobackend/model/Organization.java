package com.askobackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @Column(name = "organization_id", length = 5, nullable = false)
    private String organizationId;  // Идентификатор организации

    @Column(name = "name", length = 50)
    private String name;  // Название организации

    @Column(name = "short_name", length = 20)
    private String shortName;  // Краткое название организации

    @Column(name = "account_number", length = 20)
    private String accountNumber;  // Лицевой счет

    @Column(name = "mfo", length = 5)
    private String mfo;  // МФО

    @Column(name = "inn", length = 9)
    private String inn;  // ИНН

    @Column(name = "classification_code", length = 5)
    private String classificationCode;  // Код классификации

    @Column(name = "address", length = 160)
    private String address;  // Адрес организации

    @Column(name = "phone", length = 60)
    private String phone;  // Телефон организации

    @Column(name = "email", length = 30)
    private String email;  // Электронная почта организации

    @Column(name = "bank_system_code", length = 5)
    private String bankSystemCode;  // Код банковской системы

    @Column(name = "operation_day_status")
    private Integer operationDayStatus;  // Статус операционного дня

    @Column(name = "operation_day")
    private LocalDate operationDay;  // Дата операционного дня

    @Column(name = "days_in_year")
    private Integer daysInYear;  // Количество дней в году

    @Column(name = "capital", precision = 18, scale = 2)
    private BigDecimal capital;  // Капитал организации

    @Column(name = "assets", precision = 18, scale = 2)
    private BigDecimal assets;  // Активы организации

    @Column(name = "assets_30_days", precision = 18, scale = 2)
    private BigDecimal assets30Days;  // Активы за 30 дней

    @Column(name = "liabilities", precision = 18, scale = 2)
    private BigDecimal liabilities;  // Пассивы организации

    @Column(name = "chairman_name", length = 60)
    private String chairmanName;  // ФИО председателя

    @Column(name = "director_name", length = 60)
    private String directorName;  // ФИО директора

    @Column(name = "chief_accountant_name", length = 60)
    private String chiefAccountantName;  // ФИО главного бухгалтера

    @Column(name = "employee_count")
    private Integer employeeCount;  // Количество сотрудников организации

    @Column(name = "credit_office_employee_count")
    private Integer creditOfficeEmployeeCount;  // Количество сотрудников в кредитном отделе

    @Column(name = "branch_count")
    private Integer branchCount;  // Количество филиалов

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<Branch> branches;  // Связь с филиалами организации
}

