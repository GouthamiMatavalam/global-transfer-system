package com.assignment.globaltransfersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Account class to map the records in database table ACCOUNTS
 *
 * @author Gouthami Matavalam
 *
 */

@Entity
@Table(name = "ACCOUNTS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    @Id
    @Column(name ="account_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Client client;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "account_status")
    private String accountStatus;
}
