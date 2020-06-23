package com.example.whatsappapplication.Models;

import java.io.Serializable;

public class Users implements Serializable {
    private String email;
    private String uid;
    private String username;
    private String photoUrl;
    private long lastSeen;

    public Users(String email, String uid, String username, String photoUrl, long lastSeen) {
        this.email = email;
        this.uid = uid;
        this.username = username;
        this.photoUrl = photoUrl;
        this.lastSeen = lastSeen;
    }


    public Users(String email, String uid, String username, String photoUrl) {
        this.email = email;
        this.uid = uid;
        this.username = username;
        this.photoUrl = photoUrl;
    }

    public Users() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
