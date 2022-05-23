package database;

import entities.Account;
import entities.Category;
import repository_entities.AccountRepository;
import repository_entities.CategoryRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DbValidate {
    public AccountRepository account_repository;
    public CategoryRepository category_repository;

    public DbValidate(AccountRepository account_repository, CategoryRepository category_repository) throws SQLException, ClassNotFoundException {
        this.account_repository = account_repository;
        this.category_repository = category_repository;
    }

    public boolean accountIdValidate(String account_id) throws SQLException{
        return account_repository.accountIdValidate(account_id);
    }

    public boolean accountNameValidate(String account_name) throws SQLException {
        return account_repository.SelectAccountName(account_name).next();
    }


    public boolean categoryValidate(String category_name, String account_id) throws SQLException{
        return category_repository.categoryNameValidate(category_name, account_id);
    }

    public Account createAccountValidate(String account_name) throws SQLException {
        ResultSet result = account_repository.SelectAccountName(account_name);
        Account account = null;
        if (result.next()) {
            account = new Account(result.getString("username"), result.getString("password"));
            account.setId(UUID.fromString(result.getString("id")));
        }

        return account;
    }

    public Category createCategoryValidate(String category_name, Account account) throws SQLException {
        ResultSet result = category_repository.selectCategoryName(category_name, account.getId().toString());
        Category category = null;
        if (result.next()) {
            category = new Category(result.getString("name"), account);
        }
        return category;
    }

    public boolean authenticateAccount(String category_name, String account_password) throws SQLException {
        return account_repository.selectAuthenticateAccount(category_name, account_password);
    }




}
