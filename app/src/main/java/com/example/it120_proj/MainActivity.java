package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    Button btnScan, btnHistory, btnCashIn;
    ImageView ivQRImg;
    TextView tvBalance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dr;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        btnHistory = findViewById(R.id.THButton);
        btnCashIn = findViewById(R.id.CashIn);
        btnScan = findViewById(R.id.ScanQR);
        ivQRImg = findViewById(R.id.qrImage);
        tvBalance = findViewById(R.id.balance);


        // Get a reference to user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dr = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        //Set values here
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userBalance = snapshot.child("balance").getValue().toString();
                tvBalance.setText("Php. " + userBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //QR code automatically generated
        QRGEncoder qrgEncoder = new QRGEncoder(user.getUid(), null, QRGContents.Type.TEXT, 500);
        Bitmap qrBits = qrgEncoder.getBitmap();
        ivQRImg.setImageBitmap(qrBits);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QRScanActivity.class));
            }
        });
        btnCashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CashInActivity.class));
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransactionHistory.class));
            }
        });
    }
}