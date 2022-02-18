package com.example.finalexamv2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalexamv2.R;
import com.example.finalexamv2.activities.AuthActivity;
import com.example.finalexamv2.activities.MainActivity;
import com.example.finalexamv2.adapters.ChatRoomAdapter;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    IChats am;

    FirebaseFirestore db;

    User user = null;

    ChatRoomAdapter chatRoomAdapter;

    ArrayList<ChatRoom> chatrooms = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IChats) {
            am = (IChats) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface IChats{

        void alert(String msg);

        User getUser();

        void sendNewChatFragment();

        void setUser(User user);

        void toggleDialog(boolean show);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        getActivity().setTitle("My Chats");
        user = am.getUser();
        db = FirebaseFirestore.getInstance();

        RecyclerView chats = view.findViewById(R.id.chatsView);
        chats.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        chats.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(chats.getContext(),
                llm.getOrientation());
        chats.addItemDecoration(dividerItemDecoration);

        chatRoomAdapter = new ChatRoomAdapter();
        chats.setAdapter(chatRoomAdapter);

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.sendNewChatFragment();
            }
        });

        am.toggleDialog(true);
        db.collection(Services.DB_CHATROOMS).orderBy("last_update", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value == null) {
                    return;
                }
                am.toggleDialog(false);

                chatrooms.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if(user.chats.contains(doc.getId())){
                        ChatRoom chatroom = doc.toObject(ChatRoom.class);
                        chatroom.setId(doc.getId());
                        chatrooms.add(chatroom);
                    }
                }

                chatRoomAdapter.update(chatrooms);
            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                am.setUser(null);
                startActivity(new Intent(getActivity(), AuthActivity.class));
            }
        });

        return view;
    }
}