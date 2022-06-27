package com.controller;

import com.database.repository.AccountRepository;
import com.database.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class View {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public View(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public double viewBalanceOfAccountById(String accountID) throws SQLException {
        return accountRepository.getAccountBalance(accountID);
    }

    public ArrayList transactionsOfAccountById(String accountID) throws SQLException {
        return transactionRepository.transactionsOfAccountById(accountID);
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategoryAndDate(String accountID, String categoryName, String dateStart, String dateEnd) throws SQLException {
        return transactionRepository.transactionsOfAccountByIdFilteredByCategoryAndDate(accountID, categoryName, dateStart, dateEnd);
    }

    public ArrayList transactionsOfAccountByIdFilteredByCategory(String accountId, String categoryName) throws SQLException {
        return transactionRepository.transactionsOfAccountByIdFilteredByCategory(accountId, categoryName);
    }

    public ArrayList transactionsOfAccountByFilteredByDate(String accountID, String dateStart, String dateEnd) throws SQLException {
        return transactionRepository.transactionsOfAccountByIdFilteredByDate(accountID, dateStart, dateEnd);
    }

    public ArrayList transactionsOfAccountByFilteredByTypeAndOther(String accountID, String type, String category, String dateStart, String dateEnd) throws SQLException {
        return transactionRepository.transactionsOfAccountByIdFilteredByTypeAndOther(accountID, type, category, dateStart, dateEnd);
    }


}
