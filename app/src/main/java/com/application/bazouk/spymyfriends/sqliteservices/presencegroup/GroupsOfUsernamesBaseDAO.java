package com.application.bazouk.spymyfriends.sqliteservices.presencegroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupsOfUsernamesBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabasePresenceGroupHandler databaseHandler = null;

    public GroupsOfUsernamesBaseDAO(Context pContext) {
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
        for(String username: presenceGroup.getMapOfUsernames().keySet()) {
            ContentValues value = new ContentValues();
            value.put(DatabasePresenceGroupHandler.ID_GROUP, id);
            value.put(DatabasePresenceGroupHandler.USERNAME, username);
            value.put(DatabasePresenceGroupHandler.IS_PRESENT, presenceGroup.getMapOfUsernames().get(username)?1:0);
            mDb.insert(DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP, null, value);
        }
    }

    public void addMember(int id,String username)
    {
        ContentValues value = new ContentValues();
        value.put(DatabasePresenceGroupHandler.ID_GROUP, id);
        value.put(DatabasePresenceGroupHandler.USERNAME, username);
        value.put(DatabasePresenceGroupHandler.IS_PRESENT, 0);
        mDb.insert(DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP, null, value);
    }

    public void changePresence(int id,String username, boolean isPresent)
    {
        ContentValues value = new ContentValues();
        value.put(DatabasePresenceGroupHandler.IS_PRESENT, isPresent?1:0);
        mDb.update(DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP, value, DatabasePresenceGroupHandler.ID_GROUP  + " = ? and "+ DatabasePresenceGroupHandler.USERNAME  + " = ?", new String[] {Integer.toString(id),username});
    }

    public List<String> getUsernames(int id)
    {
        String query = "select "+ DatabasePresenceGroupHandler.USERNAME +" from " + DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.ID_GROUP + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{Integer.toString(id)});
        List<String> usernames = new ArrayList<>();
        while(c.moveToNext()) {
            usernames.add(c.getString(0));
        }
        c.close();
        return usernames;
    }

    public List<String[]> getAllGroupsName(String username) {
        List <String[]> group = new ArrayList<>();
        String query = "select "+ DatabasePresenceGroupHandler.NAME_OF_THE_GROUP+","+ DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP+"."+DatabasePresenceGroupHandler.ID_GROUP +" from " + DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP+" JOIN "+DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP+ " ON "+ DatabasePresenceGroupHandler.TABLE_PRESENCE_GROUP+"."+DatabasePresenceGroupHandler.ID_GROUP +"="+ DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP+"."+DatabasePresenceGroupHandler.ID_GROUP+" WHERE "
                + DatabasePresenceGroupHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            group.add(new String[]{c.getString(0), Integer.toString(c.getInt(1))});
        }
        c.close();
        return group;
    }

    public boolean getPresence(int id_group, String username) {
        boolean isPresent;
        String query = "select "+ DatabasePresenceGroupHandler.IS_PRESENT +" from " + DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP+ " WHERE "
                + DatabasePresenceGroupHandler.ID_GROUP + "=? and "+ DatabasePresenceGroupHandler.USERNAME +"=?";
        Cursor c = mDb.rawQuery(query, new String[]{Integer.toString(id_group),username});
        c.moveToFirst();
        isPresent = c.getInt(0)==1?true:false;
        c.close();
        return isPresent;
    }

    public int getNumberOfGroupsForUsername(String username) {
        String query = "select count(*)"+" from " + DatabasePresenceGroupHandler.TABLE_USERNAME_GROUP +" WHERE "+DatabasePresenceGroupHandler.USERNAME+"= ?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
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
}
