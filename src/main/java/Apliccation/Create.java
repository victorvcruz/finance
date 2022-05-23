package Apliccation;

import com.google.gson.Gson;
import database.DbValidate;
import database.PostgreClient;
import entities.Account;
import entities.Category;
import entities.Transaction;
import repository_entities.AccountRepository;
import repository_entities.CategoryRepository;
import repository_entities.TransactionRepository;

import java.sql.SQLException;

public class Create {

    public AccountRepository account_repository;
    public CategoryRepository category_repository;
    public TransactionRepository transaction_repository;
    public DbValidate db_validate;

    public Create(AccountRepository account_repository, CategoryRepository category_repository, TransactionRepository transaction_repository, DbValidate db_validate) throws SQLException, ClassNotFoundException {
        this.account_repository = account_repository;
        this.category_repository = category_repository;
        this.transaction_repository = transaction_repository;
        this.db_validate = db_validate;

    }



    public void createAccount(String username, String password) throws SQLException {
        Account account = new Account(username, password);
        if(!db_validate.accountNameValidate(username)){
            account_repository.InsertDbAccount(account);
        }
    }

    public void createIncome(String account_name, String password, String description, String category_name, double value) throws SQLException {
        if(db_validate.authenticateAccount(account_name, password)){
            Account account = db_validate.createAccountValidate(account_name);

            Category category = new Category(category_name, account);

            if (!db_validate.categoryValidate(category_name, account_repository.getAccountId(account_name))){
                category_repository.InsertDbCategory(category);
            }

            Transaction transaction = new Transaction(account, category, description, value);
            transaction_repository.InsertDbTransaction(transaction);
            System.out.println("Transaction created your id is: " + transaction.getId());
        } else{
            System.out.println("Incorrect username or password ");
        }
    }


}
