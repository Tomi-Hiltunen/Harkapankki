package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddCardActivity2 extends AppCompatActivity {
    private Spinner spinner;
    private User user;
    private List<User> users;
    private RadioButton radioButton;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card2);
        spinner = (Spinner) findViewById(R.id.fromAccount_Spinner);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        String JSONDataString = ReadAndWrite.read("data.json",AddCardActivity2.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());
        addItemsOnSpinner();
    }

    public void addItemsOnSpinner() {
        List<Integer> listFromAccounts = new ArrayList<Integer>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            if(user.getAccounts().get(i).getTypeOfAccount().equals("Käyttötili")) {
                listFromAccounts.add(user.getAccounts().get(i).getAccountNumber());
            }
        }
        ArrayAdapter<Integer> dataAdapter1 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, listFromAccounts);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter1);
    }

    public void saveButtonOnClick(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        int accountNumber = Integer.valueOf(spinner.getSelectedItem().toString());
        String cardType = radioButton.getText().toString();
        int cardNumber = generateRandomCardNumber();

        Card card = new Card(cardType,cardNumber,accountNumber);
        List<Card> newCardList = users.get(State.getLoggedInUserIndex()).getCards();
        newCardList.add(card);
        users.get(State.getLoggedInUserIndex()).setCards(newCardList);
        String json = new Gson().toJson(users);
        ReadAndWrite.write("data.json",json,AddCardActivity2.this);
    }

    public int generateRandomCardNumber() {
        Random rand = new Random(System.currentTimeMillis());
        int cardNumber = rand.nextInt(99999999-10000000) + 10000000;
        for (User user : users) {
            for(Card card : user.getCards()) {
                if(card.getCardNumber() == cardNumber) {
                    //Generate new card number if its already used
                    cardNumber = generateRandomCardNumber();
                }
            }
        }
        return cardNumber;
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
