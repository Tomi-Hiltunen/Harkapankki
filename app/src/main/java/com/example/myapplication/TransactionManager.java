package com.example.myapplication;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<User> users;
    private Transaction transaction;
    private Context context;
    /*
    When new transactionManager is created, it needs to take the transaction to complete
    as a parameter in the constructor
     */
    public TransactionManager(Context context, Transaction transaction) {
        this.context = context;
        String JSONDataString = ReadAndWrite.read("data.json",context);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        this.users = new Gson().fromJson(JSONDataString, userListType);
        this.transaction = transaction;
    }

    /*
    Complete the transaction(withdrawal) on a account that is being send in as a parameter of this method
     */
    public boolean completeWithdrawal(Account account) {
        float balance = account.getBalance();

        if(account.getBalance() >= transaction.getAmount()) {
            for(User user : users) {
                for(Account acc : user.getAccounts()) {
                    if(acc.getAccountNumber() == account.getAccountNumber()) {
                        acc.setBalance(balance - transaction.getAmount());
                        List<Transaction> newList = account.getTransactions();
                        newList.add(transaction);
                        acc.setTransactions(newList);
                    }
                }
            }
            String json = new Gson().toJson(users);
            ReadAndWrite.write("data.json",json,context);
            return true;
        }
        return false;
    }
    /*
    Complete the transaction(deposit) on a account that is being send in as a parameter of this method
     */
    public void completeDeposit(Account account) {
        float balance = account.getBalance();

        for(User user : users) {
            for(Account acc : user.getAccounts()) {
                if(acc.getAccountNumber() == account.getAccountNumber()) {
                    acc.setBalance(balance + transaction.getAmount());
                    List<Transaction> newList = account.getTransactions();
                    newList.add(transaction);
                    acc.setTransactions(newList);
                }
            }
        }
        String json = new Gson().toJson(users);
        ReadAndWrite.write("data.json",json,context);
    }
    /*
    Complete the transaction(payment or transfer) on both sender and receiver.
    Sender needs to have enough balance and the amount cant exceed payment or transfer limits.
     */
    public int completePayment(String transactionType) {
        Account sender = transaction.getSender();
        Account receiver = transaction.getReceiver();
        float senderBalance = sender.getBalance();
        float receiverBalance = receiver.getBalance();
        float transactionAmount = transaction.getAmount();

        //If sender has enough money, make the transaction and save info to json file
        if(senderBalance >= transactionAmount) {
            if(sender.getMaxPayment() < transactionAmount && transactionType.equals("payment")) {
                return -1;
            }
            if(sender.getMaxTransfer() < transactionAmount && transactionType.equals("transfer")) {
                return -1;
            }
            for(User user : users) {
                for(Account account : user.getAccounts()) {
                    if(account.getAccountNumber() == transaction.getReceiver().getAccountNumber()) {
                        account.setBalance(receiverBalance+transactionAmount);
                        List<Transaction> newList = account.getTransactions();
                        newList.add(transaction);
                        account.setTransactions(newList);
                    } else if(account.getAccountNumber() == transaction.getSender().getAccountNumber()) {
                        account.setBalance(senderBalance-transactionAmount);
                        List<Transaction> newList = account.getTransactions();
                        newList.add(transaction);
                        account.setTransactions(newList);
                    }
                }
            }
            String json = new Gson().toJson(users);
            ReadAndWrite.write("data.json",json,context);
            return 1;
        }
        return 0;
    }
}
