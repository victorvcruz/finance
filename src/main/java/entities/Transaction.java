package entities;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private UUID account_id;
    private UUID category_id;
    private String description;
    private double value;
    private boolean canceled;
    private LocalDate created_at;
    private LocalDate updated_at;

    public Transaction(Category category, Account account, String description, double value) {
        this.id = UUID.randomUUID();
        this.account_id = account.getId();
        this.category_id = category.getId();
        this.description = description;
        this.value = value;
        this.canceled = false;
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
    }

    public UUID getCategory_id() {
        return category_id;
    }

    public void setCategory_id(UUID category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }
}
