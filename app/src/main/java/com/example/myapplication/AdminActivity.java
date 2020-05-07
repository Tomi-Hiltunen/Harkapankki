package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminActivity extends AppCompatActivity {
    EditText nameText;
    EditText addressText;
    EditText usernameText;
    EditText passwordText;
    Button saveButton;
    String JSONDataString;
    List<User> users;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        nameText = (EditText) findViewById(R.id.nameEditText);
        addressText = (EditText) findViewById(R.id.addressEditText);
        usernameText = (EditText) findViewById(R.id.usernameEditText);
        passwordText = (EditText) findViewById(R.id.passwordEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        errorText = (TextView) findViewById(R.id.errorText);
    }

    public void saveButtonOnClick(View view) {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        JSONDataString = ReadAndWrite.read("data.json",AdminActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);

        if(checkUsers()) {
            if(checkPass(password)) {
                User newUser = new User(username,password,name,address);
                users.add(newUser);
                String jsonDataString = new Gson().toJson(users);
                ReadAndWrite.write("data.json",jsonDataString,AdminActivity.this);
                errorText.setText("Käyttäjä luotu!");
            } else {
                errorText.setText("Salasanan tulee sisältää vähintään yhden numeron,\n erikoismerkin, ison ja pienen kirjaimen,\n ja olla vähintään 12 merkkiä pitkä.");
            }
        } else {
            errorText.setText("Saman niminen käyttäjätunnus on jo luotu.");
        }
    }

    //Check if given user is already created
    public boolean checkUsers() {
        for (User user : users) {
            if (user.getUsername().equals(usernameText.getText().toString())) {
                return false;
            }
        }
        return true;
    }

    //Check if given password meets the requirements
    public boolean checkPass(String pass) {
        if(pass.length()>= 12) {
            Pattern upperCaseLetter = Pattern.compile("[A-Z]");
            Pattern lowerCaseLetter = Pattern.compile("[a-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern specialChar = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            Matcher hasUpperCaseLetter = upperCaseLetter.matcher(pass);
            Matcher hasLowerCaseLetter = lowerCaseLetter.matcher(pass);
            Matcher hasDigit = digit.matcher(pass);
            Matcher hasSpecialChar = specialChar.matcher(pass);
            return hasUpperCaseLetter.find() && hasLowerCaseLetter.find() && hasDigit.find() && hasSpecialChar.find();
        }
        return false;
    }

    public void logOutButtonOnClick(View view) {
        State.setLoggedInUserIndex(-1);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
