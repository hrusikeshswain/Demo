package com.alhafeez.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database configuration
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "assignment";
    private static final String TABLE_LOGIN = "user";

    // Login table column names
    private static final String KEY_ID = "key_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PASSWORD = "user_password";



    // Creating database
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // User login table
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT"+ ")";

        db.execSQL(CREATE_LOGIN_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete all rows from tables
        db.delete(TABLE_LOGIN, null, null);
        //db.delete(TABLE_BOOKS, null, null);
        //db.delete(TABLE_CHAPTERS, null, null);
        db.close();
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PASSWORD, password); // Password
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    /**
     * Getting user data from database
     * */
   /* public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("password", cursor.getString(2));
        }

        cursor.close();
        db.close();

        return user;
    }*/

    public ArrayList<HashMap<String, String>> getAllUsers(){
        ArrayList<HashMap<String,String>> arList = new ArrayList<HashMap<String,String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                HashMap<String, String> users = new HashMap<String, String>();
                users.put("username", cursor.getString(1));
                users.put("password", cursor.getString(2));
                arList.add(users);
            }
        }

        cursor.close();
        db.close();

        return arList;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }


}