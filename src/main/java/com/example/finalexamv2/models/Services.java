package com.example.finalexamv2.models;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Services {

    public static final String DB_USERS = "users";
    public static final String DB_CHATROOMS = "chatrooms";
    public static final String DB_CHATS = "chats";

    public static String getDateString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String getPrettyTime(Date date) {
        PrettyTime time = new PrettyTime();
        return time.format(date);
    }

}
