package com.application.bazouk.whosin.models;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private String idGroup;
    private String username;

    public Notification(String idGroup, String username)
    {
        this.idGroup = idGroup;
        this.username = username;
    }

    // --- GETTERS ---
    public String getUsername() { return username; }
    public String getIdGroup() { return idGroup; }

    // --- SETTERS ---
    public void setUsernames(String username) { this.username = username; }
    public void setIdGroup(String idGroup) { this.idGroup = idGroup; }
}
