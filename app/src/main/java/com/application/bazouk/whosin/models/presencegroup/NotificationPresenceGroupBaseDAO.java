package com.application.bazouk.whosin.models.presencegroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenceGroupBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabasePresenceGroupHandler databaseHandler = null;

    public NotificationPresenceGroupBaseDAO(Context pContext) {
        this.databaseHandler = new DatabasePresenceGroupHandler(pContext);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public void addNotificationGroup(String id, String username)
    {
        ContentValues value = new ContentValues();
        value.put(DatabasePresenceGroupHandler.ID_GROUP, id);
        value.put(DatabasePresenceGroupHandler.USERNAME, username);
        mDb.insert(DatabasePresenceGroupHandler.TABLE_NOTIFICATION_GROUP, null, value);
    }

    public int getTotalOfNotificationGroups() {
        String query = "select count(*)"+" from " + DatabasePresenceGroupHandler.TABLE_NOTIFICATION_GROUP;
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
        String query = "select "+ DatabasePresenceGroupHandler.ID_GROUP +" from " + DatabasePresenceGroupHandler.TABLE_NOTIFICATION_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            group.add(c.getInt(0));
        }
        c.close();
        return group;
    }

    public void removeMember(int id, String username) {
        mDb.delete(DatabasePresenceGroupHandler.TABLE_NOTIFICATION_GROUP,  DatabasePresenceGroupHandler.ID_GROUP+ " = ?" +" and "+DatabasePresenceGroupHandler.USERNAME+ " = ?", new String[] {String.valueOf(id),username});
    }
}
