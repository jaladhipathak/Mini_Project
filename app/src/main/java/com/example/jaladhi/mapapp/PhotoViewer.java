package com.example.jaladhi.mapapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PhotoViewer extends AppCompatActivity {

    String imgPath,captionDetail;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        imgPath=getIntent().getStringExtra("ImagePath");
        captionDetail=getIntent().getStringExtra("Caption");
        imageView = (ImageView) findViewById(R.id.viewimage);
        //Log.v("msgx",sRef.getDownloadUrl().toString());
        Picasso.get().load(imgPath).into(imageView);
        textView = (TextView) findViewById(R.id.viewcaption);
        textView.setText(captionDetail);
    }
}
