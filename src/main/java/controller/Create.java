package controller;

import entities.Account;
import entities.Category;
import entities.Transaction;
import database.repository.AccountRepository;
import database.repository.CategoryRepository;
import database.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.UUID;

public class Create {

    public AccountRepository account_repository;
    public CategoryRepository category_repository;
    public TransactionRepository transaction_repository;

    public Create(AccountRepository account_repository, CategoryRepository category_repository, TransactionRepository transaction_repository) throws SQLException, ClassNotFoundException {
        this.account_repository = account_repository;
        this.category_repository = category_repository;
        this.transaction_repository = transaction_repository;

    }

    public void createAccount(String username, String password) throws SQLException {
        Account account = new Account(username, password);
        if(!account_repository.existsAccountByUsername(username)){
            account_repository.InsertAccount(account);
        }
    }

    public Transaction createIncome(String account_id, String password, String description, String category_name, String date, double value) throws SQLException {
        if(account_repository.authenticateAccount(account_id, password)){
            return null;
        }

        Account account = account_repository.findById(account_id);

        Category category = new Category(category_name, account);

        if (!category_repository.existsCategoryByNameAndAccount(category_name, account_id)){
            category_repository.insertCategory(category);
        }

        Transaction transaction = new Transaction(account, category, description, date, value);
        transaction.setCategory_id(UUID.fromString(category_repository.findCategoryIdByNameAndAccount(category_name, account_id)));
        transaction_repository.insertTransaction(transaction);
        System.out.println("Transaction created your id is: " + transaction.getId());
        return transaction;

    }


}