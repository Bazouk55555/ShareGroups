package com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupsOfUsernamesBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabaseGroupsOfUsernamesHandler databaseHandler = null;

    public GroupsOfUsernamesBaseDAO(Context pContext) {
        this.databaseHandler = new DatabaseGroupsOfUsernamesHandler(pContext);
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
            value.put(DatabaseGroupsOfUsernamesHandler.ID_GROUP, id);
            value.put(DatabaseGroupsOfUsernamesHandler.NAME_OF_THE_GROUP, nameOfTheGroup);
            value.put(DatabaseGroupsOfUsernamesHandler.USERNAME, username);
            value.put(DatabaseGroupsOfUsernamesHandler.IS_PRESENT, presenceGroup.getMapOfUsernames().get(username)?1:0);
            mDb.insert(DatabaseGroupsOfUsernamesHandler.TABLE_GROUP, null, value);
        }
    }

    public void addMember(int id, String nameOfTheGroup, String username, boolean isPresent)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseGroupsOfUsernamesHandler.ID_GROUP, id);
        value.put(DatabaseGroupsOfUsernamesHandler.NAME_OF_THE_GROUP, nameOfTheGroup);
        value.put(DatabaseGroupsOfUsernamesHandler.USERNAME, username);
        value.put(DatabaseGroupsOfUsernamesHandler.IS_PRESENT, 0);
        mDb.insert(DatabaseGroupsOfUsernamesHandler.TABLE_GROUP, null, value);
    }

    public void changePresence(int id,String username, boolean isPresent)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseGroupsOfUsernamesHandler.IS_PRESENT, isPresent?1:0);
        mDb.update(DatabaseGroupsOfUsernamesHandler.TABLE_GROUP, value, DatabaseGroupsOfUsernamesHandler.ID_GROUP  + " = ? and "+DatabaseGroupsOfUsernamesHandler.USERNAME  + " = ?", new String[] {Integer.toString(id),username});
    }

    public List<String> getUsernames(int id)
    {
        String query = "select "+ DatabaseGroupsOfUsernamesHandler.USERNAME +" from " + DatabaseGroupsOfUsernamesHandler.TABLE_GROUP+ " WHERE "
                + DatabaseGroupsOfUsernamesHandler.ID_GROUP + "=?";
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
        String query = "select "+ DatabaseGroupsOfUsernamesHandler.NAME_OF_THE_GROUP+","+ DatabaseGroupsOfUsernamesHandler.ID_GROUP +" from " + DatabaseGroupsOfUsernamesHandler.TABLE_GROUP+ " WHERE "
                + DatabaseGroupsOfUsernamesHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            group.add(new String[]{c.getString(0), Integer.toString(c.getInt(1))});
        }
        c.close();
        return group;
    }

    public boolean getPresence(int id_group, String username) {
        boolean isPresent;
        String query = "select "+ DatabaseGroupsOfUsernamesHandler.IS_PRESENT +" from " + DatabaseGroupsOfUsernamesHandler.TABLE_GROUP+ " WHERE "
                + DatabaseGroupsOfUsernamesHandler.ID_GROUP + "=? and "+ DatabaseGroupsOfUsernamesHandler.USERNAME +"=?";
        Cursor c = mDb.rawQuery(query, new String[]{Integer.toString(id_group),username});
        c.moveToFirst();
        isPresent = c.getInt(0)==1?true:false;
        c.close();
        return isPresent;
    }
}
