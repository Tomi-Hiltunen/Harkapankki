package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoneyTransferActivity extends AppCompatActivity {
    private User user;
    Spinner spinner1;
    Spinner spinner2;
    EditText Amount;
    TextView Error, Success;
    String JSONDataString;
    List<User> users;
    Account senderAcc;
    Account receiverAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);
        Amount = (EditText) findViewById(R.id.TransferQuantity);

        JSONDataString = ReadAndWrite.read("data.json",MoneyTransferActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());
        addItemsOnSpinner(); //Transfer from this account
    }

    public void addItemsOnSpinner() {
        spinner1 = (Spinner) findViewById(R.id.FromAccount_Spinner);
        spinner2 = (Spinner) findViewById(R.id.ToAccount_Spinner);
        List<Integer> listFromAccounts = new ArrayList<Integer>();
        List<Integer> listToAccounts = new ArrayList<Integer>();
        for (int i = 0; i < user.getAccounts().size(); i++) {
            if(user.getAccounts().get(i).getTypeOfAccount().equals("Käyttötili")) {
                listFromAccounts.add(user.getAccounts().get(i).getAccountNumber());
            }
            listToAccounts.add(user.getAccounts().get(i).getAccountNumber());
        }

        ArrayAdapter<Integer> dataAdapter1 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, listFromAccounts);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView FromAccountType = (TextView) findViewById(R.id.FromAccountType);
                int selectedSpinnerItem1 = Integer.valueOf(spinner1.getSelectedItem().toString());
                for (int i = 0; i < user.getAccounts().size(); i++) {
                    if (user.getAccounts().get(i).getAccountNumber() == selectedSpinnerItem1) {
                        FromAccountType.setText(user.getAccounts().get(i).getTypeOfAccount());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, listToAccounts);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView ToAccountType = (TextView) findViewById(R.id.ToAccountType);
                int selectedSpinnerItem2 = Integer.valueOf(spinner2.getSelectedItem().toString());
                for (int i = 0; i < user.getAccounts().size(); i++) {
                    if (user.getAccounts().get(i).getAccountNumber() == selectedSpinnerItem2) {
                        ToAccountType.setText(user.getAccounts().get(i).getTypeOfAccount());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public void TransferContinueButtonOnClick(View view) {
        float amount;
        int sender = Integer.valueOf(spinner1.getSelectedItem().toString());
        int receiver = Integer.valueOf(spinner2.getSelectedItem().toString());

        EditText amountText = (EditText) findViewById(R.id.TransferQuantity);
        Error = (TextView) findViewById(R.id.ErrorMessage);
        Success = (TextView) findViewById(R.id.SuccessMessage);
        if (amountText.getText().toString().equals("")) {
            amount = 0;
            Error.setText("Lisää lähetettävä määrä");
            Success.setText("");
        } else {
            // If transfer amount is not 0 this method creates new transaction with these accounts (from setReceiverAndSender) and amount to Transaction Manager which completes transfer
            amount = Float.valueOf(amountText.getText().toString());
            setReceiverAndSender(sender,receiver);
            Transaction transaction = new Transaction("Maksu",amount,receiverAcc,senderAcc);
            TransactionManager transactionManager = new TransactionManager(MoneyTransferActivity.this,transaction);
            switch (transactionManager.completePayment("transfer")) {
                case 1:
                    State.setErrorMsg("Siirto onnistui!");
                    openDialog();
                    break;
                case -1:
                    Error.setText("Tilin " + senderAcc.getAccountNumber()+" siirtoraja on: "+senderAcc.getMaxTransfer());
                    break;
                case 0:
                    Error.setText("Tilillä ei ole tarpeeksi rahaa!");
                    break;
                default:
                    break;
            }
        }

    }

    public void setReceiverAndSender(int sender, int receiver) {
        for (User user : users) {
            for(Account account : user.getAccounts()) {
                if(sender == account.getAccountNumber()) {
                    senderAcc = account;
                } else if ( receiver == account.getAccountNumber()) {
                    receiverAcc = account;
                }
            }
        }
    }

    public void openDialog() {
        DialogInfo dialogInfo  = new DialogInfo();
        dialogInfo.show(getSupportFragmentManager(), "info dialog");
    }

    public void NewPaymentButtonOnClick(View view) {
        Intent intent = new Intent(this,NewPaymentActivity.class);
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

    public void CancelButtonOnClick(View view) {
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        Amount.setText("");
        startActivity(intent);
    }

    public void logOutButtonOnClick(View view) {
        State.setLoggedInUserIndex(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
