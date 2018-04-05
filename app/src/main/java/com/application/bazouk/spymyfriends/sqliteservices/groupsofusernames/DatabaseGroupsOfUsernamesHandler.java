package com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseGroupsOfUsernamesHandler extends SQLiteOpenHelper {
    public static final String ID_KEY = "id_key";
    public static final String ID_GROUP = "id_group";
    public static final String NAME_OF_THE_GROUP = "name_of_the_group";
    public static final String USERNAME = "username";
    public static final String IS_PRESENT = "is_present";

    public static final String TABLE_GROUP = "GROUPS_OF_USERNAMES";
    public static final String TABLE_GROUP_CREATE = "CREATE TABLE " + TABLE_GROUP + " (" + ID_KEY
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_GROUP + " INTEGER, " + NAME_OF_THE_GROUP+ " TEXT, " + USERNAME + " TEXT, "+ IS_PRESENT + " INTEGER);";
    public static final String TABLE_GROUP_DROP = "DROP TABLE IF EXISTS " + TABLE_GROUP + ";";

    private final static String DATABASE = "database_groups_by_usernames";
    private final static int VERSION = 2;

    public DatabaseGroupsOfUsernamesHandler(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_GROUP_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_GROUP_DROP);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_GROUP_DROP);
        onCreate(db);
    }
}

