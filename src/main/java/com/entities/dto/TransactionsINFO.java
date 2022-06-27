package com.entities.dto;

import com.entities.Transaction;

public class TransactionsINFO {

    private int numberOfTransactions;
    private double allValue;

    public TransactionsINFO(int numberOfTransactions, double allValue){
        this.numberOfTransactions = numberOfTransactions;
        this.allValue = allValue;
    }
}
