package com.example.marcin.learnenglishapp.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marcin.learnenglishapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    int secDelay = 1;
    new Handler().postDelayed(new Runnable() {

        public void run() {
            startActivity(new Intent(getApplicationContext(), ReminderLaunchActivity.class));
            finish();
        }
    }, secDelay * 500);

 }

}
