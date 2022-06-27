package com.database.repository;

import com.entities.Account;
import com.entities.Category;
import com.database.PostgreClient;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRepository {
    public PostgreClient postgresql;
    public AccountRepository accountRepository;

    public CategoryRepository(PostgreClient postgresql, AccountRepository accountRepository){
        this.postgresql = postgresql;
        this.accountRepository = accountRepository;
    }

    public void insertCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category(id, name, account_id, created_at, updated_at) "
                + "VALUES('" + category.getID() + "', '" + category.getName() + "', '" + category.getAccountID() +
                "', '" + category.getCreatedAt() + "', '" + category.getUpdatedAt() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet findCategoryByNameAndAccount(String categoryName, String accountID) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE name = '" + categoryName + "' " +
                "AND account_id = '" + accountID + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findCategoryById(String categoryID) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE id = '" + categoryID + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public String findCategoryIdByNameAndAccount(String categoryName, String accountID) throws SQLException {
        ResultSet result =this.findCategoryByNameAndAccount(categoryName, accountID);
        if(result.next()){
            return result.getString("id");
        }
        return null;
    }

    public boolean existsCategoryByNameAndAccount(String categoryName, String accountID) throws SQLException{
        ResultSet result = this.findCategoryByNameAndAccount(categoryName, accountID);
        return result.next();
    }

    public Category findById(String categoryID) throws SQLException {
        ResultSet result = this.findCategoryById(categoryID);
        Category category = null;
        if (result.next()) {
            Account account = accountRepository.findById(result.getString("account_id"));
            category = new Category(result.getString("name"), account);
        }
        return category;
    }

}
