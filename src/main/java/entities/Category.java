package entities;

import java.time.LocalDate;
import java.util.UUID;

public class Category {

    private UUID id;
    private String name;
    private UUID account_id;
    private LocalDate created_at;
    private LocalDate updated_at;

    public Category(String name, Account account) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.account_id = account.getId();
        this.created_at = LocalDate.now();
        this.updated_at = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
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
