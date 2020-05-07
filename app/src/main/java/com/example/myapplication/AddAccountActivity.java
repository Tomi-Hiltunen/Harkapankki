package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddAccountActivity extends AppCompatActivity {
    private List<User> users;
    private TextView infoTextView;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String JSONDataString = ReadAndWrite.read("data.json",AddAccountActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
    }

    public void saveButtonOnClick(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        int newAccNumber = generateRandomAccountNumber();
        Account newAccount = new Account(newAccNumber,50000,radioButton.getText().toString());
        List<Account> newAccountsList = users.get(State.getLoggedInUserIndex()).getAccounts();
        newAccountsList.add(newAccount);
        users.get(State.getLoggedInUserIndex()).setAccounts(newAccountsList);
        String json = new Gson().toJson(users);
        ReadAndWrite.write("data.json",json,AddAccountActivity.this);
        infoTextView.setText(radioButton.getText().toString()+"("+newAccNumber+") luotu!");
    }

    public int generateRandomAccountNumber() {
        Random rand = new Random(System.currentTimeMillis());
        int accountNumber = rand.nextInt(99999999-10000000) + 10000000;
        for (User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getAccountNumber() == accountNumber) {
                    //Generate new account number if its already used
                    accountNumber = generateRandomAccountNumber();
                }
            }
        }
        return accountNumber;
    }

    public void NewPaymentButtonOnClick(View view) {
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

    public void CancelButtonOnClick(View view) {
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
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
