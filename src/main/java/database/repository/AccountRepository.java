package database.repository;
import database.PostgreClient;
import entities.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class AccountRepository {
    public PostgreClient postgresql;

    public AccountRepository(PostgreClient postgresql) {
        this.postgresql = postgresql;
    }

    public void InsertAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account(id, username, password, created_at, updated_at) "
                + "VALUES('" + account.getId() + "', '" + account.getUsername() + "', '" + account.getPassword() +
                "', '" + account.getCreated_at() + "', '" + account.getUpdate_at() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet findAccountByUsername(String account_name) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + account_name + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findAccountById(String account_id) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE id = '" + account_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public String getAccountIdByUsername(String account_name) throws SQLException {
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


    public void updateUsernameAccount(String account_id, String username) throws SQLException {
        LocalDate update_at = LocalDate.now();

        String sql = "UPDATE account " +
                "set username = '" + username + "', " +
                "updated_at = '" + update_at + "' " +
                "where id = '" + account_id + "'";

        postgresql.runSql(sql);
    }

    public void updatePasswordAccount(String account_id, String password) throws SQLException {
        LocalDate update_at = LocalDate.now();

        String sql = "UPDATE account " +
                "set password = '" + password + "', " +
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

    public int countTransactionByAccountId(String account_id) throws SQLException {
        String sql = "SELECT count(id) " +
                "FROM transaction " +
                "WHERE account_id = '" + account_id + "'";
        ResultSet result = postgresql.runSqlToSelect(sql);
        result.next();
        return result.getInt("count");

    }

    public boolean authenticateAccount(String account_id, String account_password) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE id = '" + account_id + "' " +
                "AND password = '" + account_password + "'";

        return postgresql.runSqlToSelect(sql).next();
    }

    public boolean validateAccountById(String account_id) throws SQLException{
        ResultSet result = this.findAccountById(account_id);
        return result.next();
    }

    public boolean existsAccountByUsername(String account_name) throws SQLException {
        return this.findAccountByUsername(account_name).next();
    }

    public Account findById(String account_id) throws SQLException {
        ResultSet result = this.findAccountById(account_id);
        Account account = null;
        if (result.next()) {
            account = new Account(result.getString("username"), result.getString("password"));
            account.setId(UUID.fromString(account_id));
        }

        return account;
    }

}
