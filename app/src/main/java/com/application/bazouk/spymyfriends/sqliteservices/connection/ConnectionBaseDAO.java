package com.application.bazouk.spymyfriends.sqliteservices.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ConnectionBaseDAO {

    private SQLiteDatabase mDb = null;
    private DatabaseConnectionHandler databaseHandler = null;

    public ConnectionBaseDAO(Context pContext) {
        this.databaseHandler = new DatabaseConnectionHandler(pContext);
    }

    public SQLiteDatabase open() {
        mDb = databaseHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public boolean canConnect(String username, String password)
    {
        return getPasswordFromUsername(username).equals(password);
    }

    private String getPasswordFromUsername(String username)
    {
        String password = "";
        String query = "select "+ DatabaseConnectionHandler.PASSWORD +" from " + DatabaseConnectionHandler.TABLE_CONNECTION+ " WHERE "
                + DatabaseConnectionHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        if(c.getCount()==0)
        {
            c.close();
            return password;
        }
        while(c.moveToNext()) {
            password = c.getString(0);
        }
        c.close();
        return password;
    }

    public void add(String username, String password, String first_name, String last_name)
    {
        ContentValues value = new ContentValues();
        value.put(DatabaseConnectionHandler.USERNAME,username);
        value.put(DatabaseConnectionHandler.PASSWORD,password);
        value.put(DatabaseConnectionHandler.FIRST_NAME,first_name);
        value.put(DatabaseConnectionHandler.LAST_NAME,last_name);
        mDb.insert(DatabaseConnectionHandler.TABLE_CONNECTION, null, value);
    }

    public String [] getFirstAndLastName(String username)
    {
        String [] firstAndLastName = new String [2];
        String query = "select "+ DatabaseConnectionHandler.FIRST_NAME+","+DatabaseConnectionHandler.LAST_NAME +" from " + DatabaseConnectionHandler.TABLE_CONNECTION+ " WHERE "
                + DatabaseConnectionHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        while(c.moveToNext()) {
            firstAndLastName[0] = c.getString(0);
            firstAndLastName[1] = c.getString(1);
        }
        c.close();
        return firstAndLastName;
    }

    public boolean isUsernameReal(String username)
    {
        String query = "select * from " + DatabaseConnectionHandler.TABLE_CONNECTION+ " WHERE "
                + DatabaseConnectionHandler.USERNAME + "=?";
        Cursor c = mDb.rawQuery(query, new String[]{username});
        if(c.getCount()==0)
        {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

}
