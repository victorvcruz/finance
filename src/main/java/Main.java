import api.ApiApliccation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.Change;
import controller.Create;
import controller.View;
import database.PostgreClient;
import database.repository.AccountRepository;
import database.repository.CategoryRepository;
import database.repository.TransactionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        PostgreClient postgresql = new PostgreClient();

        AccountRepository account_repository = new AccountRepository(postgresql);
        CategoryRepository category_repository = new CategoryRepository(postgresql, account_repository);
        TransactionRepository transaction_repository = new TransactionRepository(postgresql, account_repository, category_repository);

        Create create = new Create(account_repository, category_repository, transaction_repository);
        Change change = new Change(account_repository, category_repository, transaction_repository);
        View view = new View(account_repository, transaction_repository);

        ApiApliccation api = new ApiApliccation(create, change, view, account_repository, category_repository, transaction_repository);

        api.run();
    }
}