package com.example.myapplication;

public class Transaction {
    private String type;
    private Account receiver;
    private Account sender;
    private float amount;

    public Transaction(String type, float amount, Account receiver, Account sender) {
        this.type = type;
        this.receiver = receiver;
        this.sender = sender;
        this.amount = amount;
    }

    public Transaction(String type, float amount) {
        this(type, amount, null, null);
    }

    //Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
