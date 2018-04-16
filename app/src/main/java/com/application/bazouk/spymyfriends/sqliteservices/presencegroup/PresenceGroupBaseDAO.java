package com.application.bazouk.spymyfriends.sqliteservices.presencegroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PresenceGroupBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabasePresenceGroupHandler databaseHandler = null;

    public PresenceGroupBaseDAO(Context pContext) {
        this.databaseHandler = new DatabasePresenceGroupHandler(pContext);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public void addGroup(String nameOfTheGroup)
    {
        ContentValues value = new ContentValues();
        value.put(DatabasePresenceGroupHandler.NAME_OF_THE_GROUP, nameOfTheGroup);
        mDb.insert(DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP, null, value);
    }

    public int getTotalOfGroups() {
        String query = "select count(*)"+" from " + DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP;
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

    public String getNameOfTheGroupFromId(int id_group)
    {
        String query = "select "+ DatabasePresenceGroupHandler.NAME_OF_THE_GROUP +" from " + DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.ID_GROUP + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{Integer.toString(id_group)});
        c.moveToFirst();
        String nameOfTheGroup  = c.getString(0);
        c.close();
        return nameOfTheGroup;
    }
}
