package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

import java.util.Calendar;

public class CashInActivity extends AppCompatActivity {
    EditText cashInAmount;
    Button cashInBtn;
    DatabaseReference ref;
    double balance, toAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        cashInBtn = findViewById(R.id.cashInBtn);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toAdd = Double.parseDouble(snapshot.child("balance").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cashInBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                cashInAmount = findViewById(R.id.cashInAmount);
                if(cashInAmount.getText().toString() == "0.00"){
                    Toast.makeText(CashInActivity.this,"Please input an amount", Toast.LENGTH_LONG).show();
                }else {
                    balance = toAdd + Double.parseDouble(cashInAmount.getText().toString());
                    ref.child("balance").setValue(balance);
                    writeLog(user.getUid(),Double.parseDouble(cashInAmount.getText().toString()));
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });

    }

    public void writeLog(String userID, Double balance){
        ref = FirebaseDatabase.getInstance().getReference().child("Transactions");
        String transactionId = ref.push().getKey();
        Transaction transaction = new Transaction(transactionId, userID, "Cash in", Calendar.getInstance().getTime().toString(), "Cashed in the amount of " + balance);

        ref.child(transactionId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CashInActivity.this,"Your new balance is: Php. " + balance, Toast.LENGTH_LONG).show();
            }
        });
    }
}