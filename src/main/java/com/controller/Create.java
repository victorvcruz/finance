package com.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.entities.Account;
import com.entities.Category;
import com.entities.Transaction;
import com.database.repository.AccountRepository;
import com.database.repository.CategoryRepository;
import com.database.repository.TransactionRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class Create {

    public AccountRepository accountRepository;
    public CategoryRepository categoryRepository;
    public TransactionRepository transactionRepository;

    public Create(AccountRepository accountRepository, CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;

    }

    public Account account(String username, String password) throws SQLException {
        Account account = new Account(username, password);
        accountRepository.InsertAccount(account);

        return account;

    }

    public String token(String username) throws SQLException {

        String id = accountRepository.getAccountIdByUsername(username);
        LocalDateTime localDate = LocalDateTime.now().plusHours(1);
        Date dateExpirateAt = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuer(id)
                .withExpiresAt(dateExpirateAt)
                .sign(algorithm);
    }


    public Transaction income(String accountID, String description, String categoryName, String date, double value) throws SQLException {

        Account account = accountRepository.findById(accountID);

        Category category = new Category(categoryName, account);

        if (!categoryRepository.existsCategoryByNameAndAccount(categoryName, accountID)){
            categoryRepository.insertCategory(category);
        }

        Transaction transaction = new Transaction(account, category, description, date, value);
        transaction.setCategoryId(UUID.fromString(categoryRepository.findCategoryIdByNameAndAccount(categoryName, accountID)));
        transactionRepository.insertTransaction(transaction);
        System.out.println("Transaction created your id is: " + transaction.getID());
        return transaction;

    }


}
