package com.example.cameraapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TEG = "DatabaseHelper";
    public static final String Table_Name = "pictures";
    public static final String column01 = "ID";
    public static final String column02 = "Pics";



    public DatabaseHelper(Context context) {
        super(context, Table_Name, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Table_Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + column02 + " TEXT)";
        db.execSQL(createTable);
    }

    public boolean addData(Bitmap img){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] data = getBitmapAsByteArray(img);
        ContentValues contentValues = new ContentValues();
        contentValues.put(column02, data);

        Log.d(TAG, "addData, Adding " + "picture" + " to " + Table_Name);
        long result = db.insert(Table_Name, null, contentValues);

        //If data is inserted incorrectly it will return -1
        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public Bitmap getImage(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + column02+ " FROM " + Table_Name + " WHERE " + column01 + " = " + i;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            byte[] imgByte = cursor.getBlob(0);
            cursor.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return null ;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
