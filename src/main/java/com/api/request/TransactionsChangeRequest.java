package com.api.request;

public class TransactionsChangeRequest {

    private String id;
    private String description;
    private String category_name;
    private String date;
    private double value;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }
}
