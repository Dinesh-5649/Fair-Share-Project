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
    public static final int DATABASE_VERSION = 13;

    // TABLE: Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_EMAIL = "email_id";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_UPDATED_TIME = "updated_time";


    // TABLE: Groups
    public static final String TABLE_GROUPS = "group_table";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_GROUP_NAME = "group_name";
    public static final String COLUMN_GROUP_CREATOR = "group_creator";
    public static final String COLUMN_GROUP_UPDATED_TIME = "group_updated_time";
    public static final String COLUMN_GROUP_UPDATED_BY = "group_updated_by";

    /// Members
    public static final String TABLE_MEMBERS = "members_table";
    public static final String COLUMN_MEMBERS_ID = "member_id";
    public static final String COLUMN_MEMBER_NAME = "member_name";
    public static final String COLUMN_MEMBER_NUMBER = "phone_number";
    public static final String COLUMN_GROUP_REF_ID_FOR_MEMBERS = "group_reference";

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
                COLUMN_NUMBER + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_AGE + " INTEGER NOT NULL, " +
                COLUMN_GENDER + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                COLUMN_UPDATED_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        // Create Groups Table
        String createGroupsTable = "CREATE TABLE " + TABLE_GROUPS + " (" +
                COLUMN_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GROUP_NAME + " TEXT NOT NULL, " +
                COLUMN_GROUP_CREATOR + " TEXT NOT NULL, " +
                COLUMN_GROUP_UPDATED_BY + " TEXT NOT NULL, " +
                COLUMN_GROUP_UPDATED_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        // Create Members Table
        String createMembersTable = "CREATE TABLE " + TABLE_MEMBERS + " (" +
                COLUMN_MEMBERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GROUP_REF_ID_FOR_MEMBERS + " INTEGER NOT NULL, " +
                COLUMN_MEMBER_NAME + " TEXT NOT NULL, "+
                COLUMN_MEMBER_NUMBER +" TEXT NOT NULL, " +

                "FOREIGN KEY(" + COLUMN_GROUP_REF_ID_FOR_MEMBERS + ") REFERENCES " + TABLE_GROUPS + "(" + COLUMN_GROUP_ID + "));";


        db.execSQL(createUsersTable);
        db.execSQL(createGroupsTable);
        db.execSQL(createMembersTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
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

    public boolean registerUser(String username, String number, String password, int age, String gender, String email) {

        if (username.isEmpty() || number.isEmpty() || password.isEmpty() || username == null || number == null || password == null) {
            Toast.makeText(context, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (number.length() != 10) {
            Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_NUMBER, number);
        cv.put(COLUMN_PASSWORD, password);
        cv.put(COLUMN_AGE, age);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_EMAIL, email);

        //Get current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());
        cv.put(COLUMN_UPDATED_TIME, currentTime);

        long result = db.insert(TABLE_USERS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Please enter a different User name or Number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    /// Update password
    public boolean updatePassword(String number, String newPassword) {

        //  Validate the input (e.g., check for nulls or length)
        if (number == null || newPassword == null || newPassword.isEmpty() || number.isEmpty()) {
            Toast.makeText(context, "Please enter both details", Toast.LENGTH_LONG).show();
            return false;
        }

        /// Check if the user exist
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_NUMBER + " =?";
        Cursor c = db.rawQuery(query, new String[]{number});
        if (c.getCount() == 0) {
            Toast.makeText(context, "User does not exist in this Number", Toast.LENGTH_LONG).show();
            c.close();
            db.close();
            return false;
        } else {
            c.close();
        }

        /// Check if new password is the same
        String query1 = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_NUMBER + " =? AND " + COLUMN_PASSWORD + " =?";
        Cursor c1 = db.rawQuery(query1, new String[]{number, newPassword});
        if (c1.getCount() > 0) {
            Toast.makeText(context, "New Password cannot be the same", Toast.LENGTH_LONG).show();
            c1.close();
            db.close();
            return false;
        } else {
            c1.close();
        }

        //update password
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, newPassword);
        String where = COLUMN_NUMBER + " =?";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());

        cv.put(COLUMN_UPDATED_TIME, currentTime);
        int result = db.update(TABLE_USERS, cv, where, new String[]{number});
        db.close();
        return result == 1;
    }


    //  Create group
    // ============================
    public boolean addGroup(String groupName, String creatorUsername) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (groupName.isEmpty() || groupName == null) {
            Toast.makeText(context, "Please enter the group name", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for the duplicate group name
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP_NAME + "=?",
                new String[]{groupName}
        );
        if (cursor.getCount() > 0) {
            Toast.makeText(context, "Group name already exists!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }
        cursor.close();
        // Create group
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROUP_NAME, groupName);
        cv.put(COLUMN_GROUP_CREATOR, creatorUsername);
        cv.put(COLUMN_GROUP_UPDATED_BY, creatorUsername);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());
        cv.put(COLUMN_GROUP_UPDATED_TIME, currentTime);

        long groupId = db.insert(TABLE_GROUPS, null, cv);

        if (groupId == -1) {
            Toast.makeText(context, "Failed to add group", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "Group created successfully", Toast.LENGTH_SHORT).show();

            return true;
        }
    }
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

        // Step 1: Get group IDs from members_table
        String query = "SELECT DISTINCT " + COLUMN_GROUP_REF_ID_FOR_MEMBERS +
                " FROM " + TABLE_MEMBERS +
                " WHERE " + COLUMN_MEMBER_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username});

        ArrayList<Integer> groupIds = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                groupIds.add(cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_GROUP_REF_ID_FOR_MEMBERS)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Step 2: Fetch group names using those IDs
        for (int groupId : groupIds) {
            Cursor groupCursor = db.rawQuery(
                    "SELECT " + COLUMN_GROUP_NAME +
                            " FROM " + TABLE_GROUPS +
                            " WHERE " + COLUMN_GROUP_ID + " = ?",
                    new String[]{String.valueOf(groupId)}
            );

            if (groupCursor.moveToFirst()) {
                groupList.add(groupCursor.getString(
                        groupCursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME)
                ));
            }
            groupCursor.close();
        }

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

    public boolean addMembers(String membersName, int groupId, String phoneNumber) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (membersName == null || membersName.isEmpty()) {
            Toast.makeText(context, "Please enter the member name", Toast.LENGTH_SHORT).show();
            return false;
        }

        // CHECK IF SAME MEMBER EXISTS IN SAME GROUP ONLY
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_MEMBERS +
                        " WHERE " + COLUMN_MEMBER_NUMBER + "=? AND " +
                        COLUMN_GROUP_REF_ID_FOR_MEMBERS + "=?",
                new String[]{phoneNumber, String.valueOf(groupId)}
        );

        if (cursor.getCount() > 0) {
            Toast.makeText(context, "Member already exists in this group!", Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }
        cursor.close();

        // INSERT MEMBER
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEMBER_NAME, membersName);
        cv.put(COLUMN_GROUP_REF_ID_FOR_MEMBERS, groupId);
        cv.put(COLUMN_MEMBER_NUMBER, phoneNumber);


        long result = db.insert(TABLE_MEMBERS, null, cv);

        return result!= -1;
    }


    // Helper method to find groupId by groupName

    public String getPhoneNumber(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery( "SELECT " + COLUMN_NUMBER + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?",
                new String[]{username});

        String PhoneNumber="";
        if (c.moveToFirst()) {
            PhoneNumber = c.getString(c.getColumnIndexOrThrow(COLUMN_NUMBER));
        }
        c.close();
        return PhoneNumber;

    }

    public ArrayList<String> getMembersByGroup(int groupId) {
        ArrayList<String> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_MEMBER_NAME +
                " FROM " + TABLE_MEMBERS +
                " WHERE " + COLUMN_GROUP_REF_ID_FOR_MEMBERS + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                members.add(cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_MEMBER_NAME)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return members;
    }

    public ArrayList<String> getAllUserNames() {
        ArrayList<String> usersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                usersList.add(username);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return usersList;
    }


}
