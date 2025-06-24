package com.MicroFinWay.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credit_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Все счета в цифровом формате
    @Column(name = "account_12401", length = 20)
    private String account12401; // Основной счёт кредита

    @Column(name = "account_12405", length = 20)
    private String account12405; // Просроченный кредит

    @Column(name = "account_12409", length = 20)
    private String account12409; // пересмотренный кредит

    @Column(name = "account_12499", length = 20)
    private String account12499; // резервный счет

    @Column(name = "account_12501", length = 20)
    private String account12501; // перечисление

    @Column(name = "account_14801", length = 20)
    private String account14801; // долгосрочный более 12 месяцев

    @Column(name = "account_14899", length = 20)
    private String account14899; // контр счет по резерву/резервный счет долгосрочного кредита

    @Column(name = "account_15701", length = 20)
    private String account15701; // судебный счет

    @Column(name = "account_15799", length = 20)
    private String account15799; // судебный резервный счет

    @Column(name = "account_16307", length = 20)
    private String account16307; // Общие проценты

    @Column(name = "account_16309", length = 20)
    private String account16309; // проценты

    @Column(name = "account_16377", length = 20)
    private String account16377; // Просроченные проценты

    @Column(name = "account_16405", length = 20)
    private String account16405; // Пени

    @Column(name = "account_22812", length = 20)
    private String account22812; // аванс по процентам

    @Column(name = "account_94502", length = 20)
    private String account94502; // залог золото/машина

    @Column(name = "account_94503", length = 20)
    private String account94503; // прочие залоги

    @Column(name = "account_95413", length = 20)
    private String account95413; // внебалансовый основной долг

    @Column(name = "account_91501", length = 20)
    private String account91501; //внебалансовые проценты

    @Column(name = "contract_number", length = 12)
    private String contractNumber;

    @OneToOne
    @JoinColumn(name = "credit_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonBackReference
    private Credit credit;


}
