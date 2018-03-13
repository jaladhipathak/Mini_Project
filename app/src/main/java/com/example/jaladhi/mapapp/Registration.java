package com.example.jaladhi.mapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class Registration extends AppCompatActivity {
    private EditText regName,regEmail,regMobile,regPassword,regConfirmPassword,regParentMobile;
    private RadioButton regGenParent,regGenChild;
    String Str_Name, Str_Email, Str_Mob, Str_Pass, Str_Conf_pass, Str_pr_mob, Str_is_child;
    YourPreference yourPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);
        yourPrefrence = YourPreference.getInstance(Registration.this);
        regName=(EditText)findViewById(R.id.regName);
        regEmail=(EditText)findViewById(R.id.regEmail);
        regMobile=(EditText)findViewById(R.id.regMobile);
        regPassword=(EditText)findViewById(R.id.regPassword);
        regConfirmPassword=(EditText)findViewById(R.id.regConfirmpassword);
        regParentMobile=(EditText)findViewById(R.id.regParentMobile);
        regGenParent=(RadioButton)findViewById(R.id.regGenParent);
        regGenChild=(RadioButton)findViewById(R.id.regGenChild);
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
                Log.d("Mapapp", Str_Name);
                Intent intent=new Intent(Registration.this,MainActivity.class);
                startActivity(intent);
            }

        }catch (Exception e){
        }
    }


}
