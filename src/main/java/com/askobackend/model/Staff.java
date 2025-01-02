package com.askobackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Уникальный идентификатор сотрудника

    @Column(name = "full_name", nullable = false)
    private String fullName;  // Полное имя сотрудника

    @Column(name = "position", nullable = false)
    private String position;  // Должность сотрудника

    @Column(name = "login", nullable = false, unique = true)
    private String login;  // Логин для входа

    @Column(name = "password", nullable = false)
    private String password;  // Пароль сотрудника

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;  // Филиал сотрудника

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;  // Дата создания записи

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // Дата последнего обновления записи
}
