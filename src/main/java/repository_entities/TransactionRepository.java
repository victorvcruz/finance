package repository_entities;

import entities.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import database.PostgreClient;

public class TransactionRepository {
    public PostgreClient postgresql;

    public TransactionRepository(PostgreClient postgresql) throws SQLException, ClassNotFoundException {
        this.postgresql = postgresql;
    }


    public void InsertDbTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction(id, account_id, category_id, description, value, date, canceled, created_at, updated_at) "
                + "VALUES('" + transaction.getId() + "', '" + transaction.getAccount_id() + "', '" + transaction.getCategory_id() +
                "', '" + transaction.getDescription() + "', '" + transaction.getValue() + "', '" + transaction.getDate() + "', '" + transaction.isCanceled() + "', '" + transaction.getCreated_at() + "', '" + transaction.getUpdated_at() + "');";

        postgresql.runSql(sql);

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

}
