package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "login";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "fName";
    public static final String COLUMN_LAST_NAME = "lName";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTE = "note";

    // Database creation sql statement
    private static final String DATABASE_CREATE = " CREATE TABLE " +
            TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FIRST_NAME + " TEXT, " +
            COLUMN_LAST_NAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PASSWORD + " TEXT );";

    // Initialize the database object.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database with the database creation statement.
        db.execSQL(DATABASE_CREATE);
        db.execSQL(" CREATE TABLE "+TABLE_NOTES+" ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS '" + TABLE_USERS + "'");
        db.execSQL(" DROP TABLE IF EXISTS '" + TABLE_NOTES + "'");
        onCreate(db);
    }


    public boolean addUser(UserData users){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FIRST_NAME,users.getFirstName());
        contentValues.put(COLUMN_LAST_NAME,users.getLastName());
        contentValues.put(COLUMN_EMAIL,users.getEmail());
        contentValues.put(COLUMN_PASSWORD,users.getPassword());

        long result=db.insert(TABLE_USERS,null, contentValues);

        if (result==-1){
            return false;
        } else {
            return true;
        }

    }

    public boolean addNote(UserNote notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOTE, notes.getNote());

        long result = db.insert(TABLE_NOTES, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public String getLoginData(String email, String password)
    {
        SQLiteDatabase sql = this.getReadableDatabase();
        String query=" select count(*) from " + TABLE_USERS + " where email ='"+email+"' and password='"+password+"'";
        Cursor cursor =sql.rawQuery(query,null);
        cursor.moveToFirst();
        String count = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
        return count;

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NOTES +
                " WHERE " + COLUMN_NOTE + " = '" + note + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newNote, int id, String oldNote){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NOTES + " SET " + COLUMN_NOTE +
                " = '" + newNote + "' WHERE " + COLUMN_ID + " = '" + id + "'" +
                " AND " + COLUMN_NOTE + " = '" + oldNote + "'";

        Log.d("DBText", "updateName: query: " + query);

        Log.d("DBText", "updateName: Setting name to " + newNote);

        db.execSQL(query);
    }

    public void deleteName(int id, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTES + " WHERE "
                + COLUMN_ID + " = '" + id + "'" +
                " AND " + COLUMN_NOTE + " = '" + note + "'";

        Log.d("DBText", "deleteName: query: " + query);

        Log.d("DBText", "deleteName: Deleting " + note + " from database.");

        db.execSQL(query);
    }
}