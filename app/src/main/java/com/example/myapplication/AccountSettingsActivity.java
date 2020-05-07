package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {
    private Spinner spinner;
    private User user;
    private SeekBar seekbar1, seekbar2;
    private TextView text1, text2;
    private List<User> users;
    private RadioGroup radiogroup;
    private RadioButton normal, saving;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        String JSONDataString = ReadAndWrite.read("data.json",AccountSettingsActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        user = users.get(State.getLoggedInUserIndex());

        message = (TextView) findViewById(R.id.Message);
        normal = (RadioButton) findViewById(R.id.NormalAccountButton);
        saving = (RadioButton) findViewById(R.id.SavingAccountButton);
        spinner = (Spinner) findViewById(R.id.AccountNumberSpinner);
        seekbar1 = (SeekBar) findViewById(R.id.MaxPayment_seekBar);
        seekbar2 = (SeekBar) findViewById(R.id.MaxTransfer_seekbar);
        text1 = (TextView) findViewById(R.id.MaxPaymentShow);
        text2 = (TextView) findViewById(R.id.MaxTransferShow);
        radiogroup = (RadioGroup) findViewById(R.id.AccountTypeGroup);

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
                if (user.getAccounts().get(State.getCurAccIndex()).getTypeOfAccount().equals("Säästötili")) {
                    saving.setChecked(true);
                    seekbar1.setVisibility(View.GONE);
                    seekbar2.setVisibility(View.GONE);
                    text1.setText("Säästötililtä ei voi maksaa");
                    text2.setText("Säästötililtä ei voi siirtää rahaa");
                    message.setText("");

                }
                else {
                    normal.setChecked(true);
                    seekbar1.setVisibility(View.VISIBLE);
                    seekbar2.setVisibility(View.VISIBLE);
                    String payment = Integer.toString(user.getAccounts().get(State.getCurAccIndex()).getMaxPayment());
                    String transfer = Integer.toString(user.getAccounts().get(State.getCurAccIndex()).getMaxTransfer());
                    text1.setText(payment+"€");
                    text2.setText(transfer+"€");
                    seekbar1.setProgress(user.getAccounts().get(State.getCurAccIndex()).getMaxPayment());
                    seekbar2.setProgress(user.getAccounts().get(State.getCurAccIndex()).getMaxTransfer());
                    message.setText("");


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    if (saving.isChecked()) {
                        seekbar1.setVisibility(View.GONE);
                        seekbar2.setVisibility(View.GONE);
                        text1.setText("Säästötililtä ei voi maksaa");
                        text2.setText("Säästötililtä ei voi siirtää rahaa");
                        message.setText("");
                        // We hide options not available for specific account type

                    } else {
                        // We show options which are available for specific account type and set current values to seekbar
                        normal.setChecked(true);
                        seekbar1.setVisibility(View.VISIBLE);
                        seekbar2.setVisibility(View.VISIBLE);
                        String payment = Integer.toString(user.getAccounts().get(State.getCurAccIndex()).getMaxPayment());
                        String transfer = Integer.toString(user.getAccounts().get(State.getCurAccIndex()).getMaxTransfer());
                        text1.setText(payment);
                        text2.setText(transfer);
                        seekbar1.setProgress(user.getAccounts().get(State.getCurAccIndex()).getMaxPayment());
                        seekbar2.setProgress(user.getAccounts().get(State.getCurAccIndex()).getMaxTransfer());
                        message.setText("");
                    }
                }
            }
        });

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text1.setText(""+progress+"€");
                message.setText("");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text2.setText(""+progress+"€");
                message.setText("");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
    public void SaveButtonOnClick(View view) {
        int radioId = radiogroup.getCheckedRadioButtonId();
        RadioButton radiobutton = findViewById(radioId);

        //Saving writes new information to json file.
        if (radiobutton.getText().toString().equals("Käyttötili")) {

            user.getAccounts().get(State.getCurAccIndex()).setTypeOfAccount(radiobutton.getText().toString());
            user.getAccounts().get(State.getCurAccIndex()).setMaxPayment(seekbar1.getProgress());
            user.getAccounts().get(State.getCurAccIndex()).setMaxTransfer(seekbar2.getProgress());

            String json = new Gson().toJson(users);
            ReadAndWrite.write("data.json", json, AccountSettingsActivity.this);
        }
        else if(radiobutton.getText().toString().equals("Säästötili"))
        {
            user.getAccounts().get(State.getCurAccIndex()).setTypeOfAccount(radiobutton.getText().toString());
            user.getAccounts().get(State.getCurAccIndex()).setMaxPayment(0);
            user.getAccounts().get(State.getCurAccIndex()).setMaxTransfer(0);

            String json = new Gson().toJson(users);
            ReadAndWrite.write("data.json", json, AccountSettingsActivity.this);
        }
        message.setText("Muutos tallennettu!");
    }
}