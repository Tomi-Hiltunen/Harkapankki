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

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorText;
    private String usernameString;
    private String passwordString;
    private List<User> users;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = (EditText) findViewById(R.id.usernamePlaintext);
        passwordEditText = (EditText) findViewById(R.id.passPlaintext);
        errorText = (TextView) findViewById(R.id.textView3);

        //Initialize data file(remove the comments to make the data file in phones memory)
        /*
        String asd = "[\n" +
                "  {\n" +
                "    \"accounts\": [],\n" +
                "    \"address\": \"1\",\n" +
                "    \"cards\": [],\n" +
                "    \"name\": \"1\",\n" +
                "    \"password\": \"1\",\n" +
                "    \"username\": \"1\"\n" +
                "  }\n" +
                "]";

        ReadAndWrite.write("data.json",asd,MainActivity.this);
        */
    }

    /*
    Takes user to pincode activity if user and pass are correct.
    If user enters admins credentials then admin activity opens.
     */
    public void loginButtonOnClick(View view) {
        String JSONDataString = ReadAndWrite.read("data.json",MainActivity.this);
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new Gson().fromJson(JSONDataString, userListType);
        switch (checkCredentials()) {
            case 0:
                errorText.setText("Käyttäjää ei löytynyt");
                break;
            case 1:
                errorText.setText("Väärä salasana");
                break;
            case 2:
                intent = new Intent(this,PinCodeActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this,AdminActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /*
    Checks if user input matches with credentials from the data file
    And returns errorcode to be handled in another method.
     */
    public int checkCredentials() {
        usernameString = usernameEditText.getText().toString();
        passwordString = passwordEditText.getText().toString();
        int errorCode = 0;
        if(usernameString.equals("admin123") && passwordString.equals("pass123")) {
            return 3;
        }
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(usernameString)) {
                errorCode = 1;
                if(users.get(i).getPassword().equals(passwordString)) {
                    errorCode = 2;
                    State.setLoggedInUserIndex(i);
                    State.setCurAccIndex(0);
                }
            }
        }
        return errorCode;
    }
}
