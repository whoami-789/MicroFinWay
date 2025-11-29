package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AccountPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        ACTIVE, PASSIVE
    }
}