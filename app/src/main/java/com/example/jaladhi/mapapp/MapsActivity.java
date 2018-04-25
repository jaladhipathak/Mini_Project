package com.example.jaladhi.mapapp;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double CurrentLatitude, CurrentLongitude;
    protected BroadcastReceiver broadcastReceiver;
    Intent intent2;
    Toolbar toolbar;
    private DrawerLayout myDrawerlayout;
    private android.support.v7.app.ActionBarDrawerToggle drawerToggle;
    TextView tw;
    YourPreference yourPrefrence;
    LatLng x;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference pRef;
    private DatabaseReference cRef;
    private DatabaseReference uRef;
    private FirebaseDatabase ref;
    private DatabaseReference sRef;
    private Map<String,String> markerdetail;
    private String pmob,cpmob;
    public String cid;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_side_nav);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menubutton);
        myDrawerlayout = findViewById(R.id.drawer_layout);
        markerdetail = new HashMap<String, String>();
        count = 0;

        NavigationView navigationView = findViewById(R.id.nav_view);
        ref = FirebaseDatabase.getInstance();
        pRef = ref.getReference("Parent_detail");
        cRef = ref.getReference("Child_detail");
        uRef = ref.getReference("Usage_detail");
        sRef = ref.getReference("Story_detail");
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() == null)
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        };

        //set username text on header
        View headerview = navigationView.getHeaderView(0);
        tw = (TextView) headerview.findViewById(R.id.loggedusername);
        yourPrefrence = YourPreference.getInstance(MapsActivity.this);
        String name = yourPrefrence.getData(Constants.NAME1);
        String Mobno = yourPrefrence.getData(Constants.MOBILE1);
        tw.setText(name);

        if (Constants.ISCHILD.equals("false")) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                pRef.child(uid);
                pRef.child(uid).child("Name").setValue(name);
                pRef.child(uid).child("Mobileno").setValue(Mobno);
            } else {
                Toast.makeText(MapsActivity.this, "Null User", Toast.LENGTH_SHORT).show();
            }

        } else if (Constants.ISCHILD.equals("true")) {
            String pmobileno = yourPrefrence.getData(Constants.PARENTMOBILE1);
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                cRef.child(uid);
                cRef.child(uid).child("Name").setValue(name);
                cRef.child(uid).child("Mobileno").setValue(Mobno);
                cRef.child(uid).child("ParentMobno").setValue(pmobileno);
            } else {
                Toast.makeText(MapsActivity.this, "Null User", Toast.LENGTH_SHORT).show();
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.newpost) {
                    Intent intent = new Intent(MapsActivity.this, New_Post.class);
                    intent.putExtra("lat", Double.toString(CurrentLatitude));
                    intent.putExtra("long", Double.toString(CurrentLongitude));
                    //Log.d("SendingLatitude",CurrentLatitude);
                    startActivity(intent);
                } else if (id == R.id.smypost) {
                    mMap.clear();
                    showMyPosts();
                } else if (id == R.id.sylove) {
                    Seelovables();

                } else if (id == R.id.refresh) {
                    mMap.clear();
                    showallposts();
                } else if (id == R.id.logout) {
                    auth.signOut();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawerToggle = setupDrawerToggle();
        myDrawerlayout.addDrawerListener(drawerToggle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent2 = new Intent(MapsActivity.this, GoogleService.class);
        startService(intent2);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setImageResource(android.R.drawable.ic_menu_mylocation);
//        fab.setBackgroundColor(R.attr.colorPrimary);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MapsActivity.this, "Click", Toast.LENGTH_SHORT).show();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 15));
//            }
//        });


        showallposts();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
        startService(intent2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                CurrentLatitude = Double.valueOf(intent.getStringExtra("latitude"));
                CurrentLongitude = Double.valueOf(intent.getStringExtra("longitude"));
                String ischild = yourPrefrence.getData(Constants.ISCHILD);
                if (ischild.equals("true")) {
                    String id = auth.getCurrentUser().getUid();
                    uRef.child(id).child("Latitude").setValue(CurrentLatitude);
                    uRef.child(id).child("Longitude").setValue(CurrentLongitude);
                }
                //mMap.clear();
                x = new LatLng(CurrentLatitude, CurrentLongitude);
                if (count == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 15));
                    count++;
                }
                //mMap.addCircle(new CircleOptions().center(x).radius(30.0).fillColor(Color.BLUE).strokeColor(Color.BLUE));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(x));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,15));
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
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

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    String markerid=marker.getId();
                    String impath=markerdetail.get(markerid);
                    String captionDetail=markerdetail.get(impath);
                    Intent nintent = new Intent(MapsActivity.this,PhotoViewer.class);
                    nintent.putExtra("Caption",captionDetail);
                    nintent.putExtra("ImagePath", impath);
                    startActivity(nintent);
                    return false;
                }
            });
        }
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

    public void showallposts(){

        markerdetail = new HashMap<String, String>();
        sRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Databaseresponse","data: "+dataSnapshot.getValue());
                if(dataSnapshot.exists()){
                    for(DataSnapshot sid:dataSnapshot.getChildren()){
                        Map<String,String> detail=(Map) sid.getValue();

                        String slat=detail.get("Storylatitude");
                        String slong=detail.get("Storylongitude");
                        String gb=detail.get("Good_Bad");
                        String captiondetail=detail.get("CaptionDetail");
                        Double Storylat=0.0,Storylong=0.0;
                        if(slat!=null && slong!=null) {
                            Storylat = Double.parseDouble(slat);
                            Storylong = Double.parseDouble(slong);
                        }
                        String imagename = detail.get("ImagePath");
                        LatLng newmarker=new LatLng(Storylat,Storylong);
                        String markerid;
                        if(gb!=null) {
                            if (gb.equals("good")) {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(newmarker).title(captiondetail).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                markerid = marker.getId();
                            } else {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(newmarker).title(captiondetail).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                markerid = marker.getId();
                            }
                            Log.v("DETAILS", "StoryLatitude" + slat);
                            Log.v("DETAILS", "StoryLongitude" + slong);
                            Log.v("DETAILS", "GOODBad" + gb);
                            markerdetail.put(markerid, imagename);
                            markerdetail.put(imagename,captiondetail);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Seelovables(){
        String user=auth.getCurrentUser().getUid();
        pRef.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,String> pdetail= (Map)dataSnapshot.getValue();
                    pmob = pdetail.get("Mobileno");
                    //Log.v("MSGLoc",pmob);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot cmp:dataSnapshot.getChildren()){
                        Map<String,String> mdetail = (Map) cmp.getValue();
                        cpmob = mdetail.get("ParentMobno");
                        if(pmob.equals(cpmob)){

                            cid = cmp.getKey();
                            //Log.v("MSGLoc",cid);
                            Intent lovableintent = new Intent(MapsActivity.this,SeeLovable.class);
                            lovableintent.putExtra("cid",cid);
                            startActivity(lovableintent);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //String childid = cid;
        //Bundle extras = new Bundle();
        //extras.putString("cid",childid);
        //lovableintent.putExtras(extras);
        //lovableintent.putExtra("Hi","Hello");



    }
    public void showMyPosts(){
        markerdetail=new HashMap<String, String>();
        sRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Databaseresponse","data: "+dataSnapshot.getValue());
                if(dataSnapshot.exists()){
                    for(DataSnapshot sid:dataSnapshot.getChildren()){
                        Map<String,String> detail=(Map) sid.getValue();

                        String uid = detail.get("Userid");
                        String cuid = auth.getCurrentUser().getUid();
                        if(uid.equals(cuid)) {
                            String slat = detail.get("Storylatitude");
                            String slong = detail.get("Storylongitude");
                            String gb = detail.get("Good_Bad");
                            String captiondetail = detail.get("CaptionDetail");
                            Double Storylat = 0.0, Storylong = 0.0;
                            if (slat != null && slong != null) {
                                Storylat = Double.parseDouble(slat);
                                Storylong = Double.parseDouble(slong);
                            }
                            String imagename = detail.get("ImagePath");
                            LatLng newmarker = new LatLng(Storylat, Storylong);
                            String markerid;
                            if (gb != null) {
                                if (gb.equals("good")) {
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(newmarker).title(captiondetail).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    markerid = marker.getId();
                                } else {
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(newmarker).title(captiondetail).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                    markerid = marker.getId();
                                }
                                Log.v("DETAILS", "StoryLatitude" + slat);
                                Log.v("DETAILS", "StoryLongitude" + slong);
                                Log.v("DETAILS", "GOODBad" + gb);
                                markerdetail.put(markerid, imagename);
                                markerdetail.put(imagename, captiondetail);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private FloatingActionButton getFAB() {
//        Context context = new android.support.v7.internal.view.ContextThemeWrapper(getContext(), R.style.AppTheme);
//        FloatingActionButton fab = new FloatingActionButton(context);
//        return fab;
//    }
//    private void userLocationFAB(){
//        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
//        FAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MapsActivity.this, "Click", Toast.LENGTH_SHORT).show();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 15));
//            }
//        });
//    }
}

