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
}
