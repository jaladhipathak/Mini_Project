package com.example.jaladhi.mapapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Show_Image_Preview extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__image__preview);
        imageView=(ImageView) findViewById(R.id.preview);
        String mode=getIntent().getStringExtra("mode");
        if(mode.equals("c")){
            Bundle extras = getIntent().getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        else if(mode.equals("g")){
            String path=getIntent().getStringExtra("path");
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        }

    }
}
