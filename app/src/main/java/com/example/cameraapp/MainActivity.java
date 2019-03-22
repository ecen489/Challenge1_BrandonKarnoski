package com.example.cameraapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button takePicture, loadPicture;
    EditText picID;
    private static final int REQ_CODE_PICTURE = 2048;
    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePicture = (Button) findViewById(R.id.pictureButton);
        loadPicture = (Button) findViewById(R.id.pictureRecover);
        picID = (EditText) findViewById(R.id.picID);
        mDatabaseHelper = new DatabaseHelper(this);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //Take the picture and display
                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pictureIntent, REQ_CODE_PICTURE);

            }
        });
        loadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = Integer.parseInt(picID.getText().toString());
                Bitmap bmp = mDatabaseHelper.getImage(ID);
                ImageView img = (ImageView) findViewById(R.id.cameraPicture);
                img.setImageBitmap(bmp);
            }
        });

    }

    public void AddData(Bitmap img){
        boolean insertData = mDatabaseHelper.addData(img);
        if(insertData){
            toastMessage("Picture Stored");
        }else{
            toastMessage("Save Failed");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == REQ_CODE_PICTURE && resultCode == RESULT_OK){
            Bitmap bmp = (Bitmap) intent.getExtras().get("data");
            ImageView img = (ImageView) findViewById(R.id.cameraPicture);
            img.setImageBitmap(bmp);
            AddData(bmp);


        }
    }


}
