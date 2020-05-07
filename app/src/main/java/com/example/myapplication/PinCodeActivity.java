package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;


public class PinCodeActivity extends AppCompatActivity {
    public TextView Pin;
    private EditText InputPin;
    private String PinString;
    private TextView ErrorText;
    private String Koodi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);
        Pin = (TextView) findViewById(R.id.ShowPin);
        InputPin = (EditText) findViewById(R.id.InputPinCode);
        ErrorText = (TextView) findViewById(R.id.ErrorText);
        Random random = new Random();
        PinString = String.format("%06d", random.nextInt(999999));
        Pin.setText(PinString);
    }

    public void Pin_SignInOnClick(View view){
        Koodi = InputPin.getText().toString();

        if (Koodi.equals(PinString)) {
            Intent intent = new Intent(this, WelcomeScreenActivity.class);
            startActivity(intent);
        } else {
            ErrorText.setText("Tunnusluku ei täsmää, yritä uudestaan");

        }
    }
}
