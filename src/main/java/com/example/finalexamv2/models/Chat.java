package com.example.finalexamv2.models;

import java.io.Serializable;
import java.util.Date;

public class Chat implements Serializable {

    public String id, chatroom_id, message, sender_id, sender_name;

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", chatroom_id='" + chatroom_id + '\'' +
                ", message='" + message + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sent_at=" + sent_at +
                '}';
    }

    public Date sent_at = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public Date getSent_at() {
        return sent_at;
    }

    public void setSent_at(Date sent_at) {
        this.sent_at = sent_at;
    }
}
