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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyDatabase extends SQLiteOpenHelper {

    Context context;
    public static final String DATABASE_NAME = "my_database.db";
    public static final int DATABASE_VERSION = 14;

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

    // Messages
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_GROUP_REF_ID_FOR_MESSAGE = "group_id";
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // TABLE: Expenses
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "expense_id";
    public static final String COLUMN_EXPENSE_GROUP_ID = "expense_group_id";
    public static final String COLUMN_EXPENSE_PAID_BY = "expense_paid_by"; // store member_name (String)
    public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
    public static final String COLUMN_EXPENSE_TYPE = "expense_type"; // "equal","custom","percentage"
    public static final String COLUMN_EXPENSE_DESC = "expense_description";
    public static final String COLUMN_EXPENSE_TIMESTAMP = "expense_timestamp";

    // TABLE: Expense Shares
    public static final String TABLE_EXPENSE_SHARES = "expense_shares";
    public static final String COLUMN_SHARE_ID = "share_id";
    public static final String COLUMN_EXPENSE_ID_REF = "expense_id";
    public static final String COLUMN_MEMBER_ID = "member_id";
    public static final String COLUMN_SHARE_AMOUNT = "share_amount";
    public static final String COLUMN_PAID_STATUS = "paid_status";
    public static final String COLUMN_PAID_TIME = "paid_time";


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

                "FOREIGN KEY(" + COLUMN_GROUP_REF_ID_FOR_MEMBERS + ") REFERENCES " + TABLE_GROUPS + "(" + COLUMN_GROUP_ID + ") ON DELETE CASCADE);";

        // Create Message Table
        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GROUP_REF_ID_FOR_MESSAGE + " INTEGER, " +
                COLUMN_SENDER_ID + " INTEGER, " +
                COLUMN_MESSAGE_TEXT + " TEXT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_GROUP_REF_ID_FOR_MESSAGE + ") REFERENCES " + TABLE_GROUPS + "(" + COLUMN_GROUP_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_SENDER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE);";

        String createExpensesTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_GROUP_ID + " INTEGER NOT NULL, " +
                COLUMN_EXPENSE_PAID_BY + " TEXT NOT NULL, " +
                COLUMN_EXPENSE_AMOUNT + " REAL NOT NULL, " +
                COLUMN_EXPENSE_TYPE + " TEXT NOT NULL, " +
                COLUMN_EXPENSE_DESC + " TEXT, " +
                COLUMN_EXPENSE_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(" + COLUMN_EXPENSE_GROUP_ID + ") REFERENCES " + TABLE_GROUPS + "(" + COLUMN_GROUP_ID + ") ON DELETE CASCADE);";

        String createExpenseSharesTable = "CREATE TABLE " + TABLE_EXPENSE_SHARES + " (" +
                COLUMN_SHARE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_ID_REF + " INTEGER NOT NULL, " +
                COLUMN_MEMBER_ID + " INTEGER NOT NULL, " +
                COLUMN_SHARE_AMOUNT + " REAL NOT NULL, " +
                COLUMN_PAID_STATUS + " INTEGER NOT NULL, " +
                COLUMN_PAID_TIME + " DATETIME, " +
                "FOREIGN KEY(" + COLUMN_EXPENSE_ID_REF + ") REFERENCES " +
                TABLE_EXPENSES + "(" + COLUMN_EXPENSE_ID + ") ON DELETE CASCADE);";

        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(createUsersTable);
        db.execSQL(createGroupsTable);
        db.execSQL(createMembersTable);
        db.execSQL(createMessagesTable);
        db.execSQL(createExpensesTable);
        db.execSQL(createExpenseSharesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_SHARES);

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

    /// /////// Get the group creator name by group ID

    public String getGroupCreator(int groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery( "SELECT " + COLUMN_GROUP_CREATOR + " FROM " + TABLE_GROUPS + " WHERE " + COLUMN_GROUP_ID + " = ?",
                new String[]{String.valueOf(groupId)});

        String groupCreator="";
        if (c.moveToFirst()) {
            groupCreator = c.getString(c.getColumnIndexOrThrow(COLUMN_GROUP_CREATOR));
        }
        c.close();
        return groupCreator;

    }

    /// / Delete a member from a group
    public void deleteMemberFromGroup(String memberName, int groupId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(
                TABLE_MEMBERS,
                COLUMN_MEMBER_NAME + "=? AND " + COLUMN_GROUP_REF_ID_FOR_MEMBERS + "=?",
                new String[]{memberName, String.valueOf(groupId)}
        );
        if(result==-1){
            Toast.makeText(context,"Failed to exit", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(context,"Exited successfully", Toast.LENGTH_SHORT).show();

        ///     ////////////////////////////
        ContentValues cv = new ContentValues();

        String where = COLUMN_GROUP_ID + " =?";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());

        cv.put(COLUMN_GROUP_UPDATED_TIME, currentTime);
        db.update(TABLE_USERS, cv, where, new String[]{String.valueOf(groupId)});

        db.close();
    }

    /// // Delete group
    public void deleteGroup( int groupId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result= db.delete(
                TABLE_GROUPS,
                COLUMN_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)}
        );
        if(result==-1){
            Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(context,"Deleted Successfully", Toast.LENGTH_SHORT).show();

        db.close();
    }
    //////// Send Messages
    public void addMessage(int groupId, int senderId, String messageText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUP_REF_ID_FOR_MESSAGE, groupId);
        values.put(COLUMN_SENDER_ID, senderId);
        values.put(COLUMN_MESSAGE_TEXT, messageText);

        // Store Indian time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());
        values.put(COLUMN_TIMESTAMP, currentTime);

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    // Show all group messages
    public ArrayList<Message> getMessagesByGroup(int groupId) {
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT u." + COLUMN_USERNAME + ", " +
                "m." + COLUMN_MESSAGE_TEXT + ", " +
                "m." + COLUMN_TIMESTAMP +
                " FROM " + TABLE_MESSAGES + " m " +
                " JOIN " + TABLE_USERS + " u ON m." + COLUMN_SENDER_ID + " = u." + COLUMN_USER_ID +
                " WHERE m." + COLUMN_GROUP_REF_ID_FOR_MESSAGE + " = ?" +
                " ORDER BY m." + COLUMN_TIMESTAMP + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

                Message message = new Message(sender, text, time);
                messages.add(message);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return messages;
    }
    // Insert an expense
    public boolean addExpense(int groupId, String paidByMemberName, double amount, String type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSE_GROUP_ID, groupId);
        cv.put(COLUMN_EXPENSE_PAID_BY, paidByMemberName);
        cv.put(COLUMN_EXPENSE_AMOUNT, amount);
        cv.put(COLUMN_EXPENSE_TYPE, type);
        cv.put(COLUMN_EXPENSE_DESC, description);

        long result = db.insert(TABLE_EXPENSES, null, cv);
        db.close();
        return result != -1;
    }

    // Get all expenses for a group
    public ArrayList<Expense> getExpensesByGroup(int groupId) {
        ArrayList<Expense> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_EXPENSE_GROUP_ID + " = ? ORDER BY " + COLUMN_EXPENSE_TIMESTAMP + " DESC";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(groupId)});
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_EXPENSE_ID));
                String paidBy = c.getString(c.getColumnIndexOrThrow(COLUMN_EXPENSE_PAID_BY));
                double amount = c.getDouble(c.getColumnIndexOrThrow(COLUMN_EXPENSE_AMOUNT));
                String type = c.getString(c.getColumnIndexOrThrow(COLUMN_EXPENSE_TYPE));
                String desc = c.getString(c.getColumnIndexOrThrow(COLUMN_EXPENSE_DESC));
                String time = c.getString(c.getColumnIndexOrThrow(COLUMN_EXPENSE_TIMESTAMP));

                Expense e = new Expense(id, groupId, paidBy, amount, type, desc, time);
                list.add(e);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
    //Insert Expense Shares
    public void insertExpenseShare(int expenseId, int memberId, double shareAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSE_ID_REF, expenseId);
        cv.put(COLUMN_MEMBER_ID, memberId);
        cv.put(COLUMN_SHARE_AMOUNT, shareAmount);
        cv.put(COLUMN_PAID_STATUS, 0);
        db.insert(TABLE_EXPENSE_SHARES, null, cv);
        db.close();
    }
    //Get shares for an expense (for display later)
    public Cursor getExpenseShares(int expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT m." + COLUMN_MEMBER_NAME + ", s." + COLUMN_SHARE_AMOUNT +
                        " FROM " + TABLE_EXPENSE_SHARES + " s " +
                        " JOIN " + TABLE_MEMBERS + " m ON s." + COLUMN_MEMBER_ID + " = m." + COLUMN_MEMBERS_ID +
                        " WHERE s." + COLUMN_EXPENSE_ID + "=?",
                new String[]{String.valueOf(expenseId)}
        );
    }

    // Get last inserted expense id
    public int getLastExpenseId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT expense_id FROM expenses ORDER BY expense_id DESC LIMIT 1",
                null
        );
        int id = -1;
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }
        c.close();
        return id;
    }

    // Get member ID by name & group
    public int getMemberIdByName(String name, int groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT member_id FROM members_table WHERE member_name=? AND group_reference=?",
                new String[]{name, String.valueOf(groupId)}
        );

        int id = -1;
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }
        c.close();
        return id;
    }

    public String getMemberNameById(int memberId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT member_name FROM members_table WHERE member_id=?",
                new String[]{String.valueOf(memberId)}
        );

        String memberName ="";
        if (c.moveToFirst()) {
           memberName = c.getString(c.getColumnIndexOrThrow(COLUMN_MEMBER_NAME));

        }
        c.close();
        return memberName;
    }

    public boolean updatePaidStatus(int memberId, int expenseId ) {


        /// Check if the expense exist
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSE_SHARES + " WHERE " + COLUMN_MEMBER_ID + " =? AND " + COLUMN_EXPENSE_ID_REF +" =?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(memberId),String.valueOf(expenseId)});
        if (c.getCount() == 0) {
            Toast.makeText(context, "Expense does not exist!", Toast.LENGTH_LONG).show();
            c.close();
            db.close();
            return false;
        } else {
            if (c.moveToFirst()){
                int paidStatus = c.getInt(c.getColumnIndexOrThrow(COLUMN_PAID_STATUS));
                if(paidStatus==1) return false;
            }
            c.close();
        }


        //update paid status
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PAID_STATUS, 1);
        String where = COLUMN_MEMBER_ID + " =? AND " + COLUMN_EXPENSE_ID_REF +" =?";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String currentTime = sdf.format(new Date());

        cv.put(COLUMN_PAID_TIME, currentTime);
        int result = db.update(TABLE_EXPENSE_SHARES, cv, where, new String[]{String.valueOf(memberId),String.valueOf(expenseId)});
        db.close();
        return result == 1;
    }


    public ArrayList<ExpenseShare> getExpenseShareByExpense(int expenseRefId) {
        ArrayList<ExpenseShare> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EXPENSE_SHARES + " WHERE " + COLUMN_EXPENSE_ID_REF + " = ?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(expenseRefId)});
        if (c.moveToFirst()) {
            do {
                int expenseIdRef = c.getInt(c.getColumnIndexOrThrow(COLUMN_EXPENSE_ID_REF));
                double shareAmount = c.getDouble(c.getColumnIndexOrThrow(COLUMN_SHARE_AMOUNT));
                int memberId = c.getInt(c.getColumnIndexOrThrow(COLUMN_MEMBER_ID));
                int paidStatus = c.getInt(c.getColumnIndexOrThrow(COLUMN_PAID_STATUS));
                String memberName = getMemberNameById(memberId);

                ExpenseShare e = new ExpenseShare(expenseIdRef, memberId, shareAmount, paidStatus, memberName);
                list.add(e);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public boolean updateProfile(String newEmail, String newPhone) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check duplicate email or phone
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? OR " + COLUMN_NUMBER + "=?",
                new String[]{newEmail, newPhone}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL, newEmail);
        cv.put(COLUMN_NUMBER, newPhone);

        // TEMP: update user 1 (later use login session)
        db.update(TABLE_USERS, cv, COLUMN_USER_ID + "=?", new String[]{"1"});
        return true;
    }

    // Delete user account

    public boolean deleteAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        cursor.close();

        db.delete(TABLE_USERS, COLUMN_USERNAME + "=?", new String[]{username});
        return true;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USERNAME + ", " +
                COLUMN_NUMBER + ", " +
                COLUMN_EMAIL + ", " +
                COLUMN_AGE +
                " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=?";

        Cursor c = db.rawQuery(query, new String[]{username});

        if (c.moveToFirst()) {
            String uname = c.getString(0);
            String number = c.getString(1);
            String email = c.getString(2);
            int age = c.getInt(3);

            c.close();
            return new User(uname, number, email, age);
        }

        c.close();
        return null;
    }

    public boolean updateUser(String oldName, String newName, String phone, String email, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, newName);
        cv.put(COLUMN_NUMBER, phone);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_AGE, age);

        // Update timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        cv.put(COLUMN_UPDATED_TIME, sdf.format(new Date()));

        int rows = db.update(TABLE_USERS, cv, COLUMN_USERNAME + "=?", new String[]{oldName});
        return rows > 0;
    }

    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?",
                new String[]{username}
        );
    }

    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT email_id FROM users WHERE username=?",
                new String[]{username}
        );
        String email = "";
        if (c.moveToFirst()) {
            email = c.getString(0);
        }
        c.close();
        return email;
    }

    public int getAgeByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT age FROM users WHERE username=?",
                new String[]{username}
        );
        int age = 0;
        if (c.moveToFirst()) {
            age = c.getInt(0);
        }
        c.close();
        return age;
    }
    public HashMap<String, Float> getExpenseSplitByGroup(int groupId) {

        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Float> expenseMap = new HashMap<>();

        Cursor c = db.rawQuery(
                "SELECT " + COLUMN_EXPENSE_PAID_BY + ", SUM(" + COLUMN_EXPENSE_AMOUNT + ") " +
                        "FROM " + TABLE_EXPENSES +
                        " WHERE " + COLUMN_EXPENSE_GROUP_ID + " = ? " +
                        "GROUP BY " + COLUMN_EXPENSE_PAID_BY,
                new String[]{String.valueOf(groupId)}
        );

        if (c.moveToFirst()) {
            do {
                String user = c.getString(0);
                float total = c.getFloat(1);
                expenseMap.put(user, total);
            } while (c.moveToNext());
        }

        c.close();
        return expenseMap;
    }
    public HashMap<String, Float> getPaidVsRemaining(int expenseId) {

        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Float> map = new HashMap<>();

        Cursor paid = db.rawQuery(
                "SELECT SUM(" + COLUMN_SHARE_AMOUNT + ") FROM " + TABLE_EXPENSE_SHARES +
                        " WHERE " + COLUMN_EXPENSE_ID_REF + "=? AND " + COLUMN_PAID_STATUS + "=1",
                new String[]{String.valueOf(expenseId)}
        );

        Cursor remaining = db.rawQuery(
                "SELECT SUM(" + COLUMN_SHARE_AMOUNT + ") FROM " + TABLE_EXPENSE_SHARES +
                        " WHERE " + COLUMN_EXPENSE_ID_REF + "=? AND " + COLUMN_PAID_STATUS + "=0",
                new String[]{String.valueOf(expenseId)}
        );

        float paidAmt = (paid.moveToFirst() && !paid.isNull(0)) ? paid.getFloat(0) : 0;
        float remainingAmt = (remaining.moveToFirst() && !remaining.isNull(0)) ? remaining.getFloat(0) : 0;

        map.put("Paid", paidAmt);
        map.put("Remaining", remainingAmt);

        paid.close();
        remaining.close();
        return map;
    }

    public HashMap<String, Float> getRemainingByMember(int expenseId) {

        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Float> map = new HashMap<>();

        Cursor c = db.rawQuery(
                "SELECT m." + COLUMN_MEMBER_NAME + ", SUM(s." + COLUMN_SHARE_AMOUNT + ") " +
                        "FROM " + TABLE_EXPENSE_SHARES + " s " +
                        "JOIN " + TABLE_MEMBERS + " m ON s." + COLUMN_MEMBER_ID + " = m." + COLUMN_MEMBERS_ID +
                        " WHERE s." + COLUMN_EXPENSE_ID_REF + "=? AND s." + COLUMN_PAID_STATUS + "=0 " +
                        "GROUP BY m." + COLUMN_MEMBER_NAME,
                new String[]{String.valueOf(expenseId)}
        );

        if (c.moveToFirst()) {
            do {
                map.put(c.getString(0), c.getFloat(1));
            } while (c.moveToNext());
        }
        c.close();
        return map;
    }

}