package com.example.marcin.learnenglishapp.reminder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marcin.learnenglishapp.R;

public class Notification extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notification);
        textView = findViewById(R.id.tv_message);
        Bundle bundle = getIntent().getExtras();                                                    //call the data which is passed by another intent
        textView.setText(bundle.getString("message"));
    }

}
