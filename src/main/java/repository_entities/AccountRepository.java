package repository_entities;
import database.PostgreClient;
import entities.Account;
import entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AccountRepository {
    public PostgreClient postgresql;

    public AccountRepository(PostgreClient postgresql) throws SQLException, ClassNotFoundException {
        this.postgresql = postgresql;
    }

    public void InsertDbAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account(id, username, password, created_at, updated_at) "
                + "VALUES('" + account.getId() + "', '" + account.getUsername() + "', '" + account.getPassword() +
                "', '" + account.getCreated_at() + "', '" + account.getUpdate_at() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet SelectAccountName(String account_name) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + account_name + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet SelectAccountId(String account_id) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE id = '" + account_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public String getAccountId(String account_name) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + account_name + "'";

        ResultSet result = postgresql.runSqlToSelect(sql);

        String id = null;
        if (result.next()) {
            id = result.getString("id");
        }
        return id;
    }


    public void updateAttributesAccount(String account_id, String username, String password) throws SQLException {
        LocalDate update_at = LocalDate.now();

        String sql = "UPDATE account " +
                "set username = '" + username + "', " +
                "password = '" + password + "', " +
                "updated_at = '" + update_at + "' " +
                "where id = '" + account_id + "'";

        postgresql.runSql(sql);
    }

    public double getAccountBalance(String account_id) throws SQLException {
        String sql = "SELECT " +
                "SUM(value) " +
                "FROM transaction " +
                "WHERE account_id = '" + account_id + "' " +
                "AND canceled = 'false'";
        ResultSet result = postgresql.runSqlToSelect(sql);
        result.next();
        return result.getDouble("sum");
    }

    public boolean accountIdValidate(String account_id) throws SQLException{
        ResultSet result = this.SelectAccountId(account_id);
        return result.next();
    }

    public boolean selectAuthenticateAccount(String account_name, String account_password) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + account_name + "' " +
                "AND password = '" + account_password + "'";

        return postgresql.runSqlToSelect(sql).next();
    }



}
