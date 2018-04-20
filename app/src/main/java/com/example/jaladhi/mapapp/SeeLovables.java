package com.example.jaladhi.mapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeeLovables extends AppCompatActivity {

    private FirebaseDatabase ref;
    private DatabaseReference uRef;
    private FirebaseAuth auth;
    private DatabaseReference pRef,cRef;


    String uid,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_lovables);

        ref = FirebaseDatabase.getInstance();
        uRef = ref.getReference("Usage_detail");
        auth = FirebaseAuth.getInstance();
        pRef=ref.getReference("Parent_detail");
        cRef=ref.getReference("Child_detail");

        uid = auth.getCurrentUser().getUid();
        //mobile = pRef.child(uid).child(Mobileno);
        pRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
