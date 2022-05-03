package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;

public class CheckOut extends AppCompatActivity
{

    private String recipientID;
    private String userID;
    private String transactionID;
    private String recipientName;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private TextView recipientNameText;
    private TextView availableBalance;
    private EditText amountInput;
    private double currentbalance;
    private double recipientBalance;
    private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        //Status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        recipientID = getIntent().getStringExtra("RECIPIENT_ID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        recipientNameText = findViewById(R.id.payment_textview_recipent_name);
        availableBalance = findViewById(R.id.payment_vailable_balance);
        amountInput = findViewById(R.id.payment_edittext);
        df = new DecimalFormat("0.00");

        firebaseDatabase.getReference().child("Users").child(recipientID).child("username").get().addOnCompleteListener(task ->
        {
            recipientName = task.getResult().getValue().toString();
        });

        init();
    }

    private void init()
    {
        //Set recipient name
        firebaseDatabase.getReference().child("Users").child(recipientID).get().addOnCompleteListener(task ->
        {
            recipientNameText.setText(task.getResult().child("username").getValue().toString());
        });

        //Get available balance
        firebaseDatabase.getReference().child("Users").child(userID).get().addOnCompleteListener(task ->
        {
            currentbalance = Double.parseDouble(task.getResult().child("balance").getValue().toString());
            availableBalance.setText("Available Balance PHP: " + df.format(currentbalance));
        });
    }

    //Pay button
    public void pay(View view)
    {
        //Amount to pay validation
        if (!payAmountValidationError())
        {
            //Check if have sufficient balance
            if (currentbalance < Double.parseDouble(amountInput.getText().toString()))
                Toast.makeText(this, "Insufficient funds!", Toast.LENGTH_SHORT).show();
            else
            {
                updateDataBase();

                Intent intent = new Intent(getBaseContext(), Receipt.class);
                intent.putExtra("RECIPIENT_ID", recipientID);
                intent.putExtra("AMOUNT_PAID", amountInput.getText().toString());
                intent.putExtra("TRANSACTION_ID", transactionID);

                startActivity(intent);
            }
        }
        else
            Toast.makeText(this, "Invalid amount input!", Toast.LENGTH_SHORT).show();
    }

    private boolean payAmountValidationError()
    {
        //No input
        if (amountInput.getText() == null)
            return true;

        //Input is non numerical
        try
        {
            Integer.parseInt(amountInput.getText().toString());
            return  false;
        }
        catch (NumberFormatException e)
        {
            return true;
        }

    }

    private void updateDataBase()
    {
        //Deduct amount paid
        currentbalance -= Double.parseDouble(amountInput.getText().toString());
        firebaseDatabase.getReference().child("Users").child(userID).child("balance").setValue(currentbalance);

        //Add amount paid to recipient
        firebaseDatabase.getReference().child("Users").child(recipientID).child("balance").get().addOnCompleteListener(task ->
        {
            recipientBalance = Double.parseDouble(task.getResult().getValue().toString());
            recipientBalance += Double.parseDouble(amountInput.getText().toString());
            firebaseDatabase.getReference().child("Users").child(recipientID).child("balance").setValue(recipientBalance);
        });

        //Add transaction to database
        transactionID = firebaseDatabase.getReference().child("Transactions").push().getKey();
        Transaction transaction = new Transaction(transactionID, userID, "Payment", Calendar.getInstance().getTime().toString(),
                "Paid with the amount of " + amountInput.getText().toString() + " PHP to " + recipientName + " ID:" + recipientID);
        firebaseDatabase.getReference().child("Transactions").child(transactionID).setValue(transaction);

    }

}