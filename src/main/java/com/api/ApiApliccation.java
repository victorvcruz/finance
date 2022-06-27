package com.api;

import com.api.request.*;
import com.api.request.Exceptions.ExceptionDateInvalid;
import com.api.request.Exceptions.ExceptionTokenUnauthorized;
import com.api.request.Exceptions.ExceptionTypeInvalid;
import com.api.response.Error;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.controller.Change;
import com.controller.Create;
import com.google.gson.*;
import com.controller.View;
import com.database.repository.TransactionRepository;
import com.entities.Account;
import com.entities.Token;
import com.entities.Transaction;
import org.apache.log4j.BasicConfigurator;
import com.database.repository.AccountRepository;

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
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public ApiApliccation(Create create, Change change, View view, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.create = create;
        this.change = change;
        this.view = view;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void run(){
        BasicConfigurator.configure();

        post("/accounts", (request, response) -> {

            Thread.sleep(5000);


            response.type("application/json");
            AccountCreateRequest accountRequest = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            if(accountRepository.existsAccountByUsername(accountRequest.getUsername())) {
                response.status(HTTP_CONFLICT);
                return Convert.json().toJson(new Error("This user already exists"));
            }

            Account account = create.account(accountRequest.getUsername(), accountRequest.getPassword());
            return Convert.json().toJson(account);
        });

        post("/accounts/auth", (request, response) -> {
            response.type("application/json");
            CreateTokenRequest tokenRequest = new Gson().fromJson(request.body(), CreateTokenRequest.class);

            if(!accountRepository.authenticateAccount(tokenRequest.getUsername(), tokenRequest.getPassword())){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Access not allowed"));
            }

            String tokenCreate = create.token(tokenRequest.getUsername());

            return Convert.json().toJson(new Token(tokenCreate));
        });

        post("/transactions", (request, response) -> {

            String token = request.headers("token");

            Thread.sleep(5000);


            response.type("application/json");
            TransactionsCreateRequest transactionRequest = new Gson().fromJson(request.body(), TransactionsCreateRequest.class);

            try {
                String accountID = this.accountIdByToken(token);
                this.dateIsValid(transactionRequest.getDate());

                Transaction transaction = create.income(accountID, transactionRequest.getDescription(), transactionRequest.getCategory_name(), transactionRequest.getDate(), transactionRequest.getValue());
                return Convert.json().toJson(transaction);

            } catch (ExceptionTokenUnauthorized except){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            } catch (ExceptionDateInvalid except){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new Error("Date invalid, insert 'YYYY/MM/DD'"));
            }


        });

        get("/accounts", (request, response) -> {
            String token = request.headers("token");

            Thread.sleep(5000);


            try{
                String accountID = this.accountIdByToken(token);

                Account account = accountRepository.findById(accountID);
                account.setBalance(view.viewBalanceOfAccountById(accountID));
                return Convert.json().toJson(account);

            } catch (ExceptionTokenUnauthorized expect){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            }

        });

        get("/accounts/transactions", (request, response) -> {
            String token = request.headers("token");

            String type = request.queryParams("type");
            String category = request.queryParams("category");
            String dateStart = request.queryParams("date_start");
            String dateEnd = request.queryParams("date_end");

            Thread.sleep(5000);


            try{
                String accountID = this.accountIdByToken(token);

                if(type != null) {
                    this.typeIsValid(type);
                }

                if(dateStart != null & dateEnd == null){
                    response.status(HTTP_BAD_REQUEST);
                    return Convert.json().toJson(new Error("Insert date_end"));
                }

                if(dateStart == null & dateEnd != null){
                    response.status(HTTP_BAD_REQUEST);
                    return Convert.json().toJson(new Error("Insert date_start"));
                }

                if(dateStart != null & dateEnd != null){
                    this.dateIsValid(dateStart);
                    this.dateIsValid(dateEnd);
                }

                if(type != null){
                    if(dateStart != null & dateEnd != null & category != null){
                        return view.transactionsOfAccountByFilteredByTypeAndOther(accountID, type, category,
                                dateStart, dateEnd);
                    }

                    if(dateStart != null & dateEnd != null){
                        return view.transactionsOfAccountByFilteredByTypeAndOther(accountID, type, "",
                                dateStart, dateEnd);
                    }

                    if(category != null){
                        return view.transactionsOfAccountByFilteredByTypeAndOther(accountID, type, category,
                                "", "");
                    }

                    return view.transactionsOfAccountByFilteredByTypeAndOther(accountID, type, "",
                            "", "");
                }

                if(dateStart != null & dateEnd != null & category != null){
                    return view.transactionsOfAccountByIdFilteredByCategoryAndDate(accountID, category,
                            dateStart, dateEnd);
                }

                if(dateStart != null & dateEnd != null){
                    return view.transactionsOfAccountByFilteredByDate(accountID, dateStart, dateEnd);
                }

                if(category != null){
                    return view.transactionsOfAccountByIdFilteredByCategory(accountID, category);
                }

                return view.transactionsOfAccountById(accountID);

            } catch (ExceptionTokenUnauthorized expect){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            } catch (ExceptionTypeInvalid expect){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new Error("Type invalid, insert 'incomes' or 'expenses'"));
            } catch (ExceptionDateInvalid expect){
                response.status(HTTP_BAD_REQUEST);
                return Convert.json().toJson(new Error("Date invalid, insert 'YYYY/MM/DD'"));
            }

        });

        put("/accounts", (request, response) -> {
            String token = request.headers("token");

            Thread.sleep(5000);


            response.type("application/json");
            AccountCreateRequest accountRequest = new Gson().fromJson(request.body(), AccountCreateRequest.class);

            try{
                String accountID = this.accountIdByToken(token);

                if(accountRequest.getUsername() != null) {
                    if(!accountRepository.existsAccountByUsername(accountRequest.getUsername())) {
                        change.accountUsername(accountID, accountRequest.getUsername());
                    } else{
                        response.status(HTTP_CONFLICT);
                        return Convert.json().toJson(new Error("This user already exists"));
                    }
                }
                if(accountRequest.getPassword() != null) {
                    change.accountPassword(accountID, accountRequest.getPassword());
                }
                Account account = accountRepository.findById(accountID);
                account.setBalance(accountRepository.getAccountBalance(accountID));
                return Convert.json().toJson(account);

            } catch (ExceptionTokenUnauthorized expect){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            }

        });

        put("/transactions", (request, response) -> {
            String token = request.headers("token");

            Thread.sleep(5000);


            response.type("application/json");
            TransactionsChangeRequest transactionRequest = new Gson().fromJson(request.body(), TransactionsChangeRequest.class);

            try{
                this.accountIdByToken(token);

                if(!transactionRepository.existsTransactionById(transactionRequest.getId())){
                    response.status(HTTP_NOT_FOUND);
                    return Convert.json().toJson(new Error("Transactions id not found"));
                }

                change.transaction(transactionRequest.getId(),transactionRequest.getDescription(), transactionRequest.getDate(), transactionRequest.getValue());

                return Convert.json().toJson(transactionRepository.findById(transactionRequest.getId()));

            } catch (ExceptionTokenUnauthorized expect){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            }



        });

        delete("/transactions", (request, response) -> {
            String token = request.headers("token");

            Thread.sleep(5000);


            response.type("application/json");
            TransactionsCancelRequest transactionRequest = new Gson().fromJson(request.body(), TransactionsCancelRequest.class);

            try{
                this.accountIdByToken(token);


                if(!transactionRepository.existsTransactionById(transactionRequest.getId())){
                    response.status(HTTP_NOT_FOUND);
                    return Convert.json().toJson(new Error("Transactions id not found"));
                }

                Transaction transaction = transactionRepository.findById(transactionRequest.getId());

                change.cancelTransaction(transactionRequest.getId(), "true");

                return Convert.json().toJson(transaction);

            } catch (ExceptionTokenUnauthorized expect){
                response.status(HTTP_UNAUTHORIZED);
                return Convert.json().toJson(new Error("Token invalid"));
            }

        });

    }

    public String accountIdByToken(String token) throws ExceptionTokenUnauthorized {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getIssuer();
        } catch (JWTVerificationException exception){
            throw new ExceptionTokenUnauthorized();
        }
    }

    public void dateIsValid(String dateStr) throws ExceptionDateInvalid {
        DateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
             throw new ExceptionDateInvalid();
        }
    }

    public void typeIsValid(String type) throws ExceptionTypeInvalid {
         if(!(type.equals("incomes") || type.equals("expenses"))){
             throw new ExceptionTypeInvalid();
         }
    }


}

