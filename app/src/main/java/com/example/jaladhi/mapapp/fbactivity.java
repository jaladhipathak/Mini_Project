package com.example.jaladhi.mapapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fbactivity extends AppCompatActivity {

    private Button b1;
    private FirebaseDatabase ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b1=(Button)findViewById(R.id.fbadd);
        ref=FirebaseDatabase.getInstance();
        final DatabaseReference mref=ref.getReference("table1");


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mref.child("User").child("Name").setValue("Prashant");
            }
        });


    }

}
