package com.example.it120_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class QRScanActivity extends AppCompatActivity
{

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        codeScannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, codeScannerView);

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
                //cost.setText("₱" + result);
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
                //cost.setText("₱" + result);
            });
        });
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

}