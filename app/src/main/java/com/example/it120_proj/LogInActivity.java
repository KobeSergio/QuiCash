package com.example.it120_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView register = (TextView) findViewById(R.id.SignUp);
        register.setOnClickListener(this);

        startActivity(new Intent(this, QRScanActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}
