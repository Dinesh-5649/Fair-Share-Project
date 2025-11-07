package com.example.fairshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyDatabase extends SQLiteOpenHelper {

    Context context;
    public static final String DATABASE_NAME = "my_database.db";
    public static final int DATABASE_VERSION = 1; //

    // TABLE: Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";



    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL);";


        db.execSQL(createUsersTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys = OFF;");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        db.execSQL("PRAGMA foreign_keys = ON;");

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    // ============================
    //  USERS - LOGIN / SIGNUP
    // ============================

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean loginSuccess = cursor.getCount() > 0;
        cursor.close();
        db.close();

        if (loginSuccess) {
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }

        return loginSuccess;
    }
}
