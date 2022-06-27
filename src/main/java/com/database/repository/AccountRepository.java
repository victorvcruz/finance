package com.database.repository;

import com.database.PostgreClient;
import com.entities.Account;
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
                + "VALUES('" + account.getID() + "', '" + account.getUsername() + "', '" + account.getPassword() +
                "', '" + account.getCreatedAt() + "', '" + account.getUpdateAt() + "');";

        postgresql.runSql(sql);
    }

    public ResultSet findAccountByUsername(String accountName) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + accountName + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findAccountById(String accountID) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE id = '" + accountID + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public String getAccountIdByUsername(String accountName) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + accountName + "'";

        ResultSet result = postgresql.runSqlToSelect(sql);

        String id = null;
        if (result.next()) {
            id = result.getString("id");
        }
        return id;
    }


    public void updateUsernameAccount(String accountID, String username) throws SQLException {
        LocalDate updateAt = LocalDate.now();

        String sql = "UPDATE account " +
                "set username = '" + username + "', " +
                "updated_at = '" + updateAt + "' " +
                "where id = '" + accountID + "'";

        postgresql.runSql(sql);
    }

    public void updatePasswordAccount(String accountID, String password) throws SQLException {
        LocalDate updateAt = LocalDate.now();

        String sql = "UPDATE account " +
                "set password = '" + password + "', " +
                "updated_at = '" + updateAt + "' " +
                "where id = '" + accountID + "'";

        postgresql.runSql(sql);
    }

    public double getAccountBalance(String accountID) throws SQLException {
        String sql = "SELECT " +
                "SUM(value) " +
                "FROM transaction " +
                "WHERE account_id = '" + accountID + "' " +
                "AND canceled = 'false'";
        ResultSet result = postgresql.runSqlToSelect(sql);
        result.next();
        return result.getDouble("sum");
    }

    public boolean authenticateAccount(String accountUsername, String accountPassword) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE username = '" + accountUsername + "' " +
                "AND password = '" + accountPassword + "'";

        return postgresql.runSqlToSelect(sql).next();
    }

    public boolean existsAccountByUsername(String accountName) throws SQLException {
        return this.findAccountByUsername(accountName).next();
    }

    public Account findById(String accountID) throws SQLException {
        ResultSet result = this.findAccountById(accountID);
        Account account = null;
        if (result.next()) {
            account = new Account(result.getString("username"), result.getString("password"));
            account.setID(UUID.fromString(accountID));
        }

        return account;
    }




}
