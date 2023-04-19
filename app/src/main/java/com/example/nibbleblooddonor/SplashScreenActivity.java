package com.example.nibbleblooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView title, slogan;
    private ImageView logo;
    Animation topAnimation, sideAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        slogan = findViewById(R.id.slogan);
        title = findViewById(R.id.title);
        logo = findViewById(R.id.icon);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_animation);

        title.setAnimation(sideAnimation);
        slogan.setAnimation(sideAnimation);
        logo.setAnimation(topAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 4300);

    }
}