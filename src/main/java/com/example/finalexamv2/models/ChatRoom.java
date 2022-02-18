package com.example.finalexamv2.models;

import java.io.Serializable;
import java.util.Date;

public class ChatRoom implements Serializable {

    String id;
    String creator, receipient;
    String creator_id, receipient_id;
    Date created_at;
    Date last_update;
    String lastText;

    public ChatRoom(String creator, String receipient, String creator_id, String receipient_id, Date created_at, Date last_update, String lastText) {
        this.creator = creator;
        this.receipient = receipient;
        this.creator_id = creator_id;
        this.receipient_id = receipient_id;
        this.created_at = created_at;
        this.last_update = last_update;
        this.lastText = lastText;
    }

    public String getOtherUserId(String id){
        return (this.creator_id.equals(id)) ? this.receipient_id : creator_id;
    }

    public String getOtherUserName(String name){
        return (this.creator.equals(name)) ? this.receipient : creator;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "creator='" + creator + '\'' +
                ", receipient='" + receipient + '\'' +
                ", creator_id='" + creator_id + '\'' +
                ", receipient_id='" + receipient_id + '\'' +
                ", created_at=" + created_at +
                '}';
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getReceipient_id() {
        return receipient_id;
    }

    public void setReceipient_id(String receipient_id) {
        this.receipient_id = receipient_id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getReceipient() {
        return receipient;
    }

    public void setReceipient(String receipient) {
        this.receipient = receipient;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public ChatRoom() {
    }


}
