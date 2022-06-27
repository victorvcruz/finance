package com.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Account {

    private UUID id;
    private String username;
    private String password;
    private double balance;
    private LocalDate createdAt;
    private LocalDate updateAt;

    public Account(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.balance = 0;
        this.createdAt = LocalDate.now();
        this.updateAt = LocalDate.now();

    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }
}
