package com.application.bazouk.whosin.models;

import java.util.ArrayList;
import java.util.List;

public class UserGroup {

    private List<String> usernames;
    private List <String> names;
    private List<Boolean> isPresent;
    private String nameOfTheGroup;

    public UserGroup(List<String> usernames, List<String>names, List<Boolean> isPresent, String nameOfTheGroup)
    {
        this.usernames = new ArrayList<>();
        this.names = new ArrayList<>();
        this.isPresent = new ArrayList<>();
        this.usernames.addAll(usernames);
        this.names = names;
        this.isPresent.addAll(isPresent);
        this.nameOfTheGroup = nameOfTheGroup;
    }

    // --- GETTERS ---
    public List<String> getUsernames() { return usernames; }
    public List<String> getNames() { return names; }
    public List<Boolean> getIsPresent() {return isPresent;}
    public String getNameOfTheGroup() { return nameOfTheGroup; }

    // --- SETTERS ---
    public void setUsernames(List<String> usernames) { this.usernames.addAll(usernames); }
    public void setNames(List<String> names) { this.names.addAll(names); }
    public void setIsPresent(List<Boolean> isPresent) {this.isPresent.addAll(isPresent);}
    public void setNameOfTheGroup(String nameOfTheGroup) { this.nameOfTheGroup = nameOfTheGroup; }
}
