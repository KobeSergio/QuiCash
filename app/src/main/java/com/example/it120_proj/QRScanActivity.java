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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

public class QRScanActivity extends AppCompatActivity
{

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private String recipientID;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        //Status bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        codeScannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, codeScannerView);
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);

        codeScannerView.setOnClickListener(view -> scan());
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
                Toast.makeText(this, "Please allow camera to be able to use this feature.", Toast.LENGTH_SHORT).show();
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
                proceedToCheckOut();
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
                proceedToCheckOut();
            });
        });
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void proceedToCheckOut()
    {
        firebaseDatabase.getReference().child("Users").child(recipientID).get().addOnCompleteListener(task ->
        {
            if (task.getResult().child("username").getValue() == null)
                Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
            else
            {
                Intent intent = new Intent(getBaseContext(), CheckOut.class);
                intent.putExtra("RECIPIENT_ID", recipientID);
                startActivity(intent);
            }
        });
    }
}