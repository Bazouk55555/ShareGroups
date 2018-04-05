package com.application.bazouk.spymyfriends.sqliteservices.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnectionHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "Id";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String FIRST_NAME = "First_Name";
    public static final String LAST_NAME = "Last_Name";

    public static final String TABLE_CONNECTION = "CONNECTION";
    public static final String TABLE_CONNECTION_CREATE = "CREATE TABLE " + TABLE_CONNECTION + " (" + ID_KEY
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT, " + PASSWORD + " TEXT, " + FIRST_NAME + " TEXT, "+ LAST_NAME + " TEXT);";
    public static final String TABLE_CONNECTION_DROP = "DROP TABLE IF EXISTS " + TABLE_CONNECTION + ";";

    private final static String DATABASE = "database_connection";
    private final static int VERSION = 1;

    public DatabaseConnectionHandler(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CONNECTION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CONNECTION_DROP);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CONNECTION_DROP);
        onCreate(db);
    }
}

