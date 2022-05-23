package Api;

import Apliccation.Create;
import com.google.gson.*;
import database.DbValidate;
import entities.Account;
import org.apache.log4j.BasicConfigurator;
import repository_entities.AccountRepository;

import static spark.Spark.post;
import static spark.Spark.get;


public class ApiApliccation {
    private Create create;
    private AccountRepository account_repository;
    private DbValidate db_validate;

    public ApiApliccation(Create create, AccountRepository account_repository, DbValidate db_validate) {
        this.create = create;
        this.account_repository = account_repository;
        this.db_validate = db_validate;
    }

    public void run(){
        BasicConfigurator.configure();

        post("/accounts", (request, response) -> {
            response.type("application/json");
            AccountCreateRequest account_request = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            create.createAccount(account_request.getUsername(), account_request.getPassword());

            return account_request.getPassword();
        });

        post("/transactions/:id", (request, response) -> {
            String username = request.params(":id");
            String password = request.headers("Password");

            response.type("application/json");
            TransactionsCreateRequest transaction_request = new Gson().fromJson(request.body(), TransactionsCreateRequest.class);

            create.createIncome(username, password, transaction_request.getDescription(), transaction_request.getCategory_name(), transaction_request.getValue());

            return password;
        });

        get("/:id", (request, response) -> {
            String username = request.params(":id");
            String password = request.headers("Password");

            if(db_validate.authenticateAccount(username, password)){
                Account account = db_validate.createAccountValidate(username);
                account.setBalance(account_repository.getAccountBalance(account_repository.getAccountId(username)));
                return Convert.json().toJson(account);
            }

            return 0;
        });
    }
}

