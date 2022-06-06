package entities;

import java.time.LocalDate;
import java.util.UUID;

public class Account {

    private UUID id;
    private String username;
    private String password;
    private double balance;
    private LocalDate created_at;
    private LocalDate update_at;

    public Account(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.balance = 0;
        this.created_at = LocalDate.now();
        this.update_at = LocalDate.now();

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {

        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(LocalDate update_at) {
        this.update_at = update_at;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", created_at=" + created_at +
                ", update_at=" + update_at +
                '}';
    }
}
