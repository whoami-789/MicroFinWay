package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "code", length = 20, unique = true, nullable = false)
    private String code;

    @Column(name = "current_operational_day")
    private LocalDate currentOperationalDay;

    // Можно добавить другие поля: адрес, ИНН, руководитель и т.д.
}
