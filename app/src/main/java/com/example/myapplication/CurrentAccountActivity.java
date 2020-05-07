package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CurrentAccountActivity extends AppCompatActivity {
    private User user;
    private Spinner spinner;
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_account);
        spinner = (Spinner) findViewById(R.id.spinner);

        //Read data and turn it into java object
        String JSONDataString = ReadAndWrite.read("data.json",CurrentAccountActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());

        setTextViewData();
        addItemsOnSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int selectedSpinnerItem = Integer.valueOf(spinner.getSelectedItem().toString());
                for (int i = 0; i < user.getAccounts().size(); i++) {
                    if (user.getAccounts().get(i).getAccountNumber() == selectedSpinnerItem) {
                        State.setCurAccIndex(i);
                    }
                }
                setTextViewData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public void addItemsOnSpinner() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            list.add(user.getAccounts().get(i).getAccountNumber());
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(State.getCurAccIndex());
    }


    public void setTextViewData() {
        TextView balanceText = (TextView) findViewById(R.id.curAccBalanceTextview);
        balanceText.setText(String.valueOf(user.getAccounts().get(State.getCurAccIndex()).getBalance()));

        TextView AccountType = (TextView) findViewById(R.id.curAccTextview);
        AccountType.setText(user.getAccounts().get(State.getCurAccIndex()).getTypeOfAccount());

        List<Transaction> transactionList = user.getAccounts().get(State.getCurAccIndex()).getTransactions();

        //Add all transactions to the textview
        String payments = "";
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equals("Maksu")) {
                if (transaction.getSender().getAccountNumber() == user.getAccounts().get(State.getCurAccIndex()).getAccountNumber()) {
                    payments = "\n-"+String.valueOf(transaction.getAmount())+"€          vastaanottaja: "+ transaction.getReceiver().getAccountNumber() + payments;
                } else {
                    payments = "\n+" + String.valueOf(transaction.getAmount()) + "€          lähettäjä: " + transaction.getSender().getAccountNumber() + payments;
                }
            } else if (transaction.getType().equals("Nosto")) {
                payments = "\n-" + String.valueOf(transaction.getAmount()) + "€           Nosto"+ payments;
            } else if (transaction.getType().equals("Talletus")) {
                payments = "\n+" + String.valueOf(transaction.getAmount()) + "€           Talletus"+ payments;
            }
        }
        TextView paymentHistoryTextview = (TextView) findViewById(R.id.cardsTextView);
        paymentHistoryTextview.setText(payments);
    }
    public void CardsButtonOnClick(View view) {
        Intent intent = new Intent(this, CardsActivity.class);
        startActivity(intent);
    }

    public void payNavButtonOnClick(View view) {
        Intent intent = new Intent(this,NewPaymentActivity.class);
        startActivity(intent);
    }

    public void MoneyTransferButtonOnClick(View view) {
        Intent intent = new Intent(this, MoneyTransferActivity.class);
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
