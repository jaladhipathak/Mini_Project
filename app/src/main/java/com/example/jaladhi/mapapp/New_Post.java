package com.example.jaladhi.mapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class New_Post extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__post);
        imageButton=(ImageButton)findViewById(R.id.cameraicon);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Intent nintent=new Intent(New_Post.this,Show_Image_Preview.class);
            if(extras!=null){
                nintent.putExtras(extras);
                startActivity(nintent);
            }
            else{
                Toast tost= Toast.makeText(getApplicationContext(),"Please Select a Picture",Toast.LENGTH_SHORT);
                tost.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
                tost.show();
            }

        }
    }
}
