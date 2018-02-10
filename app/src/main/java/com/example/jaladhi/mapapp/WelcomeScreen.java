package com.example.jaladhi.mapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        String name = getIntent().getStringExtra("usrname");
        TextView txt = (TextView) findViewById(R.id.WelcomeMessage);
        txt.append(" "+name);
    }

}

