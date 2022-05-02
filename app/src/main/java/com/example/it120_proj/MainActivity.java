package com.example.it120_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    EditText qrVal;
    Button GenerateBTN, ScanBTN, THistory, cashIn;
    ImageView qrImg;
    TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrVal = findViewById(R.id.qrValue);
        cashIn = findViewById(R.id.CashIn);
        ScanBTN = findViewById(R.id.ScanQR);
        qrImg = findViewById(R.id.qrImage);
        balance = findViewById(R.id.balance);

        GenerateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = qrVal.getText().toString();
                if (data.isEmpty()) {
                    qrVal.setError("Please input a value");
                }
                else {
                    QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
                    Bitmap qrBits = qrgEncoder.getBitmap();
                    qrImg.setImageBitmap(qrBits);
                }
            }
        });

        ScanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QRScanActivity.class));
            }
        });
    }
}