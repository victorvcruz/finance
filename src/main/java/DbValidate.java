import entities.Account;
import entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DbValidate {

    public boolean accountIdValidate(String account_id, PostgreClient postgresql) throws SQLException{
        ResultSet result = postgresql.SelectAccountId(account_id);
        return result.next();
    }

    public boolean categoryNameValidate(String category_name, String account_id, PostgreClient postgresql) throws SQLException{
        ResultSet result = postgresql.selectCategoryName(category_name, account_id);
        return result.next();
    }

    public Account createAccountValidate(String account_id,PostgreClient postgresql) throws SQLException {
        ResultSet result = postgresql.SelectAccountId(account_id);
        Account account = null;
        if (result.next()) {
            account = new Account(result.getString("username"), result.getString("password"));
            account.setId(UUID.fromString(result.getString("id")));
        }

        return account;
    }

    public Category createCategoryValidate(String category_name, Account account, PostgreClient postgresql) throws SQLException {
        ResultSet result = postgresql.selectCategoryName(category_name, account.getId().toString());
        Category category = null;
        if (result.next()) {
            category = new Category(result.getString("name"), account);
        }
        return category;
    }




}
