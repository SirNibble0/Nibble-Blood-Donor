package com.example.nibbleblooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RecipientRegistrationActivity extends AppCompatActivity {

    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_registration);

        backButton = findViewById(R.id.rRegisterHaveAcct);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipientRegistrationActivity.this, LoginActivity.class));
            }
        });
    }
}