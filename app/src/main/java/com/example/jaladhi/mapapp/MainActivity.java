package com.example.jaladhi.mapapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    YourPreference yourPrefrence;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourPrefrence = YourPreference.getInstance(MainActivity.this);
        auth=FirebaseAuth.getInstance();
        Button b1=(Button)findViewById(R.id.login);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit=(EditText)findViewById(R.id.username);
                EditText passw=(EditText)findViewById(R.id.password);
                email=edit.getText().toString();
                password=passw.getText().toString();
                loginfun();
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(intent);
                }
            }
        };

    }
    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    public void loginfun(){
        if(!(TextUtils.isEmpty(email)||TextUtils.isEmpty(password))) {
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(MainActivity.this,"Fields Empty",Toast.LENGTH_LONG).show();
        }
    }
    public void changefun(View view){
        if(view.getId()==R.id.notYetRegistered){
            Intent intent3=new Intent(view.getContext(),Registration.class);
            startActivity(intent3);
        }
    }

}

