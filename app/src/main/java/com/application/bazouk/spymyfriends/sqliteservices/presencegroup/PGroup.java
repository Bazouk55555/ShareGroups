package com.application.bazouk.spymyfriends.sqliteservices.presencegroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrien on 24/03/2018.
 */

public class PGroup {

    private int id;
    String name;
    Map<String,Boolean> mapOfUsernames;

    PGroup(int id, String name)
    {
        mapOfUsernames = new HashMap<>();
        this.id = id;
        this.name = name;
    }

    public void addMember(String username)
    {
        //presenceGroupList.add(username);
    }

    public int getId()
    {
        return id;
    }

    /*public List<String> getPresenceGroupList()
    {
        return presenceGroupList;
    }*/
}
