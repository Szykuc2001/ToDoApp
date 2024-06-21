package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

// Splash screen activity that shows a splash screen for 2 seconds before transitioning to MainActivity
public class SplashActivity extends AppCompatActivity {

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Set the layout for the splash screen

        // Create an intent to transition from SplashActivity to MainActivity
        final Intent i = new Intent(SplashActivity.this, MainActivity.class);

        // Use a handler to introduce a delay of 2 seconds before transitioning to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i); // Start the MainActivity
                finish(); // Finish and close the SplashActivity
            }
        }, 2000); // 2000 milliseconds delay (2 seconds)
    }
}
