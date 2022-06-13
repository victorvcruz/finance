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
import entities.dto.TransactionDTO;

public class TransactionRepository {
    public PostgreClient postgresql;
    public AccountRepository account_repository;
    public CategoryRepository category_repository;

    public TransactionRepository(PostgreClient postgresql, AccountRepository account_repository, CategoryRepository category_repository) {
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
                "where id = '" + transaction_id + "' " +
                "AND canceled = false";

        postgresql.runSql(sql);
    }

    public ResultSet findTransactionById(String transaction_id) throws SQLException {
        String sql = "SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at " +
                "FROM transaction " +
                "WHERE id = '" + transaction_id + "' " +
                "AND canceled = false";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findTransactionDTOById(String transaction_id) throws SQLException {
        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.id = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, transaction_id);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountId(String account_id) throws SQLException {
        String sql = """
                SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at
                FROM transaction
                where account_id = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, account_id);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByCategoryAndDate(String account_id, String category_name, String date_start, String date_end) throws SQLException {

        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.account_id = '%s'
                AND category.name = '%s'
                AND canceled = false
                AND date >= '%s'
                AND date <= '%s'
                """;

        String sqlFormat = String.format(sql, account_id, category_name, date_start.replace("/", "-"), date_end.replace("/", "-"));

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByCategory(String account_id, String category_name) throws SQLException {
        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.account_id = '%s'
                AND category.name = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, account_id, category_name);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByDate(String account_id, String date_start, String date_end) throws SQLException {
        String sql = """
                SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at
                FROM transaction
                where account_id = '%s'
                AND canceled = false
                AND date >= '%s'
                AND date <= '%s'
                """;

        String sqlFormat = String.format(sql, account_id, date_start.replace("/", "-"), date_end.replace("/", "-"));

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByTypeAndOthers(String account_id, String type, String category,String date_start, String date_end) throws SQLException {
        String type_query;
        String category_query_formated = "";
        String date_start_query_formated= "";
        String date_end_query_formated = "";

        if(type.equals("expenses")){
            type_query = "AND value < 0";
        } else{
            type_query = "AND value > 0";
        }

        if(category != ""){
            String category_query = "AND category.name = '%s'";
            category_query_formated = String.format(category_query, category);
        }

        if(date_start != "" && date_end != ""){
            String date_start_query = "AND date >= '%s'" ;
            String date_end_query = "AND date <= '%s'";

            date_start_query_formated = String.format(date_start_query, date_start.replace("/", "-"));
            date_end_query_formated = String.format(date_end_query, date_end.replace("/", "-"));
        }


        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.account_id = '%s'
                AND canceled = false
                %s
                %s
                %s
                %s
                """;

        String sqlFormat = String.format(sql, account_id, type_query, category_query_formated, date_start_query_formated, date_end_query_formated);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ArrayList transactionsOfAccountById(String account_id) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountId(account_id);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

        }

        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategoryAndDate(String account_id, String category_name, String date_start, String date_end) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByCategoryAndDate(account_id, category_name, date_start, date_end);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

        }

        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategory(String account_id, String category_name) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByCategory(account_id, category_name);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

        }

        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByDate(String account_id, String date_start, String date_end) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByDate(account_id, date_start, date_end);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

        }

        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByTypeAndOther(String account_id, String type, String category,String date_start, String date_end) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByTypeAndOthers(account_id, type, category, date_start, date_end);

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

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

    public TransactionDTO findDTOById(String transaction_id) throws SQLException {
        ResultSet result = this.findTransactionDTOById(transaction_id);
        TransactionDTO transaction = null;
        if(result.next()){
            transaction = new TransactionDTO(result.getString("id"), result.getString("name"), result.getString("description"), result.getDouble("value"), result.getTimestamp("date"), result.getTimestamp("created_at"), result.getTimestamp("updated_at"));
        }
        return  transaction;
    }

}
