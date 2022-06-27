package com.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Category {

    private UUID id;
    private String name;
    private UUID accountID;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Category(String name, Account account) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.accountID = account.getID();
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public UUID getAccountID() {
        return accountID;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    
}
