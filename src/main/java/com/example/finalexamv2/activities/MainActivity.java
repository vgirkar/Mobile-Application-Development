package com.example.finalexamv2.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalexamv2.R;
import com.example.finalexamv2.adapters.ChatAdapter;
import com.example.finalexamv2.adapters.ChatRoomAdapter;
import com.example.finalexamv2.fragments.ChatFragment;
import com.example.finalexamv2.fragments.ChatsFragment;
import com.example.finalexamv2.fragments.LoginFragment;
import com.example.finalexamv2.fragments.NewChatFragment;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends CommonActivity implements NewChatFragment.INewChat, ChatsFragment.IChats, ChatFragment.IChat, ChatRoomAdapter.IChatroomItem, ChatAdapter.IChatItem {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = null;
        if(getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra(USER_KEY)){
            user = (User) getIntent().getSerializableExtra(USER_KEY);
        }

        if (user != null) {
            setUser(user);
            sendChatsFragment();
        }
    }

    public void sendNewChatFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainView, new NewChatFragment())
                .addToBackStack(null)
                .commit();
    }

    public void sendChatsFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainView, new ChatsFragment())
                .commit();
    }

    public void startChatFragment(ChatRoom chatRoom, String msg){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainView, ChatFragment.newInstance(chatRoom, msg))
                .addToBackStack(null)
                .commit();
    }

}