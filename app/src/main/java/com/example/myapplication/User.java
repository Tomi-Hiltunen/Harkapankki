package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String address;
    private List<Account> accounts;
    private List<Card> cards;

    //Constructors(User has atleast username,pass,user holders real name, and address)
    public User(String username, String password, String name, String address, List<Account> accounts,List<Card> cards) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.accounts = accounts;
        this.cards = cards;
    }
    public User(String username, String password, String name, String address, List<Account> accounts) {
        this(username,password,name,address,accounts, new ArrayList<Card>());
    }
    public User(String username, String password, String name, String address) {
        this(username,password,name,address,new ArrayList<Account>());
    }

    //Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
