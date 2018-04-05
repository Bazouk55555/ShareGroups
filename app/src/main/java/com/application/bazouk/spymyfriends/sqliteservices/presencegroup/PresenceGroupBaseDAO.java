package com.application.bazouk.spymyfriends.sqliteservices.presencegroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    public void addGroup(PGroup presenceGroup)
    {
        int id = presenceGroup.getId();
        String nameOfTheGroup = presenceGroup.getNameOfTheGroup();
        for(String username: presenceGroup.getMapOfUsernames().keySet()) {
            ContentValues value = new ContentValues();
            value.put(DatabasePresenceGroupHandler.ID_GROUP, id);
            value.put(DatabasePresenceGroupHandler.NAME_OF_THE_GROUP, nameOfTheGroup);
            value.put(DatabasePresenceGroupHandler.USERNAME, username);
            value.put(DatabasePresenceGroupHandler.IS_PRESENT, presenceGroup.getMapOfUsernames().get(username)?1:0);
            mDb.insert(DatabasePresenceGroupHandler.TABLE_GROUP, null, value);
        }
    }

    public void addMember(int id, String nameOfTheGroup, String username, boolean isPresent)
    {
        ContentValues value = new ContentValues();
        value.put(DatabasePresenceGroupHandler.ID_GROUP, id);
        value.put(DatabasePresenceGroupHandler.NAME_OF_THE_GROUP, nameOfTheGroup);
        value.put(DatabasePresenceGroupHandler.USERNAME, username);
        value.put(DatabasePresenceGroupHandler.IS_PRESENT, 0);
        mDb.insert(DatabasePresenceGroupHandler.TABLE_GROUP, null, value);
    }

    public void changePresence(int id,String username, boolean isPresent)
    {

    }

    public List<String> getUsernames(int id)
    {
        String query = "select "+ DatabasePresenceGroupHandler.USERNAME +" from " + DatabasePresenceGroupHandler.TABLE_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.ID_GROUP + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{Integer.toString(id)});
        List<String> usernames = new ArrayList<>();
        while(c.moveToNext()) {
            usernames.add(c.getString(0));
        }
        c.close();
        return usernames;
    }

    public List<String> getAllGroupsName(String username) {
        List <String> group = new ArrayList<>();
        String query = "select "+ DatabasePresenceGroupHandler.NAME_OF_THE_GROUP +" from " + DatabasePresenceGroupHandler.TABLE_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            group.add(c.getString(0));
        }
        c.close();
        return group;
    }
}
