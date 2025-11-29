package com.MicroFinWay.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "debit", nullable = false, length = 10)
    private String debit;

    @Column(name = "credit", nullable = false, length = 10)
    private String credit;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    // Тип ордера: PRIHODNYJ, RASHODNYJ, MEMORIALNYJ
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 20)
    private OrderType orderType;

    public enum OrderType {
        PRIHODNYJ,  // приходный ордер
        RASHODNYJ,  // расходный ордер
        MEMORIALNYJ // мемориальный ордер
    }
}