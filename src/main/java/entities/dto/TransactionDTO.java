package entities.dto;

import java.sql.Timestamp;


public class TransactionDTO {

    private String id;
    private String category_name;
    private String description;
    private double value;
    private Timestamp date;
    private Timestamp created_at;
    private Timestamp updated_at;

    public TransactionDTO(String id, String category_name, String description, double value, Timestamp date, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.category_name = category_name;
        this.description = description;
        this.value = value;
        this.date = date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }



}

