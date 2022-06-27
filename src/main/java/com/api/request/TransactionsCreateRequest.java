package com.api.request;

public class TransactionsCreateRequest {
    private String description;
    private String category_name;
    private String date;
    private double value;


    public String getDescription() {
        return description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}

