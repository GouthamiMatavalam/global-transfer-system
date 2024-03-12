package com.assignment.globaltransfersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Transactions class to map the records in database table TRANSACTIONS
 *
 * @author Gouthami Matavalam
 *
 */

@Entity
@Table(name = "TRANSACTIONS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transactions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Account account;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "to_account_id")
    private Long toAccountId;

    @Column(name = "type_of_transaction")
    private String transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "remarks")
    private String remarks;
}