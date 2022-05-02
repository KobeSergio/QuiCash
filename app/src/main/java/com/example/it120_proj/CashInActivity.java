package com.example.it120_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CashInActivity extends AppCompatActivity {
    EditText cashInAmount;
    Button cashInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);

        cashInBtn = findViewById(R.id.cashInBtn);
        cashInAmount = findViewById(R.id.cashInAmount);



    }
}