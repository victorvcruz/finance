package com;

import com.api.ApiApliccation;
import com.controller.Change;
import com.controller.Create;
import com.controller.View;
import com.database.PostgreClient;
import com.database.repository.AccountRepository;
import com.database.repository.CategoryRepository;
import com.database.repository.TransactionRepository;
import java.sql.SQLException;



public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        PostgreClient postgresql = new PostgreClient();

        AccountRepository account_repository = new AccountRepository(postgresql);
        CategoryRepository category_repository = new CategoryRepository(postgresql, account_repository);
        TransactionRepository transaction_repository = new TransactionRepository(postgresql, account_repository, category_repository);

        Create create = new Create(account_repository, category_repository, transaction_repository);
        Change change = new Change(account_repository, category_repository, transaction_repository);
        View view = new View(account_repository, transaction_repository);

        ApiApliccation api = new ApiApliccation(create, change, view, account_repository, transaction_repository);

        api.run();

    }

}