package com.application.bazouk.whosin.models;


public class Notification {

    private String idGroup;
    private String username;
    String nameOfTheGroup;

    public Notification(String idGroup, String username, String nameOfTheGroup)
    {
        this.idGroup = idGroup;
        this.username = username;
        this.nameOfTheGroup = nameOfTheGroup;
    }

    // --- GETTERS ---
    public String getIdGroup() { return idGroup; }
    public String getUsername() { return username; }
    public String getNameOfTheGroup() { return nameOfTheGroup; }

    // --- SETTERS ---
    public void setIdGroup(String idGroup) { this.idGroup = idGroup; }
    public void setUsername(String username) { this.username = username; }
    public void setNameOfTheGroup(String nameOfTheGroup) { this.nameOfTheGroup = nameOfTheGroup; }
}
