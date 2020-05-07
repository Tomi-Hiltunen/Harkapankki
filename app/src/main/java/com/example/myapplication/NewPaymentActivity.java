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

public class NewPaymentActivity extends AppCompatActivity {
    private Spinner spinner;
    private List<User> users;
    private User user;
    private String JSONDataString;
    private EditText receiverText;
    private EditText amountText;
    private TextView PayError;
    private TextView SuccessMessage;
    private Account receiver;
    private Account sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        spinner = (Spinner) findViewById(R.id.spinner2);
        receiverText = (EditText) findViewById(R.id.receiverEditText);
        amountText = (EditText) findViewById(R.id.amountEditText);
        PayError = (TextView) findViewById(R.id.PayError);
        SuccessMessage = (TextView) findViewById(R.id.PaySuccessMessage);

        //Make user object form data file
        JSONDataString = ReadAndWrite.read("data.json",NewPaymentActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());



        addItemsOnSpinner();

    }

    public void continueButtonOnClick(View view) {
        float amount;
        int receiverAccNumber;
        setSender();

        //Get amount from user input
        if (amountText.getText().toString().equals("")) {
            amount = 0;
        } else {
            amount = Float.valueOf(amountText.getText().toString());
        }
        //Get receiver from user input
        if (receiverText.getText().toString().equals("")) {
            receiverAccNumber = -1;
        } else {
            receiverAccNumber = Integer.valueOf(receiverText.getText().toString());
        }
        //Complete the transaction with error handling
        if(setReceiver(receiverAccNumber)) {
            if(amount > 0) {
                Transaction transaction = new Transaction("Maksu",amount,receiver,sender);
                TransactionManager transactionManager = new TransactionManager(NewPaymentActivity.this,transaction);
                switch (transactionManager.completePayment("payment")) {
                    case 1:
                        State.setErrorMsg("Maksu suoritettu!");
                        openDialog();
                        break;
                    case -1:
                        PayError.setText("Tilin " + sender.getAccountNumber()+" maksuraja on: "+sender.getMaxPayment());
                        break;
                    case 0:
                        PayError.setText("Tilillä ei ole tarpeeksi rahaa!");
                        break;
                    default:
                        break;
                }
            } else {
                SuccessMessage.setText("");
                PayError.setText("Lisää lähetettävä määrä");
            }
        } else {
            SuccessMessage.setText("");
            PayError.setText("Vastaanottajaa ei löytynyt");
        }
    }

    public void addItemsOnSpinner() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            if(user.getAccounts().get(i).getTypeOfAccount().equals("Käyttötili")) {
                list.add(user.getAccounts().get(i).getAccountNumber());
            }
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    //Find account with given account number and receiver to it
    public boolean setReceiver(int receiverAccNumber) {
        for (User user: users) {
            for(Account account : user.getAccounts()) {
                if (receiverAccNumber == account.getAccountNumber()) {
                    receiver = account;
                    return true;
                }
            }
        }
        return false;
    }

    //Find sender account with given account number and sender to it
    public void setSender() {
        int senderAccNumber = Integer.valueOf(spinner.getSelectedItem().toString());
        for (User user : users) {
            for(Account account : user.getAccounts()) {
                if(senderAccNumber == account.getAccountNumber()) {
                    sender = account;
                }
            }
        }
    }

    public void openDialog() {
        DialogInfo dialogInfo  = new DialogInfo();
        dialogInfo.show(getSupportFragmentManager(), "info dialog");
    }

    public void NewTransferButtonOnClick(View view) {
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
