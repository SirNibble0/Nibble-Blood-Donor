package com.example.nibbleblooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private Button donorBtn, recipientBtn;
    private TextView haveAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);

        haveAcct = findViewById(R.id.haveAcct);
        donorBtn = findViewById(R.id.donorBtn);
        recipientBtn = findViewById(R.id.recipientBtn);

        haveAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectRegistrationActivity.this, LoginActivity.class));
            }
        });

        donorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectRegistrationActivity.this, DonorRegistrationActivity.class));
            }
        });

        recipientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectRegistrationActivity.this, RecipientRegistrationActivity.class));
            }
        });
    }
}