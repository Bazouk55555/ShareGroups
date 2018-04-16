package com.application.bazouk.spymyfriends.sqliteservices.presencegroup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabasePresenceGroupHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "id_key";
    public static final String ID_GROUP = "id_group";
    public static final String USERNAME = "username";
    public static final String IS_PRESENT = "is_present";
    public static final String NAME_OF_THE_GROUP = "name_of_the_group";

    public static final String TABLE_USERNAME_GROUP = "GROUPS_OF_USERNAMES";
    public static final String TABLE_USERNAME_GROUP_CREATE = "CREATE TABLE " + TABLE_USERNAME_GROUP + " (" + ID_KEY
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_GROUP + " INTEGER, " + USERNAME + " TEXT, "+ IS_PRESENT + " INTEGER);";
    public static final String TABLE_USERNAME_GROUP_DROP = "DROP TABLE IF EXISTS " + TABLE_USERNAME_GROUP + ";";

    public static final String TABLE_NOTIFICATION_GROUP = "NOTIFICATION_PRESENCE_GROUP";
    public static final String TABLE_NOTIFICATION_GROUP_CREATE = "CREATE TABLE " + TABLE_NOTIFICATION_GROUP + " (" + ID_GROUP
            + " INTEGER PRIMARY KEY, " + USERNAME+ " TEXT);";
    public static final String TABLE_NOTIFICATION_GROUP_DROP = "DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_GROUP + ";";

    public static final String TABLE_PRESENCE_GROUP = "PRESENCE_GROUP";
    public static final String TABLE_PRESENCE_GROUP_CREATE = "CREATE TABLE " + TABLE_PRESENCE_GROUP + " (" + ID_GROUP
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_OF_THE_GROUP+ " TEXT);";
    public static final String TABLE_PRESENCE_GROUP_DROP = "DROP TABLE IF EXISTS " + TABLE_PRESENCE_GROUP + ";";


    private final static String DATABASE = "database_group";
    private final static int VERSION = 2;

    public DatabasePresenceGroupHandler(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PRESENCE_GROUP_CREATE);
        db.execSQL(TABLE_USERNAME_GROUP_CREATE);
        db.execSQL(TABLE_NOTIFICATION_GROUP_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_PRESENCE_GROUP_DROP);
        db.execSQL(TABLE_USERNAME_GROUP_DROP);
        db.execSQL(TABLE_NOTIFICATION_GROUP_DROP);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_PRESENCE_GROUP_DROP);
        db.execSQL(TABLE_USERNAME_GROUP_DROP);
        db.execSQL(TABLE_NOTIFICATION_GROUP_DROP);
        onCreate(db);
    }
}

