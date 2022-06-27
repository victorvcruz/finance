package com.controller;

import com.database.repository.AccountRepository;
import com.database.repository.CategoryRepository;
import com.database.repository.TransactionRepository;

import java.sql.SQLException;

public class Change {
    public AccountRepository accountRepository;
    public CategoryRepository categoryRepository;
    public TransactionRepository transactionRepository;

    public Change(AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public void cancelTransaction(String transactionID, String bool) throws SQLException {
        transactionRepository.updateCancelTransaction(transactionID, bool);
    }

    public void transaction(String transactionID, String description, String date, double value) throws SQLException {
        transactionRepository.updateAttributesTransaction(transactionID, description, date, value);
    }

    public void accountUsername(String accountID, String username) throws SQLException {
        accountRepository.updateUsernameAccount(accountID, username);
    }

    public void accountPassword(String accountID, String password) throws SQLException {
        accountRepository.updatePasswordAccount(accountID, password);
    }
}
