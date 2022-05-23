import Api.ApiApliccation;
import Apliccation.Change;
import Apliccation.Create;
import com.google.gson.Gson;
import database.DbValidate;
import database.PostgreClient;
import repository_entities.AccountRepository;
import repository_entities.CategoryRepository;
import repository_entities.TransactionRepository;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        PostgreClient postgresql = new PostgreClient();

        AccountRepository account_repository = new AccountRepository(postgresql);
        CategoryRepository category_repository = new CategoryRepository(postgresql);
        TransactionRepository transaction_repository = new TransactionRepository(postgresql);

        DbValidate db_validate = new DbValidate(account_repository, category_repository);

        Create create = new Create(account_repository, category_repository, transaction_repository, db_validate);
        Change change = new Change(account_repository, category_repository, transaction_repository);

        ApiApliccation api = new ApiApliccation(create, account_repository, db_validate);

        api.run();




//        create.createAccount("Paulo", "2340");

//        create.createIncome("Fernando", "0014", "Segundo Salário Estágio", "Salário", 600);
//        change.changeTransaction("b74d7d2d-521c-489d-91c1-e14add702e66", "Salário", "2022/05/02",1400);
//        change.cancelTransaction("105ee9b9-fc42-48c1-bbbf-b095dd55d65c", "true");
//        change.changeAccount("15b2c7c3-ed09-4981-961b-86e7211cfb51", "Péricles", "6666");


    }
}