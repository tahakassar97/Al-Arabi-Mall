package com.tahakassar.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.marcok.stepprogressbar.StepProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    TextView welcomeTV;
    StepProgressBar mStepProgressBar;
    BasicData basicData;
    private boolean isUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        welcomeTV = findViewById(R.id.welcomeTV);
        basicData = BasicData.getInstance(this);
        basicData.setGradientColor(welcomeTV);
        mStepProgressBar = findViewById(R.id.stepProgressBar);
        if (basicData.getUserData() == null) {
            isUser = false;
        }
        handler(0);
    }

    private void handler(int delay) {
        new Handler().postDelayed(() -> {
            try {
                if (delay < 4) {
                    mStepProgressBar.next();
                    handler(delay + 1);
                } else {
                    if (isUser) {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    }
                    finish();
                }
            } catch (Exception e) {
            }
        }, 750);
    }

}