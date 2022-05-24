package database.repository;

import entities.Account;
import entities.Category;
import database.PostgreClient;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRepository {
    public PostgreClient postgresql;
    public AccountRepository account_repository;

    public CategoryRepository(PostgreClient postgresql, AccountRepository account_repository){
        this.postgresql = postgresql;
        this.account_repository = account_repository;
    }

    public void insertCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category(id, name, account_id, created_at, updated_at) "
                + "VALUES('" + category.getId() + "', '" + category.getName() + "', '" + category.getAccount_id() +
                "', '" + category.getCreated_at() + "', '" + category.getUpdated_at() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet findCategoryByNameAndAccount(String category_name, String account_id) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE name = '" + category_name + "' " +
                "AND account_id = '" + account_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findCategoryById(String category_id) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE id = '" + category_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public String findCategoryIdByNameAndAccount(String category_name, String account_id) throws SQLException {
        ResultSet result =this.findCategoryByNameAndAccount(category_name, account_id);
        if(result.next()){
            return result.getString("id");
        }
        return null;
    }

    public boolean existsCategoryByNameAndAccount(String category_name, String account_id) throws SQLException{
        ResultSet result = this.findCategoryByNameAndAccount(category_name, account_id);
        return result.next();
    }

    public Category findById(String category_id) throws SQLException {
        ResultSet result = this.findCategoryById(category_id);
        Category category = null;
        if (result.next()) {
            Account account = account_repository.findById(result.getString("account_id"));
            category = new Category(result.getString("name"), account);
        }
        return category;
    }

}
