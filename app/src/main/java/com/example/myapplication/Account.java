package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private int accountNumber;
    private String typeOfAccount;
    private float balance;
    private List<Transaction> transactions;
    private int maxPayment;
    private int maxTransfer;

    //Constructors
    public Account(int accountNumber, float balance, String typeOfAccount, List<Transaction> transactions, int maxPayment, int maxTransfer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.typeOfAccount = typeOfAccount;
        this.transactions = transactions;
        this.maxPayment = maxPayment;
        this.maxTransfer = maxTransfer;
    }
    public Account(int accountNumber, float balance, String typeOfAccount) {
        this(accountNumber,balance,typeOfAccount, new ArrayList<Transaction>(), 5000, 5000);
    }


    //Getters and setters
    public int getAccountNumber() {
        return accountNumber;
    }

    public int getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(int maxPayment) {
        this.maxPayment = maxPayment;
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    public void setMaxTransfer(int maxTransfer) {
        this.maxTransfer = maxTransfer;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
