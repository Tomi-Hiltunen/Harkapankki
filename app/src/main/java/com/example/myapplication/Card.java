package com.example.myapplication;

public class Card {
    private String cardType;
    private int cardNumber;
    private int accountNumber;

    public Card(String cardType, int cardNumber, int accountNumber) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
