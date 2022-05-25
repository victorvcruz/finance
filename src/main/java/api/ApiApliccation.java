package api;

import api.request.AccountCreateRequest;
import api.request.TransactionsCancelRequest;
import api.request.TransactionsChangeRequest;
import api.request.TransactionsCreateRequest;
import api.response.ErrorConflict;
import api.response.ErrorNotFound;
import controller.Change;
import controller.Create;
import com.google.gson.*;
import controller.View;
import database.repository.CategoryRepository;
import database.repository.TransactionRepository;
import entities.Account;
import entities.Transaction;
import org.apache.log4j.BasicConfigurator;
import database.repository.AccountRepository;
import api.response.ErrorUnauthorized;

import static java.net.HttpURLConnection.*;
import static spark.Spark.post;
import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.delete;


public class ApiApliccation {
    private Create create;
    private Change change;
    private View view;
    private AccountRepository account_repository;
    private CategoryRepository category_repository;
    private TransactionRepository transaction_repository;

    public ApiApliccation(Create create, Change change, View view, AccountRepository account_repository, CategoryRepository category_repository, TransactionRepository transaction_repository) {
        this.create = create;
        this.change = change;
        this.view = view;
        this.account_repository = account_repository;
        this.category_repository = category_repository;
        this.transaction_repository = transaction_repository;
    }

    public void run(){
        BasicConfigurator.configure();

        post("/accounts", (request, response) -> {
            response.type("application/json");
            AccountCreateRequest account_request = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            if(!account_repository.existsAccountByUsername(account_request.getUsername())) {
                response.status(HTTP_CONFLICT);
                return Convert.json().toJson(new ErrorConflict());
            }

            create.createAccount(account_request.getUsername(), account_request.getPassword());
            return Convert.json().toJson(account_repository.findById(account_repository.getAccountIdByUsername(account_request.getUsername())));
        });

        post("/transactions/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            response.type("application/json");
            TransactionsCreateRequest transaction_request = new Gson().fromJson(request.body(), TransactionsCreateRequest.class);

            if(account_repository.authenticateAccount(account_id, password)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }

            Transaction transaction = create.createIncome(account_id, password, transaction_request.getDescription(), transaction_request.getCategory_name(), transaction_request.getDate(), transaction_request.getValue());
            return Convert.json().toJson(transaction);
        });

        get("/accounts/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            if(account_repository.authenticateAccount(account_id, password)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }

            Account account = account_repository.findById(account_id);
            account.setBalance(view.viewBalanceOfAccountById(account_id));
            return Convert.json().toJson(account);
        });

        get("/transactions/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            if(account_repository.authenticateAccount(account_id, password)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }

            return view.viewTransactionsOfAccountById(account_id);
        });

        put("/accounts/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            response.type("application/json");
            AccountCreateRequest account_request = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            if(!account_repository.authenticateAccount(account_id, password)) {
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }

            if(account_request.getUsername() != null) {
                if(!account_repository.existsAccountByUsername(account_request.getUsername())) {
                    change.changeAccountUsername(account_id, account_request.getUsername());
                } else{
                    response.status(HTTP_CONFLICT);
                    return Convert.json().toJson(new ErrorConflict());
                }
            }
            if(account_request.getPassword() != null) {
                change.changeAccountPassword(account_id, account_request.getPassword());
            }
            Account account = account_repository.findById(account_id);
            account.setBalance(account_repository.getAccountBalance(account_id));
            return Convert.json().toJson(account);
        });

        put("/transactions/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            response.type("application/json");
            TransactionsChangeRequest transaction_request = new Gson().fromJson(request.body(), TransactionsChangeRequest.class);

            if(!account_repository.authenticateAccount(account_id, password)) {
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }
            if(!transaction_repository.existsTransactionById(transaction_request.getId())){
                response.status(HTTP_NOT_FOUND);
                return Convert.json().toJson(new ErrorNotFound());
            }

            change.changeTransaction(transaction_request.getId(),transaction_request.getDescription(), transaction_request.getDate(), transaction_request.getValue());

            return Convert.json().toJson(transaction_repository.findById(transaction_request.getId()));
        });

        delete("/transactions/:id", (request, response) -> {
            String account_id = request.params(":id");
            String password = request.headers("Password");

            response.type("application/json");
            TransactionsCancelRequest transaction_request = new Gson().fromJson(request.body(), TransactionsCancelRequest.class);

            if(!account_repository.authenticateAccount(account_id, password)) {
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }
            if(!transaction_repository.existsTransactionById(transaction_request.getId())){
                response.status(HTTP_NOT_FOUND);
                return Convert.json().toJson(new ErrorNotFound());
            }

            change.cancelTransaction(transaction_request.getId(), "true");

            return Convert.json().toJson(transaction_repository.findById(transaction_request.getId()));
        });

    }
}

