package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {
    private EditText Address;
    private TextView curAddress;
    List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        Address = (EditText) findViewById(R.id.AddressEdit);
        curAddress = (TextView) findViewById(R.id.textView6);
        String JSONDataString = ReadAndWrite.read("data.json",UserSettingsActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        curAddress.setText("Osoite("+users.get(State.getLoggedInUserIndex()).getAddress()+"):");
    }

    public void CardsButtonOnClick(View view) {
        Intent intent = new Intent(this, CardsActivity.class);
        startActivity(intent);
    }

    public void NewPaymentButtonOnClick(View view) {
        Intent intent = new Intent(this,NewPaymentActivity.class);
        startActivity(intent);
    }

    public void NewTransferButtonOnClick(View view) {
        Intent intent = new Intent(this, MoneyTransferActivity.class);
        startActivity(intent);
    }

    public void CancelButtonOnClick(View view) {
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        Address.setText("");
        startActivity(intent);
    }

    public void HomeButtonOnClick(View view) {
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        startActivity(intent);
    }

    public void saveButtonOnClick(View view) {
        users.get(State.getLoggedInUserIndex()).setAddress(Address.getText().toString());
        curAddress.setText("Osoite("+Address.getText().toString()+"):");
        String json = new Gson().toJson(users);
        ReadAndWrite.write("data.json",json,UserSettingsActivity.this);
    }

    public void logOutButtonOnClick(View view) {
        State.setLoggedInUserIndex(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
