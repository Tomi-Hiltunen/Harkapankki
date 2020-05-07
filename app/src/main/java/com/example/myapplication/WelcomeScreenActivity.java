package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WelcomeScreenActivity extends AppCompatActivity {
    private List<User> users;
    private int userIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userIndex = State.getLoggedInUserIndex();
        //Read data file and make object list out of it
        String JSONDataString = ReadAndWrite.read("data.json",WelcomeScreenActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);

        Button button = (Button) findViewById(R.id.currentAccount);
        if(users.get(userIndex).getAccounts().size() > 0) {
            button.setText(users.get(userIndex).getAccounts().get(State.getCurAccIndex()).getTypeOfAccount()+"("+users.get(userIndex).getAccounts().get(State.getCurAccIndex()).getAccountNumber()+")                   "+String.valueOf(users.get(userIndex).getAccounts().get(State.getCurAccIndex()).getBalance())+"€");
        }
    }

    public void currentAccountButtonOnClick(View view) {
        Intent intent;
        //If user don't have any accounts redirect user to make account activity, otherwise show accounts history
        if(users.get(State.getLoggedInUserIndex()).getAccounts().size() < 1) {
            intent = new Intent(this,AddAccountActivity.class);
        } else {
            intent = new Intent(this,CurrentAccountActivity.class);
        }
        startActivity(intent);
    }

    public void payNavButtonOnClick(View view) {
        Intent intent = new Intent(this,NewPaymentActivity.class);
        startActivity(intent);
    }

    public void addAccountButtonOnClick(View view) {
        Intent intent = new Intent(this,AddAccountActivity.class);
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

    public void SettingsOnClick(View view) {
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
    }

    public void AddMoneyButtonOnClick(View view) {
        Intent intent = new Intent(this, AddMoneyActivity.class);
        startActivity(intent);
    }
    public void ChangeAccountsButtonOnClick(View view) {
        Intent intent = new Intent(this, AccountSettingsActivity.class);
        startActivity(intent);
    }
    public void logOutButtonOnClick(View view) {
        State.setLoggedInUserIndex(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
