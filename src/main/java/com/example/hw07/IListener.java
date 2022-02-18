package com.example.hw07;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


public interface IListener {
    void login();
    void register();
    void home(String userid);
    void track(String userid);
    void history(String userid);
    void trackActivity(String userId, LatLng start, LatLng end, Timestamp time);
}

