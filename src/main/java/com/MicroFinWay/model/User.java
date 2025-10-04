package com.MicroFinWay.model;

import com.MicroFinWay.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Represents a user entity with attributes that can belong to either
 * individuals or legal entities. This class is annotated as an entity
 * for persistence in a database table named "users".
 *
 * Key entities include user details, contact information, personal
 * and organizational attributes, banking details, and metadata such as
 * creation and update timestamps.
 *
 * The fields include:
 * - Identification details (e.g., id, kod, INN, passport details)
 * - Contact information (e.g., phone numbers, address)
 * - User types and statuses (e.g., userType, status, filial)
 * - Banking details (e.g., bank card number, bank name, etc.)
 * - Additional fields specific to individuals (e.g., birth date)
 *   and legal entities (e.g., registration details)
 * - Metadata (e.g., createdAt, updatedAt)
 * - Geographical details (e.g., city, region, district)
 *
 * It maintains a relationship with credits assigned to the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор пользователя

    @Column(name = "kod", nullable = false, unique = true)
    private String kod;

    @Column(name = "full_name")
    private String fullName; // Полное имя/название пользователя

    @Column(name = "phone_mobile", length = 20)
    private String phoneMobile; // Мобильный телефон

    @Column(name = "phone_home", length = 20)
    private String phoneHome; // Домашний телефон

    @Column(name = "address", length = 260)
    private String address; // Адрес

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType; // Тип пользователя (физическое/юридическое лицо)

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Примечания

    @Column(name = "status")
    private Integer status; // Статус

    @Column(name = "filial")
    private Integer filial; // Филиал

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Дата создания записи

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Дата последнего изменения

    // Общие поля для физических и юридических лиц
    @Column(name = "inn", length = 12)
    private String inn; // ИНН (для юр. лиц и индивидуальных предпринимателей)

    @Column(name = "passport_series", length = 4)
    private String passportSeries; // Серия паспорта (для физ. лиц)

    @Column(name = "passport_number", length = 10)
    private String passportNumber; // Номер паспорта (для физ. лиц)

    @Column(name = "passport_issued_date")
    private LocalDate passportIssuedDate; // Дата выдачи паспорта (для физ. лиц)

    @Column(name = "passport_issued_by", columnDefinition = "TEXT")
    private String passportIssuedBy; // Кем выдан паспорт (для физ. лиц)

    @Column(name = "birth_date")
    private LocalDate birthDate; // Дата рождения (для физ. лиц)

    @Column(name = "personal_identification_number", length = 20)
    private String personalIdentificationNumber; // ПИНФЛ (для физ. лиц)

    // Специфичные поля для юридических лиц
    @Column(name = "short_name", length = 30)
    private String shortName; // Краткое название (для юр. лиц)

    @Column(name = "okonx", length = 5)
    private String okonx; // ОКОНХ (для юр. лиц)

    @Column(name = "registration_number")
    private Integer registrationNumber; // Регистрационный номер (для юр. лиц)

    @Column(name = "registration_date")
    private LocalDate registrationDate; // Дата регистрации (для юр. лиц)

    @Column(name = "director_name", length = 100)
    private String directorName; // ФИО директора (для юр. лиц)

    @Column(name = "accountant_name", length = 60)
    private String accountantName; // ФИО бухгалтера (для юр. лиц)

    @Column(name = "ownership_type")
    private Integer ownershipType; // Тип собственности (для юр. лиц)

    @Column(name = "opf", length = 4)
    private String opf; // Организационно-правовая форма (для юр. лиц)

    // Банковские данные для физических и юридических лиц
    @Column(name = "bank_card_number", length = 19)
    private String bankCardNumber; // Номер банковской карты

    @Column(name = "bank_card_expiration_date")
    private LocalDate bankCardExpirationDate; // Дата истечения срока действия карты

    @Column(name = "bank_card_holder_name", length = 100)
    private String bankCardHolderName; // Имя владельца карты

    @Column(name = "bank_card_type", length = 10)
    private String bankCardType; // Тип карты (Visa, MasterCard и т.д.)

    @Column(name = "bank_name", length = 100)
    private String bankName; // Название банка

    @Column(name = "bank_bic", length = 9)
    private String bankBic; // БИК банка

    @Column(name = "bank_correspondent_account", length = 20)
    private String bankCorrespondentAccount; // Корреспондентский счет банка

    // Дополнительные поля для юридических лиц
    @Column(name = "legal_address", length = 260)
    private String legalAddress; // Юридический адрес (для юр. лиц)

    @Column(name = "director_passport_series", length = 4)
    private String directorPassportSeries; // Серия паспорта директора (для юр. лиц)

    @Column(name = "director_passport_number", length = 10)
    private String directorPassportNumber; // Номер паспорта директора (для юр. лиц)

    @Column(name = "director_birth_date")
    private LocalDate directorBirthDate; // Дата рождения директора (для юр. лиц)

    @Column(name = "director_identification_number", length = 20)
    private String directorIdentificationNumber; // ИНН директора (для юр. лиц)

    // Прочие дополнительные поля
    @Column(name = "employment_status")
    private Integer employmentStatus; // Статус занятости (для физ. лиц)

    @Column(name = "social_security_number", length = 20)
    private String socialSecurityNumber; // Номер социального страхования (для физ. лиц)

    // Дополнительные поля для физ. и юр. лиц
    @Column(name = "gender")
    private String gender; // Пол пользователя (мужской, женский)

    @Column(name = "city", length = 100)
    private String city; // Город

    @Column(name = "district", length = 100)
    private String district; // Район

    @Column(name = "region", length = 100)
    private String region; // Область

    @Column(name = "city_code")
    private Integer cityCode; // Код города

    @Column(name = "district_code")
    private Integer districtCode; // Код района

    @Column(name = "region_code")
    private Integer regionCode; // Код области

    @Column(name = "katm_sir")
    private String katm_sir;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"}) // чтобы у кредита не тянул обратно юзера
    private List<Credit> credits;
// Список кредитов пользователя

}
