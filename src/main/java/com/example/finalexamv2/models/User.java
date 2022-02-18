package com.example.finalexamv2.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    public String id, display_name, email;

    public ArrayList<String> chats = new ArrayList<>();

    public ArrayList<String> blocked = new ArrayList<>();

    public ArrayList<String> getBlocked() {
        return blocked;
    }

    public void addBlocked(String id){
        this.blocked.add(id);
    }

    public void addChat(String id){
        this.chats.add(id);
    }

    public User(){}

    public User(String id, String display_name, String email) {
        this.id = id;
        this.display_name = display_name;
        this.email = email;
    }

    public ArrayList<String> getChats() {
        return chats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.display_name;
    }
}
