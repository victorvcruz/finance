package entities.dto;

import entities.Account;
import entities.Category;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionDTO {

    private String id;
    private String category_name;
    private String description;
    private double value;
    private String date;
    private LocalDate created_at;
    private LocalDate updated_at;

    public TransactionDTO(String id, String category_name, String description, String date, double value) {
        this.id = id;
        this.category_name = category_name;
        this.description = description;
        this.value = value;
        this.date = date;
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }



}

