package controller;

import database.repository.AccountRepository;
import database.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class View {

    private AccountRepository account_repository;
    private TransactionRepository transaction_repository;

    public View(AccountRepository account_repository, TransactionRepository transaction_repository){
        this.account_repository = account_repository;
        this.transaction_repository = transaction_repository;
    }

    public double viewBalanceOfAccountById(String account_id) throws SQLException {
        return account_repository.getAccountBalance(account_id);
    }

    public ArrayList viewTransactionsOfAccountById(String account_id) throws SQLException {
        return transaction_repository.transactionsOfAccountById(account_id);
    }

    public ArrayList viewTransactionsOfAccountByIdFilteredByCategoryAndDate(String account_id, String category_name, String date_start, String date_end) throws SQLException {
        return transaction_repository.transactionsOfAccountByIdFilteredByCategoryAndDate(account_id, category_name, date_start, date_end);
    }

    public ArrayList viewTransactionsOfAccountByIdFilteredByCategory(String account_id, String category_name) throws SQLException {
        return transaction_repository.transactionsOfAccountByIdFilteredByCategory(account_id, category_name);
    }

    public ArrayList viewTransactionsOfAccountByFilteredByDate(String account_id, String date_start, String date_end) throws SQLException {
        return transaction_repository.transactionsOfAccountByIdFilteredByDate(account_id, date_start, date_end);
    }
}
