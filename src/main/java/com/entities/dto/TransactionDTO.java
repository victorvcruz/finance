package com.entities.dto;

import java.sql.Timestamp;


public class TransactionDTO {

    private String id;
    private String categoryName;
    private String description;
    private double value;
    private Timestamp date;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TransactionDTO(String id, String categoryName, String description, double value, Timestamp date, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.value = value;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



}

