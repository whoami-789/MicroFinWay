package com.askobackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Уникальный идентификатор филиала

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;  // Филиал организации

    @Column(name = "name", length = 50)
    private String name;  // Название филиала

    @Column(name = "address", length = 160)
    private String address;  // Адрес филиала

    @Column(name = "phone", length = 60)
    private String phone;  // Телефон филиала

    @Column(name = "email", length = 30)
    private String email;  // Электронная почта филиала

    @Column(name = "director_name", length = 100)
    private String directorName;  // ФИО директора филиала

    @Column(name = "employee_count")
    private Integer employeeCount;  // Количество сотрудников филиала

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private Set<Staff> staffMembers;  // Сотрудники филиала
}
