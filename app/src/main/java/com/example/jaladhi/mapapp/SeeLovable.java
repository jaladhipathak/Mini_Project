package com.example.jaladhi.mapapp;

import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SeeLovable extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String cid, latitude, longitude;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference uRef;
    Double CurrentLatitude, CurrentLongitude;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_lovable);
        count=0;
        //Bundle extras = getIntent().getExtras();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Log.v("MSGLoc",getIntent().getStringExtra("Hi"));

        cid= getIntent().getStringExtra("cid");
        //Log.v("MSGLoc",cid);
        firebaseDatabase = FirebaseDatabase.getInstance();
        uRef = firebaseDatabase.getReference("Usage_detail");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        putLocation();
        // Add a marker in Sydney and move the camera

    }

    public void putLocation(){
        uRef.child(cid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,Double> ChildUpdates = (Map) dataSnapshot.getValue();
                    CurrentLatitude = ChildUpdates.get("Latitude");
                    CurrentLongitude = ChildUpdates.get("Longitude");
                    Log.v("MSGLoc",CurrentLatitude.toString());
                    Log.v("MSGLoc",CurrentLongitude.toString());
                    if(CurrentLatitude!=null && CurrentLongitude!=null) {
                        //CurrentLatitude = Double.parseDouble(latitude);
                        //CurrentLongitude = Double.parseDouble(longitude);
                        LatLng x = new LatLng(CurrentLatitude, CurrentLongitude);
                        if (count == 0) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 15));
                            count++;
                        }
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(x).title("Child").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
