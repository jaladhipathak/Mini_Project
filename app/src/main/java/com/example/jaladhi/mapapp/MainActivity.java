package com.example.jaladhi.mapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    YourPreference yourPrefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourPrefrence = YourPreference.getInstance(MainActivity.this);

    }
    public void myfun(View view){
        if(view.getId()==R.id.login){

            EditText edit=(EditText)findViewById(R.id.username);
            EditText passw=(EditText)findViewById(R.id.password);
            String uname=edit.getText().toString();
            String pass=passw.getText().toString();
            String storedname=yourPrefrence.getData(Constants.NAME1);
            String storedpass=yourPrefrence.getData(Constants.PASSWORD1);
            if(uname.equals(storedname) && pass.equals(storedpass)){
                Intent intent=new Intent(view.getContext(),MapsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
            }

        }
    }
    public void changefun(View view){
        if(view.getId()==R.id.notYetRegistered){
            Intent intent3=new Intent(view.getContext(),Registration.class);
            startActivity(intent3);
        }
    }

}

