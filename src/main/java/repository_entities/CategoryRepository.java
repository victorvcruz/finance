package repository_entities;

import entities.Category;
import database.PostgreClient;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRepository {
    public PostgreClient postgresql;

    public CategoryRepository(PostgreClient postgresql) throws SQLException, ClassNotFoundException {
        this.postgresql = postgresql;
    }



    public void InsertDbCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category(id, name, account_id, created_at, updated_at) "
                + "VALUES('" + category.getId() + "', '" + category.getName() + "', '" + category.getAccount_id() +
                "', '" + category.getCreated_at() + "', '" + category.getUpdated_at() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet selectCategoryName(String category_name, String account_id) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE name = '" + category_name + "' " +
                "AND account_id = '" + account_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public boolean categoryNameValidate(String category_name, String account_id) throws SQLException{
        ResultSet result = this.selectCategoryName(category_name, account_id);
        return result.next();
    }

}
