package com.example.nibbleblooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView noAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noAcct = findViewById(R.id.dontHaveAcct);

        noAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SelectRegistrationActivity.class));
            }
        });
    }
}