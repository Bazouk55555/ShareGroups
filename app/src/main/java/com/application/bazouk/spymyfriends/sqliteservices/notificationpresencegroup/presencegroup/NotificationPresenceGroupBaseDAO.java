package com.application.bazouk.spymyfriends.sqliteservices.notificationpresencegroup.presencegroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenceGroupBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabaseNotificationPresenceGroupHandler databaseHandler = null;

    public NotificationPresenceGroupBaseDAO(Context pContext) {
        this.databaseHandler = new DatabaseNotificationPresenceGroupHandler(pContext);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public void addNotificationGroup(int id, String username)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseNotificationPresenceGroupHandler.ID_GROUP, id);
        value.put(DatabaseNotificationPresenceGroupHandler.USERNAME, username);
        mDb.insert(DatabaseNotificationPresenceGroupHandler.TABLE_NOTIFICATION_GROUP, null, value);
    }

    public int getTotalOfNotificationGroups() {
        String query = "select count(*)"+" from " + DatabaseNotificationPresenceGroupHandler.TABLE_NOTIFICATION_GROUP;
        Cursor c = mDb.rawQuery(query, null);
        c.moveToFirst();
        int total;
        if(c.moveToFirst())
        {
            total = c.getInt(0);;
        }
        else
        {
            total = 0;
        }
        c.close();
        return total;
    }

    public List<Integer> getAllGroupsNameForAUsername(String username) {
        List <Integer> group = new ArrayList<>();
        String query = "select "+ DatabaseNotificationPresenceGroupHandler.ID_GROUP +" from " + DatabaseNotificationPresenceGroupHandler.TABLE_NOTIFICATION_GROUP+ " WHERE "
                + DatabaseNotificationPresenceGroupHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            group.add(c.getInt(0));
        }
        c.close();
        return group;
    }
}
