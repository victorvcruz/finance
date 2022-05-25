package database.repository;

import api.Convert;
import entities.Account;
import entities.Category;
import entities.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import database.PostgreClient;

public class TransactionRepository {
    public PostgreClient postgresql;
    public AccountRepository account_repository;
    public CategoryRepository category_repository;

    public TransactionRepository(PostgreClient postgresql, AccountRepository account_repository, CategoryRepository category_repository) throws SQLException, ClassNotFoundException {
        this.postgresql = postgresql;
        this.account_repository = account_repository;
        this.category_repository = category_repository;
    }


    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql =
                """
                INSERT INTO transaction(id, account_id, category_id, description, value, date, canceled, created_at, updated_at)
                VALUES('%s', '%s', '%s', '%s', %s, to_timestamp('%s', 'YYYY-MM-DD'), %s, '%s', '%s'); 
                        """;
        String sqlFormat = String.format(sql, transaction.getId(), transaction.getAccount_id(),
                transaction.getCategory_id(), transaction.getDescription(), transaction.getValue(),
                transaction.getDate(), transaction.isCanceled(), transaction.getCreated_at(),
                transaction.getUpdated_at());

        System.out.println(sqlFormat);

        postgresql.runSql(sqlFormat);

    }

    public void updateCancelTransaction(String transaction_id, String bool) throws SQLException {
        LocalDate update_at = LocalDate.now();

        String sql = "UPDATE transaction " +
                "set canceled = '" + bool + "', " +
                "updated_at = '" + update_at + "' " +
                "where id = '" + transaction_id + "'";

        postgresql.runSql(sql);
    }

    public void updateAttributesTransaction(String transaction_id, String description, String date, double value) throws SQLException {
        LocalDate update_at = LocalDate.now();

        String sql = "UPDATE transaction " +
                "set description = '" + description + "', " +
                "value = '" + value + "', " +
                "date = to_timestamp('" + date + "', 'YYYY-MM-DD'), " +
                "updated_at = '" + update_at + "' " +
                "where id = '" + transaction_id + "'";

        postgresql.runSql(sql);
    }

    public ResultSet findTransactionById(String transaction_id) throws SQLException {
        String sql = "SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at " +
                "FROM transaction " +
                "WHERE id = '" + transaction_id + "'";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findTransactionByAccountId(String account_id) throws SQLException {
        String sql = "SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at " +
                "FROM transaction " +
                "WHERE account_id = '" + account_id + "' " +
                "AND canceled = false";

        return postgresql.runSqlToSelect(sql);
    }

    public ArrayList transactionsOfAccountById(String account_id) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountId(account_id);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findById(result.getString("id"))));

        }

        return transactionList;
    }


    public boolean existsTransactionById(String transaction_id) throws SQLException {
        return this.findTransactionById(transaction_id).next();
    }

    public Transaction findById(String transaction_id) throws SQLException {
        ResultSet result = this.findTransactionById(transaction_id);
        Transaction transaction = null;
        if (result.next()) {
            Account account = account_repository.findById(result.getString("account_id"));
            Category category = category_repository.findById(result.getString("category_id"));
            transaction = new Transaction(account, category, result.getString("description"), result.getString("date"), result.getDouble("value"));
            transaction.setId(UUID.fromString(transaction_id));
            transaction.setCategory_id(UUID.fromString(result.getString("category_id")));
            transaction.setCanceled(result.getBoolean("canceled"));
        }

        return transaction;
    }

}
