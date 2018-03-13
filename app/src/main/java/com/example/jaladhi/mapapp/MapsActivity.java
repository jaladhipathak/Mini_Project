package com.example.jaladhi.mapapp;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double CurrentLatitude,CurrentLongitude;
    private BroadcastReceiver broadcastReceiver;
    Intent intent2;
    Toolbar toolbar;
    private DrawerLayout myDrawerlayout;
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    TextView tw;
    YourPreference yourPrefrence;
    LatLng x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_side_nav);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menubutton);
        myDrawerlayout =findViewById(R.id.drawer_layout);
        //tw=(TextView)findViewById(R.id.loggedusername);
        //yourPrefrence=YourPreference.getInstance(MapsActivity.this);
        //String name=yourPrefrence.getData(Constants.NAME1);
        //tw.setText(name);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.newpost) {
                    Intent intent=new Intent(MapsActivity.this,New_Post.class);
                    startActivity(intent);
                } else if (id == R.id.smypost) {

                } else if (id == R.id.sylove) {

                } else if (id == R.id.chcity) {

                } else if (id == R.id.refresh) {

                } else if (id == R.id.logout) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawerToggle= setupDrawerToggle();
        myDrawerlayout.addDrawerListener(drawerToggle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent2 = new Intent(MapsActivity.this, GoogleService.class);
        startService(intent2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
        startService(intent2);

    }
    @Override
    protected void onStart(){
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                CurrentLatitude = Double.valueOf(intent.getStringExtra("latitude"));
                CurrentLongitude = Double.valueOf(intent.getStringExtra("longitude"));
                mMap.clear();
                x= new LatLng(CurrentLatitude,CurrentLongitude);
                //mMap.addCircle(new CircleOptions().center(x).radius(30.0).fillColor(Color.BLUE).strokeColor(Color.BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(x));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,15));
            }
        };;

    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent2);
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

        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(x));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,15));
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(CurrentLatitude, CurrentLongitude);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private android.support.v7.app.ActionBarDrawerToggle setupDrawerToggle(){
        return new ActionBarDrawerToggle(this, myDrawerlayout, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
    }
    @Override
    public void onBackPressed() {

        if (myDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            myDrawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == android.R.id.home) {
//            myDrawerlayout.openDrawer(GravityCompat.START);
//            return true;
//        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}

