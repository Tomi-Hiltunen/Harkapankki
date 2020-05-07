package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddMoneyActivity extends AppCompatActivity {
    private Spinner spinner;
    private User user;
    TextView Error, Success;
    String JSONDataString;
    List<User> users;
    Account account;
    EditText WithdrawAmountText;
    EditText DepositAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        Error = (TextView) findViewById(R.id.AddMoneyErrorMessage);
        Success = (TextView) findViewById(R.id.AddMoneySuccessMessage);
        WithdrawAmountText = (EditText) findViewById(R.id.WithdrawSum);
        DepositAmountText = (EditText) findViewById(R.id.DepositSum);
        JSONDataString = ReadAndWrite.read("data.json",AddMoneyActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());


        addItemsOnSpinner();

    }

    public void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner4);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            list.add(user.getAccounts().get(i).getAccountNumber());
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void DepositButtonOnClick(View view) {
        float DepositAmount;
        setAccount();
        // After checking everything is OK, Deposit sends information to Transaction Manager with Account (from SetAccount) and amount of money.
        if (DepositAmountText.getText().toString().equals("")) {
            Error.setText("Tarkista talletusmäärä");
            Success.setText("");
        } else {
            DepositAmount = Float.valueOf(DepositAmountText.getText().toString());
            Transaction transaction = new Transaction("Talletus",DepositAmount);
            TransactionManager transactionManager = new TransactionManager(AddMoneyActivity.this,transaction);
            transactionManager.completeDeposit(account);
            State.setErrorMsg("Talletus onnistui!");
            openDialog();
        }
    }

    public void WithdrawButtonOnClick(View view) {
        float WithdrawAmount;
        setAccount();
        // After checking everything is OK, Withdrawing sends information to Transaction Manager with Account (from SetAccount) and amount of money.
        if (WithdrawAmountText.getText().toString().equals("")) {
            Error.setText("Tarkista talletusmäärä");
            Success.setText("");
        } else {
            WithdrawAmount = Float.valueOf(WithdrawAmountText.getText().toString());
            Transaction transaction = new Transaction("Nosto",WithdrawAmount);
            TransactionManager transactionManager = new TransactionManager(AddMoneyActivity.this,transaction);
            if(transactionManager.completeWithdrawal(account)) {
                State.setErrorMsg("Nosto onnistui!");
                openDialog();
            } else {
                Error.setText("Tililläsi ei ole tarpeeksi rahaa");
                Success.setText("");
            }
        }
    }

    public void setAccount() {
        int accountNumber = Integer.valueOf(spinner.getSelectedItem().toString());
        for(User user : users) {
            for (Account acc : user.getAccounts()) {
                if(accountNumber == acc.getAccountNumber()) {
                    account = acc;
                }
            }
        }
    }

    public void openDialog() {
        DialogInfo dialogInfo  = new DialogInfo();
        dialogInfo.show(getSupportFragmentManager(), "info dialog");
    }

    public void payNavButtonOnClick(View view) {
        Intent intent = new Intent(this,NewPaymentActivity.class);
        startActivity(intent);
    }

    public void MoneyTransferButtonOnClick(View view) {
        Intent intent = new Intent(this, MoneyTransferActivity.class);
        startActivity(intent);
    }

    public void CardsButtonOnClick(View view) {
        Intent intent = new Intent(this, CardsActivity.class);
        startActivity(intent);
    }

    public void HomeButtonOnClick(View view) {
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        startActivity(intent);
    }

    public void logOutButtonOnClick(View view) {
        State.setLoggedInUserIndex(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
