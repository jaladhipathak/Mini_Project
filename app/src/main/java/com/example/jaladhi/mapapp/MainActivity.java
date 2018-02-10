package com.example.jaladhi.mapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void myfun(View view){
        if(view.getId()==R.id.login){
            Intent intent=new Intent(view.getContext(),WelcomeScreen.class);
            EditText edit=(EditText)findViewById(R.id.username);
            String uname=edit.getText().toString();
            intent.putExtra("usrname",uname);
            startActivity(intent);
        }
    }

}

