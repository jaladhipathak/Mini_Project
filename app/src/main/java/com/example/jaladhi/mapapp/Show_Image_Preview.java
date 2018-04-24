package com.example.jaladhi.mapapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Show_Image_Preview extends AppCompatActivity {
    ImageView imageView;
    private RadioButton good;
    private RadioButton bad;
    ImageButton sendbutton;
    EditText editText;
    File finalfile;
    String finalpath;
    String good_bad;
    private StorageReference storageReference;
    private FirebaseDatabase ref;
    private DatabaseReference sRef;
    private FirebaseAuth auth;
    String latitude,longitude,caption;
    String imageid,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__image__preview);
        imageView=(ImageView) findViewById(R.id.preview);
        good=(RadioButton)findViewById(R.id.good);
        bad=(RadioButton)findViewById(R.id.Bad);
        auth=FirebaseAuth.getInstance();
        sendbutton = (ImageButton)findViewById(R.id.sendbutton);
        editText = (EditText)findViewById(R.id.captiondetail);
        storageReference = FirebaseStorage.getInstance().getReference();
        ref=FirebaseDatabase.getInstance();
        sRef=ref.getReference("Story_detail");

        latitude=getIntent().getStringExtra("lat");
        longitude=getIntent().getStringExtra("long");

        String mode=getIntent().getStringExtra("mode");
        if(mode.equals("c")){
            Bundle extras = getIntent().getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            finalpath=getRealPathFromURI(tempUri);
            finalfile = new File(finalpath);

        }
        else if(mode.equals("g")){
            String path=getIntent().getStringExtra("path");
            finalpath=path;
            finalfile=new File(finalpath);
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        }

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bad.isChecked())
                    bad.setChecked(false);
                good_bad="good";
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(good.isChecked())
                    good.setChecked(false);
                good_bad="bad";
            }
        });
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalpath!=null){
                    uploadfile();
                }
            }
        });

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String str=cursor.getString(idx);
        cursor.close();
        return str;
    }

    public void uploadfile(){

        uid= auth.getCurrentUser().getUid();
        imageid=uid+finalpath;
        imageid=imageid.substring(0,imageid.length()-4);
        imageid=imageid.replaceAll("\\/","");
        caption=editText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        Uri file=Uri.fromFile(finalfile);
        StorageReference reference = storageReference.child("images/"+imageid);
        reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef.child(imageid);
                sRef.child(imageid).child("Userid").setValue(uid);
                sRef.child(imageid).child("ImagePath").setValue(taskSnapshot.getDownloadUrl().toString());
                sRef.child(imageid).child("CaptionDetail").setValue(caption);
                sRef.child(imageid).child("Storylatitude").setValue(latitude);
                sRef.child(imageid).child("Storylongitude").setValue(longitude);
                sRef.child(imageid).child("Good_Bad").setValue(good_bad);
                progressDialog.dismiss();
                Toast.makeText(Show_Image_Preview.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Intent successintent = new Intent(Show_Image_Preview.this,MapsActivity.class);
                startActivity(successintent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                Intent failureintent = new Intent(Show_Image_Preview.this,MapsActivity.class);
                startActivity(failureintent);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });
    }
}
