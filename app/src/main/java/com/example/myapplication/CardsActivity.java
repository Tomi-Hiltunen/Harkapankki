package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardsActivity extends AppCompatActivity {
    private User user;
    private List<User> users;
    private TextView cardsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String JSONDataString = ReadAndWrite.read("data.json",CardsActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());

        if (user.getCards().size() > 0 ) {
            setCardsTextView();
        }
    }

    public void setCardsTextView() {
        TextView cardsTextView = (TextView) findViewById(R.id.cardsTextView);
        String cards = "";
        for (Card card : user.getCards()) {
            cards = card.getCardType()+"("+card.getCardNumber()+")\n"+"Tilinumero: "+card.getAccountNumber()+"\n\n"+cards;
        }
        cardsTextView.setText(cards);
    }

    public void NewCardOnClick(View view) {
        Intent intent = new Intent(this,AddCardActivity2.class);
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
