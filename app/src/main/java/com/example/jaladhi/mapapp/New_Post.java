package com.example.jaladhi.mapapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class New_Post extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMG = 2;
    ImageButton imageButton;
    ImageButton gallerybutton;
    String imgstring;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__post);
        imageButton = (ImageButton) findViewById(R.id.cameraicon);
        gallerybutton = (ImageButton) findViewById(R.id.galleryicon);
        latitude=getIntent().getStringExtra("lat");
        longitude=getIntent().getStringExtra("long");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadimageintent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(loadimageintent, RESULT_LOAD_IMG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Intent nintent = new Intent(New_Post.this, Show_Image_Preview.class);
                if (extras != null) {
                    nintent.putExtras(extras);
                    nintent.putExtra("mode","c");
                    nintent.putExtra("lat",latitude);
                    nintent.putExtra("long",longitude);
                    startActivity(nintent);
                }

            } else if (requestCode == RESULT_LOAD_IMG && data != null) {
                if (true) {
                    Uri selectedimage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedimage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgstring = cursor.getString(columnIndex);
                    cursor.close();
                    Intent nintent = new Intent(New_Post.this, Show_Image_Preview.class);
                    nintent.putExtra("mode","g");
                    nintent.putExtra("path", imgstring);
                    nintent.putExtra("lat",latitude);
                    nintent.putExtra("long",longitude);
                    startActivity(nintent);
                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
