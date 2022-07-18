package com.example.marcin.learnenglishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.marcin.learnenglishapp.player.PlayerActivity;
import com.example.marcin.learnenglishapp.record.RecordLaunchActivity;
import com.example.marcin.learnenglishapp.reminder.ReminderLaunchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //Tworzę nowe aktywnośći
//Przypomnienia
    public void launchSettingsReminder(View v){
        Intent i = new Intent(this, ReminderLaunchActivity.class);
        startActivity(i);
    }
//Nagrywanie
    public void launchSettingsRecord(View v){
        Intent i = new Intent(this, RecordLaunchActivity.class);
        startActivity(i);
    }
//Odtwarzacz
    public void launchSettingsPlayer(View v){
        Intent i = new Intent(this, PlayerActivity.class);
        startActivity(i);
    }

    public void launchTestPlayer(View v){
        Intent i = new Intent(this, RangeActib.class);
        startActivity(i);
    }

}