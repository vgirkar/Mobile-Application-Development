package com.example.finalexamv2.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalexamv2.R;
import com.example.finalexamv2.models.User;

public class CommonActivity extends AppCompatActivity {



    public static final String USER_KEY = "user";

    ProgressDialog dialog;

    User user = null;

    public void toggleDialog(boolean show){
        toggleDialog(show, null);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void toggleDialog(boolean show, String msg){
        if(show) {
            dialog = new ProgressDialog(this);
            if(msg == null)
                dialog.setMessage(getString(R.string.loading));
            else
                dialog.setMessage(msg);
            dialog.setCancelable(false);
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }

    public void goBack(){
        getSupportFragmentManager().popBackStack();
    }

    public void alert(String alert){
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle(R.string.info)
                .setMessage(alert)
                .setPositiveButton(R.string.okay, null)
                .show());
    }

}
