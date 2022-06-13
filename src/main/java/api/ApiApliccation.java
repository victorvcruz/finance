package api;

import api.request.*;
import api.response.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import controller.Change;
import controller.Create;
import com.google.gson.*;
import controller.View;
import database.repository.TransactionRepository;
import entities.Account;
import entities.Token;
import entities.Transaction;
import org.apache.log4j.BasicConfigurator;
import database.repository.AccountRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    private TransactionRepository transaction_repository;

    public ApiApliccation(Create create, Change change, View view, AccountRepository account_repository, TransactionRepository transaction_repository) {
        this.create = create;
        this.change = change;
        this.view = view;
        this.account_repository = account_repository;
        this.transaction_repository = transaction_repository;
    }

    public void run(){
        BasicConfigurator.configure();

        post("/accounts", (request, response) -> {
            response.type("application/json");
            AccountCreateRequest account_request = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            if(account_repository.existsAccountByUsername(account_request.getUsername())) {
                response.status(HTTP_CONFLICT);
                return Convert.json().toJson(new ErrorConflict());
            }

            create.account(account_request.getUsername(), account_request.getPassword());
            return Convert.json().toJson(account_repository.findById(account_repository.getAccountIdByUsername(account_request.getUsername())));
        });

        post("/accounts/auth", (request, response) -> {
            response.type("application/json");
            CreateTokenRequest token_request = new Gson().fromJson(request.body(), CreateTokenRequest.class);

            if(!account_repository.authenticateAccount(token_request.getUsername(), token_request.getPassword())){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorUnauthorized());
            }

            String token_create = create.token(token_request.getUsername());

            return Convert.json().toJson(new Token(token_create));
        });

        post("/transactions", (request, response) -> {
            String token = request.headers("token");

            response.type("application/json");
            TransactionsCreateRequest transaction_request = new Gson().fromJson(request.body(), TransactionsCreateRequest.class);

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            if(!this.dateIsValid(transaction_request.getDate())){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new ErrorInvalidDate());
            }

            String account_id = this.accountIdByToken(token);


            Transaction transaction = create.income(account_id, transaction_request.getDescription(), transaction_request.getCategory_name(), transaction_request.getDate(), transaction_request.getValue());
            return Convert.json().toJson(transaction);
        });

        get("/accounts", (request, response) -> {
            String token = request.headers("token");;

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            String account_id = this.accountIdByToken(token);

            Account account = account_repository.findById(account_id);
            account.setBalance(view.viewBalanceOfAccountById(account_id));
            return Convert.json().toJson(account);
        });

        get("/accounts/transactions", (request, response) -> {
            String token = request.headers("token");

            String type = request.queryParams("type");
            String category = request.queryParams("category");
            String date_start = request.queryParams("date_start");
            String date_end = request.queryParams("date_end");

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            String account_id = this.accountIdByToken(token);

            if(type != null){
                if(!this.typeIsValid(type)){
                    response.status(HTTP_BAD_REQUEST);
                    return Convert.json().toJson(new ErrorInvalidType());
                }
            }

            if(date_start != null & date_end == null){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new ErrorNotFoundDateEnd());
            }

            if(date_start == null & date_end != null){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new ErrorNotFoundDateStart());
            }

            if(date_start != null & date_end != null){

                if(!this.dateIsValid(date_start)){
                    response.status(HTTP_BAD_REQUEST);
                    return Convert.json().toJson(new ErrorInvalidDate());
                }
                if(!this.dateIsValid(date_end)){
                    response.status(HTTP_BAD_REQUEST);
                    return Convert.json().toJson(new ErrorInvalidDate());
                }

            }

            if(type != null){
                if(date_start != null & date_end != null & category != null){
                    return view.transactionsOfAccountByFilteredByTypeAndOther(account_id, type, category,
                            date_start, date_end);
                }

                if(date_start != null & date_end != null){
                    return view.transactionsOfAccountByFilteredByTypeAndOther(account_id, type, "",
                            date_start, date_end);
                }

                if(category != null){
                    return view.transactionsOfAccountByFilteredByTypeAndOther(account_id, type, category,
                            "", "");
                }

                return view.transactionsOfAccountByFilteredByTypeAndOther(account_id, type, "",
                        "", "");
            }

            if(date_start != null & date_end != null & category != null){
                return view.transactionsOfAccountByIdFilteredByCategoryAndDate(account_id, category,
                        date_start, date_end);
            }

            if(date_start != null & date_end != null){
                return view.transactionsOfAccountByFilteredByDate(account_id, date_start, date_end);
            }

            if(category != null){
                return view.transactionsOfAccountByIdFilteredByCategory(account_id, category);
            }

            return view.transactionsOfAccountById(account_id);
        });

        put("/accounts", (request, response) -> {
            String token = request.headers("token");


            response.type("application/json");
            AccountCreateRequest account_request = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            String account_id = this.accountIdByToken(token);

            if(account_request.getUsername() != null) {
                if(!account_repository.existsAccountByUsername(account_request.getUsername())) {
                    change.accountUsername(account_id, account_request.getUsername());
                } else{
                    response.status(HTTP_CONFLICT);
                    return Convert.json().toJson(new ErrorConflict());
                }
            }
            if(account_request.getPassword() != null) {
                change.accountPassword(account_id, account_request.getPassword());
            }
            Account account = account_repository.findById(account_id);
            account.setBalance(account_repository.getAccountBalance(account_id));
            return Convert.json().toJson(account);
        });

        put("/transactions", (request, response) -> {
            String token = request.headers("token");

            response.type("application/json");
            TransactionsChangeRequest transaction_request = new Gson().fromJson(request.body(), TransactionsChangeRequest.class);

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            if(!transaction_repository.existsTransactionById(transaction_request.getId())){
                response.status(HTTP_NOT_FOUND);
                return Convert.json().toJson(new ErrorNotFound());
            }

            change.transaction(transaction_request.getId(),transaction_request.getDescription(), transaction_request.getDate(), transaction_request.getValue());

            return Convert.json().toJson(transaction_repository.findById(transaction_request.getId()));
        });

        delete("/transactions", (request, response) -> {
            String token = request.headers("token");

            response.type("application/json");
            TransactionsCancelRequest transaction_request = new Gson().fromJson(request.body(), TransactionsCancelRequest.class);

            if(!this.tokenIsAuthorized(token)){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new ErrorInvalidToken());
            }

            if(!transaction_repository.existsTransactionById(transaction_request.getId())){
                response.status(HTTP_NOT_FOUND);
                return Convert.json().toJson(new ErrorNotFound());
            }

            change.cancelTransaction(transaction_request.getId(), "true");

            return Convert.json().toJson(transaction_repository.findById(transaction_request.getId()));
        });

    }

    public boolean tokenIsAuthorized(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;

        } catch (JWTVerificationException exception){
            return false;
        }
    }

    public String accountIdByToken(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getIssuer();
    }

    public boolean dateIsValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean typeIsValid(String type){
        return type.equals("incomes") || type.equals("expenses");
    }


}

