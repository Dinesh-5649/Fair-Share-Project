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
    public static final int DATABASE_VERSION = 2; //

    // TABLE: Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // TABLE: Groups
    public static final String TABLE_GROUPS = "group_table";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_GROUP_NAME = "group_name";



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

        // Create Groups Table
        String createGroupsTable = "CREATE TABLE " + TABLE_GROUPS + " (" +
                COLUMN_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GROUP_NAME + " TEXT UNIQUE NOT NULL);";


        db.execSQL(createUsersTable);
        db.execSQL(createGroupsTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);

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

    //  Create group
    // ============================
    public long addGroup(String groupName, String creatorUsername) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check for the duplicate group name
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP_NAME + "=?",
                new String[]{groupName}
        );
        if (cursor.getCount() > 0) {
            Toast.makeText(context, "Group name already exists!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return -1;
        }
        cursor.close();
        // Create group
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROUP_NAME, groupName);

        long groupId = db.insert(TABLE_GROUPS, null, cv);

        if (groupId == -1) {
            Toast.makeText(context, "Failed to add group", Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            Toast.makeText(context, "Group created successfully", Toast.LENGTH_SHORT).show();

            return groupId;
        }
    }
//
    //Find user id by user name
    //  Helper method to find UserID by UserName    //

    public Integer getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?",
                new String[]{username}
        );

        Integer userId = null;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }

        cursor.close();

        return userId;
    }

    //get the groups by for the user
    public ArrayList<String> getGroupsForUser(String username) {
        ArrayList<String> groupList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Integer userId = getUserIdByUsername(username);
        if (userId == null) {
            Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show();
            return groupList;
        }

        String query = "SELECT " + COLUMN_GROUP_NAME + " FROM " + TABLE_GROUPS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String groupName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME));
                groupList.add(groupName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groupList;
    }

    // Get the group Id by Group name
    public int getGroupIdByGroupName(String groupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_GROUP_ID + " FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{groupName});
        int groupId = 0;
        if (cursor.moveToFirst()) {
            groupId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GROUP_ID));
        }
        db.close();
        cursor.close();
        return groupId;
    }


}
