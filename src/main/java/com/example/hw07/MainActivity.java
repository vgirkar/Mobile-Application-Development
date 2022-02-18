package com.example.hw07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements IListener{
    /*
    Vinit Girkar & Ilan Aktanova Group1-1
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.Login);
        FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFragment, new LoginFragment(), getString(R.string.Login))
                .commit();

    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, new LoginFragment(),  getString(R.string.Login))
                .commit();
    }

    @Override
    public void register() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, new RegisterFragment(), getString(R.string.Register))
                .commit();
    }

    @Override
    public void home(String userid) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment,  HomeFragment.newInstance(userid), getString(R.string.Home))
                .commit();
    }

    @Override
    public void track(String userid) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment,   MapsFragment.newInstance(userid), getString(R.string.TrackLocation))
                .commit();
    }

    @Override
    public void history(String userid) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment,   HistoryFragment.newInstance(userid), getString(R.string.history))
                .commit();
    }

    @Override
    public void trackActivity(String userId, LatLng start, LatLng end, Timestamp time) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment,   TrackFragment.newInstance(userId,start,end,time), getString(R.string.track))
                .commit();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}