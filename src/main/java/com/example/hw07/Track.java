package com.example.hw07;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Track implements Serializable {
    String userid;
    int id;
    LatLng geopoint;
    LatLng end;
    Timestamp time;

    public Track(String userid, int id, LatLng geopoint, LatLng end, Timestamp time) {
        this.userid = userid;
        this.id = id;
        this.geopoint = geopoint;
        this.end = end;
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(LatLng geopoint) {
        this.geopoint = geopoint;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
