package com.assignment.globaltransfersystem.model;

import jakarta.persistence.*;

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
public class Account implements Serializable {

    @Id
    @Column(name ="account_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "account_status")
    private String accountStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
