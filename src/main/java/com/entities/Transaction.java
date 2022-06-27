package com.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private UUID accountID;
    private UUID categoryID;
    private String description;
    private double value;
    private String date;
    private boolean canceled;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Transaction(Account account, Category category, String description, String date, double value) {
        this.id = UUID.randomUUID();
        this.accountID = account.getID();
        this.categoryID = category.getID();
        this.description = description;
        this.value = value;
        this.date = date;
        this.canceled = false;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountID;
    }

    public UUID getCategoryId() {
        return categoryID;
    }

    public void setCategoryId(UUID category_id) {
        this.categoryID = category_id;
    }

    public String getDescription() {
        return description;
    }


    public double getValue() {
        return value;
    }


    public String getDate() {
        return date;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }


}
