package controller;

import database.repository.AccountRepository;
import database.repository.CategoryRepository;
import database.repository.TransactionRepository;

import java.sql.SQLException;

public class Change {
    public AccountRepository account_repository;
    public CategoryRepository category_repository;
    public TransactionRepository transaction_repository;

    public Change(AccountRepository account_repository, CategoryRepository category_repository, TransactionRepository transaction_repository) {
        this.account_repository = account_repository;
        this.category_repository = category_repository;
        this.transaction_repository = transaction_repository;
    }

    public void cancelTransaction(String transaction_id, String bool) throws SQLException {
        transaction_repository.updateCancelTransaction(transaction_id, bool);
    }

    public void transaction(String transaction_id, String description, String date, double value) throws SQLException {
        transaction_repository.updateAttributesTransaction(transaction_id, description, date, value);
    }

    public void accountUsername(String account_id, String username) throws SQLException {
        account_repository.updateUsernameAccount(account_id, username);
    }

    public void accountPassword(String account_id, String password) throws SQLException {
        account_repository.updatePasswordAccount(account_id, password);
    }
}
