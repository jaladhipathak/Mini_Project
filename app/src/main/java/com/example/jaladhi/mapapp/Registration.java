package com.example.jaladhi.mapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private EditText regName,regEmail,regMobile,regPassword,regConfirmPassword,regParentMobile;
    private RadioButton regGenParent,regGenChild;
    String Str_Name, Str_Email, Str_Mob, Str_Pass, Str_Conf_pass, Str_pr_mob, Str_is_child;
    YourPreference yourPrefrence;
    private FirebaseDatabase ref;
    private DatabaseReference pRef;
    private DatabaseReference cRef;
    private DatabaseReference uRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);

        yourPrefrence = YourPreference.getInstance(Registration.this);

        ref=FirebaseDatabase.getInstance();
        pRef=ref.getReference("Parent_detail");
        cRef=ref.getReference("Child_detail");
        uRef=ref.getReference("Usage_detail");
        auth=FirebaseAuth.getInstance();

        regName=(EditText)findViewById(R.id.regName);
        regEmail=(EditText)findViewById(R.id.regEmail);
        regMobile=(EditText)findViewById(R.id.regMobile);
        regPassword=(EditText)findViewById(R.id.regPassword);
        regConfirmPassword=(EditText)findViewById(R.id.regConfirmpassword);
        regParentMobile=(EditText)findViewById(R.id.regParentMobile);
        regGenParent=(RadioButton)findViewById(R.id.regGenParent);
        regGenChild=(RadioButton)findViewById(R.id.regGenChild);
        //Str_is_child="false";

        regGenParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regGenChild.isChecked())
                    regGenChild.setChecked(false);
                Str_is_child="false";
                regParentMobile.setVisibility(View.GONE);
            }
        });

        regGenChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regGenParent.isChecked())
                    regGenParent.setChecked(false);
                Str_is_child="true";
                regParentMobile.setVisibility(View.VISIBLE);
            }
        });

    }
    public void newfun(View view){
        try {
            Str_Name = regName.getText().toString();
            Str_Email = regEmail.getText().toString();
            Str_Mob = regMobile.getText().toString();
            Str_Pass = regPassword.getText().toString();
            Str_Conf_pass = regConfirmPassword.getText().toString();
            Str_pr_mob = regParentMobile.getText().toString();
            if (Str_Name.equals("")) {
                regName.setError("Insert valid Name");
            } else if (Str_Email.equals("")) {
                regEmail.setError("Insert valid Email");
            } else if (Str_Mob.equals("") || Str_Mob.length()!=10) {
                regMobile.setError("Insert valid Mobile Number");
            } else if (Str_Pass.equals("") || Str_Pass.length()<6 || Str_Pass.length()>14) {
                regPassword.setError("Insert valid Password");
            } else if (Str_Conf_pass.equals("") || Str_Pass.length()<6 || Str_Pass.length()>14||!Str_Conf_pass.equals(Str_Pass)) {
                regConfirmPassword.setError("Your Password does't match");
            } else if ((Str_pr_mob.equals("") || Str_pr_mob.length()!=10) && regGenChild.isChecked()) {
                regParentMobile.setError("Insert valid parent mobile number");
            }else {
                yourPrefrence.saveData(Constants.NAME1,Str_Name);
                yourPrefrence.saveData(Constants.EMAILID1,Str_Email);
                yourPrefrence.saveData(Constants.MOBILE1,Str_Mob);
                yourPrefrence.saveData(Constants.PASSWORD1,Str_Pass);
                yourPrefrence.saveData(Constants.ISCHILD, Str_is_child);
                yourPrefrence.saveData(Constants.PARENTMOBILE1,Str_pr_mob);

                if(regGenParent.isChecked()){

                    auth.createUserWithEmailAndPassword(Str_Email, Str_Pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(Registration.this, "Register done", Toast.LENGTH_SHORT).show();
                                    if (task.isSuccessful()) {
                                        auth.signInWithEmailAndPassword(Str_Email,Str_Pass).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Toast.makeText(Registration.this, "signin", Toast.LENGTH_SHORT).show();
                                                FirebaseUser user = auth.getCurrentUser();
                                                if(user!=null) {
                                                    String uid = user.getUid();
                                                    pRef.child(uid);
                                                    pRef.child(uid).child("Name").setValue(Str_Name);
                                                    pRef.child(uid).child("Mobileno").setValue(Str_Mob);
                                                }
                                                else {
                                                    Toast.makeText(Registration.this,"Null User",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Log.d("msg","createUserWithEmail:success");
                                    } else {
                                        Log.w("msg", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Registration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });

//                    Intent intent=new Intent(Registration.this,MapsActivity.class);
//                    intent.putExtra("Str_Name",Str_Name);
//                    intent.putExtra("Str_Mob",Str_Mob);
//                    startActivity(intent);
//                    FirebaseUser user = auth.getCurrentUser();
//                    if(user!=null) {
//                        String uid = user.getUid();
//                        pRef.child(uid);
//                        pRef.child(uid).child("Name").setValue(Str_Name);
//                        pRef.child(uid).child("Mobileno").setValue(Str_Mob);
//                    }
//                    else {
//                        Toast.makeText(Registration.this,"Null User",Toast.LENGTH_SHORT).show();
//                    }

                }
                else if(regGenChild.isChecked()){

                    auth.createUserWithEmailAndPassword(Str_Email, Str_Pass)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        auth.signInWithEmailAndPassword(Str_Email,Str_Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                FirebaseUser user = auth.getCurrentUser();
                                                if(user!=null) {
                                                    String uid = user.getUid();
                                                    cRef.child(uid);
                                                    cRef.child(uid).child("Name").setValue(Str_Name);
                                                    cRef.child(uid).child("Mobileno").setValue(Str_Mob);
                                                    cRef.child(uid).child("ParentMobno").setValue(Str_pr_mob);
                                                    uRef.child(uid);
                                                }else {
                                                    Toast.makeText(Registration.this,"Null User",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        Log.d("msg","createUserWithEmail:success");

                                    } else {

                                        Log.w("msg", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Registration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });

//                    Intent intent=new Intent(Registration.this,MapsActivity.class);
//                    intent.putExtra("Str_Name",Str_Name);
//                    intent.putExtra("Str_Mob",Str_Mob);
//                    intent.putExtra("Str_pr_mob",Str_pr_mob);
//                    startActivity(intent);

//                    FirebaseUser user = auth.getCurrentUser();
//                    if(user!=null) {
//                        String uid = user.getUid();
//                        cRef.child(uid);
//                        cRef.child(uid).child("Name").setValue(Str_Name);
//                        cRef.child(uid).child("Mobileno").setValue(Str_Mob);
//                        cRef.child(uid).child("ParentMobno").setValue(Str_pr_mob);
//                    }else {
//                        Toast.makeText(Registration.this,"Null User",Toast.LENGTH_SHORT).show();
//                    }
                }

                Intent intent=new Intent(Registration.this,MainActivity.class);
                startActivity(intent);
            }

        }catch (Exception e){
        }
    }


}
