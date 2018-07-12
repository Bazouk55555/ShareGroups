package com.application.bazouk.whosin.models.presencegroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adrien on 24/03/2018.
 */

public class PGroup {

    private int id;
    private String nameOfTheGroup;
    private Map<String,Boolean> mapOfUsernames;

    public PGroup(int id, String nameOfTheGroup)
    {
        this.id = id;
        mapOfUsernames = new HashMap<>();
        this.nameOfTheGroup = nameOfTheGroup;
    }

    public void addMember(String username, boolean isPresent)
    {
        mapOfUsernames.put(username,isPresent);
    }

    public int getId()
    {
        return id;
    }

    public String getNameOfTheGroup()
    {
        return nameOfTheGroup;
    }

    public Map<String,Boolean> getMapOfUsernames()
    {
        return mapOfUsernames;
    }
}
