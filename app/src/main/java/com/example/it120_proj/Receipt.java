package com.example.it120_proj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Receipt extends AppCompatActivity
{

    private String recipientID;
    private String amountPaid;
    private String transactionID;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private TextView recipientName;
    private TextView amountPaidTextView;
    private TextView transactionIDTextView;
    private DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //Status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        recipientID = getIntent().getStringExtra("RECIPIENT_ID");
        amountPaid = getIntent().getStringExtra("AMOUNT_PAID");
        transactionID = getIntent().getStringExtra("TRANSACTION_ID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recipientName = findViewById(R.id.payment_textview_recipent_name);
        amountPaidTextView = findViewById(R.id.textview_receipt_amount_paid);
        transactionIDTextView = findViewById(R.id.textview_receipt_transactionID);
        df = new DecimalFormat("0.00");

        init();
    }

    private void init()
    {
        //Set recipient name
        firebaseDatabase.getReference().child("Users").child(recipientID).child("username").get().addOnCompleteListener(task ->
        {
            recipientName.setText(task.getResult().getValue().toString());
        });

        //Set amount paid
        amountPaidTextView.setText("PHP " + df.format(Double.parseDouble(amountPaid)));

        //Set transaction ID
        transactionIDTextView.setText(transactionID);
    }

    public void home(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}