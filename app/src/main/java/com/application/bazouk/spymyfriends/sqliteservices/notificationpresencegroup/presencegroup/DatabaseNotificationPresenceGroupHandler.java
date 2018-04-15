package com.application.bazouk.spymyfriends.sqliteservices.notificationpresencegroup.presencegroup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseNotificationPresenceGroupHandler extends SQLiteOpenHelper {
    public static final String ID_GROUP = "id_group";
    public static final String USERNAME = "username";

    public static final String TABLE_NOTIFICATION_GROUP = "NOTIFICATION_PRESENCE_GROUP";
    public static final String TABLE_GROUP_CREATE = "CREATE TABLE " + TABLE_NOTIFICATION_GROUP + " (" + ID_GROUP
            + " INTEGER PRIMARY KEY, " + USERNAME+ " TEXT);";
    public static final String TABLE_GROUP_DROP = "DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_GROUP + ";";

    private final static String DATABASE = "database_notification_group";
    private final static int VERSION = 3;

    public DatabaseNotificationPresenceGroupHandler(Context context) {
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

