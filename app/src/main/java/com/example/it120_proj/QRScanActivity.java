package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;

public class QRScanActivity extends AppCompatActivity
{

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private EditText editTextPayAmount;
    private String recipientID;
    private String amountToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        codeScannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, codeScannerView);
        editTextPayAmount = findViewById(R.id.edittext_amount);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
            scan();
        }

        codeScannerView.setOnClickListener((View.OnClickListener) view -> scan());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                scan();
            else
                Toast.makeText(this, "Please allow camera to be able to use this feature.", Toast.LENGTH_SHORT);
        }
    }

    private void scan()
    {
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(result ->
        {
            runOnUiThread(() ->
            {
                recipientID = result.toString();
            });
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(result ->
        {
            runOnUiThread(() ->
            {
                recipientID = result.toString();
            });
        });
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    //Pay button
    public void pay(View view)
    {
        //Amount to pay validation
        if (!payAmountValidationError())
        {
            amountToPay = editTextPayAmount.getText().toString();

            Intent intent = new Intent(getBaseContext(), Receipt.class);
            intent.putExtra("RECIPIENT_ID", recipientID);
            intent.putExtra("AMOUNT_TO_PAY", amountToPay);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Invalid amount input!", Toast.LENGTH_SHORT);
    }

    private boolean payAmountValidationError()
    {
        //No input
        if (editTextPayAmount.getText() == null)
            return true;

        //Input is non numerical
        try
        {
            Integer.parseInt(editTextPayAmount.getText().toString());
            return  false;
        }
        catch (NumberFormatException e)
        {
            return true;
        }

    }
}