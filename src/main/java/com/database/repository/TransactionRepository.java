package com.database.repository;

import com.api.Convert;
import com.entities.Account;
import com.entities.Category;
import com.entities.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.database.PostgreClient;
import com.entities.dto.TransactionDTO;
import com.entities.dto.TransactionsINFO;

public class TransactionRepository {
    public PostgreClient postgresql;
    public AccountRepository accountRepository;
    public CategoryRepository categoryRepository;

    public TransactionRepository(PostgreClient postgresql, AccountRepository accountRepository, CategoryRepository categoryRepository) {
        this.postgresql = postgresql;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }


    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql =
                """
                INSERT INTO transaction(id, account_id, category_id, description, value, date, canceled, created_at, updated_at)
                VALUES('%s', '%s', '%s', '%s', %s, to_timestamp('%s', 'YYYY-MM-DD'), %s, '%s', '%s');
                        """;
        String sqlFormat = String.format(sql, transaction.getID(), transaction.getAccountId(),
                transaction.getCategoryId(), transaction.getDescription(), transaction.getValue(),
                transaction.getDate(), transaction.isCanceled(), transaction.getCreatedAt(),
                transaction.getUpdatedAt());

        System.out.println(sqlFormat);

        postgresql.runSql(sqlFormat);

    }

    public void updateCancelTransaction(String transactionsID, String bool) throws SQLException {
        LocalDate updateAt = LocalDate.now();

        String sql = "UPDATE transaction " +
                "set canceled = '" + bool + "', " +
                "updated_at = '" + updateAt + "' " +
                "where id = '" + transactionsID + "'";

        postgresql.runSql(sql);
    }

    public void updateAttributesTransaction(String transactionsID, String description, String date, double value) throws SQLException {
        LocalDate updateAt = LocalDate.now();

        String sql = "UPDATE transaction " +
                "set description = '" + description + "', " +
                "value = '" + value + "', " +
                "date = to_timestamp('" + date + "', 'YYYY-MM-DD'), " +
                "updated_at = '" + updateAt + "' " +
                "where id = '" + transactionsID + "' " +
                "AND canceled = false";

        postgresql.runSql(sql);
    }

    public ResultSet findTransactionById(String transactionsID) throws SQLException {
        String sql = "SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at " +
                "FROM transaction " +
                "WHERE id = '" + transactionsID + "' " +
                "AND canceled = false";

        return postgresql.runSqlToSelect(sql);
    }

    public ResultSet findTransactionDTOById(String transactionsID) throws SQLException {
        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.id = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, transactionsID);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountId(String accountID) throws SQLException {
        String sql = """
                SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at
                FROM transaction
                where account_id = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, accountID);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByCategoryAndDate(String accountID, String categoryName, String dateStart, String dateEnd) throws SQLException {

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

        String sqlFormat = String.format(sql, accountID, categoryName, dateStart.replace("/", "-"), dateEnd.replace("/", "-"));

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByCategory(String accountID, String categoryName) throws SQLException {
        String sql = """
                SELECT transaction.id, category.name, transaction.description, transaction.value, transaction.date, transaction.created_at, transaction.updated_at
                FROM transaction
                INNER JOIN category ON category.id = category_id
                WHERE transaction.account_id = '%s'
                AND category.name = '%s'
                AND canceled = false
                """;

        String sqlFormat = String.format(sql, accountID, categoryName);

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByDate(String accountID, String dateStart, String dateEnd) throws SQLException {
        String sql = """
                SELECT id, account_id, category_id, description, value, date, canceled, created_at, updated_at
                FROM transaction
                where account_id = '%s'
                AND canceled = false
                AND date >= '%s'
                AND date <= '%s'
                """;

        String sqlFormat = String.format(sql, accountID, dateStart.replace("/", "-"), dateEnd.replace("/", "-"));

        return postgresql.runSqlToSelect(sqlFormat);
    }

    public ResultSet findTransactionByAccountIdFilteredByTypeAndOthers(String accountID, String type, String category,String dateStart, String dateEnd) throws SQLException {
        String typeQuery;
        String categoryQueryFormated = "";
        String dateStartQueryFormated= "";
        String dateEndQueryFormated = "";

        if(type.equals("expenses")){
            typeQuery = "AND value < 0";
        } else{
            typeQuery = "AND value > 0";
        }

        if(category != ""){
            String categoryQuery = "AND category.name = '%s'";
            categoryQueryFormated = String.format(categoryQuery, category);
        }

        if(dateStart != "" && dateEnd != ""){
            String dateStartQuery = "AND date >= '%s'" ;
            String dateEndQuery = "AND date <= '%s'";

            dateStartQueryFormated = String.format(dateStartQuery, dateStart.replace("/", "-"));
            dateEndQueryFormated = String.format(dateEndQuery, dateEnd.replace("/", "-"));
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

        String sqlFormat = String.format(sql, accountID, typeQuery, categoryQueryFormated, dateStartQueryFormated, dateEndQueryFormated);

        return postgresql.runSqlToSelect(sqlFormat);
    }


    public ArrayList transactionsOfAccountById(String accountID) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountId(accountID);

        int count = 0;
        double allValue = 0;

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

            count++;
            allValue += result.getDouble("value");

        }

        transactionList.add(Convert.json().toJson(new TransactionsINFO(count, allValue)));


        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategoryAndDate(String accountID, String categoryName, String dateStart, String dateEnd) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByCategoryAndDate(accountID, categoryName, dateStart, dateEnd);

        int count = 0;
        double allValue = 0;

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

            count++;
            allValue += result.getDouble("value");
        }

        transactionList.add(Convert.json().toJson(new TransactionsINFO(count, allValue)));


        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategory(String accountID, String categoryName) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByCategory(accountID, categoryName);

        int count = 0;
        double allValue = 0;

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

            count++;
            allValue += result.getDouble("value");
        }

        transactionList.add(Convert.json().toJson(new TransactionsINFO(count, allValue)));


        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByDate(String accountID, String dateStart, String dateEnd) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByDate(accountID, dateStart, dateEnd);

        int count = 0;
        double allValue = 0;

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));

            count++;
            allValue += result.getDouble("value");
        }

        transactionList.add(Convert.json().toJson(new TransactionsINFO(count, allValue)));


        return transactionList;
    }

    public ArrayList transactionsOfAccountByIdFilteredByTypeAndOther(String accountID, String type, String category,String dateStart, String dateEnd) throws SQLException {
        ArrayList<String> transactionList = new ArrayList<>();
        ResultSet result = this.findTransactionByAccountIdFilteredByTypeAndOthers(accountID, type, category, dateStart, dateEnd);

        int count = 0;
        double allValue = 0;

        while(result.next()){
            transactionList.add(Convert.json().toJson(this.findDTOById(result.getString("id"))));
            count++;
            allValue += result.getDouble("value");
        }

        transactionList.add(Convert.json().toJson(new TransactionsINFO(count, allValue)));


        return transactionList;
    }




    public boolean existsTransactionById(String transactionsID) throws SQLException {
        return this.findTransactionById(transactionsID).next();
    }

    public Transaction findById(String transactionsID) throws SQLException {
        ResultSet result = this.findTransactionById(transactionsID);
        Transaction transaction = null;
        if (result.next()) {
            Account account = accountRepository.findById(result.getString("account_id"));
            Category category = categoryRepository.findById(result.getString("category_id"));
            transaction = new Transaction(account, category, result.getString("description"), result.getString("date"), result.getDouble("value"));
            transaction.setID(UUID.fromString(transactionsID));
            transaction.setCategoryId(UUID.fromString(result.getString("category_id")));
            transaction.setCanceled(result.getBoolean("canceled"));
        }

        return transaction;
    }

    public TransactionDTO findDTOById(String transactionsID) throws SQLException {
        ResultSet result = this.findTransactionDTOById(transactionsID);
        TransactionDTO transaction = null;
        if(result.next()){
            transaction = new TransactionDTO(result.getString("id"), result.getString("name"), result.getString("description"), result.getDouble("value"), result.getTimestamp("date"), result.getTimestamp("created_at"), result.getTimestamp("updated_at"));
        }
        return  transaction;
    }

}
