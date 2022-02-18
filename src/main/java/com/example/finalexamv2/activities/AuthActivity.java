package com.example.finalexamv2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalexamv2.R;
import com.example.finalexamv2.fragments.LoginFragment;
import com.example.finalexamv2.fragments.RegisterFragment;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends CommonActivity implements LoginFragment.LoginInterface, RegisterFragment.RegInterface {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            sendLoginFragment();
        }else{
            startMainActivity();
        }

    }

    public void sendLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    public void sendRegisterFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }

    public void startMainActivity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection(Services.DB_USERS).document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    User user = snapshot.toObject(User.class);
                    user.setId(snapshot.getId());

                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.putExtra(USER_KEY, user);
                    startActivity(intent);
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
    }

}
