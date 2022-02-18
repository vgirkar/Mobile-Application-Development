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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalexamv2.R;
import com.example.finalexamv2.activities.AuthActivity;
import com.example.finalexamv2.activities.MainActivity;
import com.example.finalexamv2.adapters.ChatAdapter;
import com.example.finalexamv2.adapters.UserAdapter;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewChatFragment extends Fragment {

    INewChat am;

    FirebaseFirestore db;

    User user = null;

    User selected_user = null;

    TextView selected;

    EditText message;

    UserAdapter userAdapter = null;

    ArrayList<User> users_list = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof INewChat) {
            am = (INewChat) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    public interface INewChat{

        void alert(String msg);

        User getUser();

        void setUser(User user);

        void toggleDialog(boolean show);

        void goBack();

        void startChatFragment(ChatRoom chatRoom, String msg);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chat, container, false);
        getActivity().setTitle("New Chat");
        user = am.getUser();
        db = FirebaseFirestore.getInstance();

        message = view.findViewById(R.id.editTextTextPersonName2);
        selected = view.findViewById(R.id.textView7);

        RecyclerView users = view.findViewById(R.id.userList);
        users.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        users.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(users.getContext(),
                llm.getOrientation());
        users.addItemDecoration(dividerItemDecoration);

        userAdapter = new UserAdapter(new UserAdapter.IUserAdapter() {
            @Override
            public void onClick(User recipient) {
                selected_user = recipient;
                selected.setText(selected_user.getDisplay_name());
            }
        });
        users.setAdapter(userAdapter);

        am.toggleDialog(true);
        db.collection(Services.DB_USERS).document(user.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    user = snapshot.toObject(User.class);
                    user.setId(snapshot.getId());
                    am.setUser(user);
                } else {
                    task.getException().printStackTrace();
                }
            }
        });

        db.collection(Services.DB_USERS).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                users_list.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if(user.getId().equals(doc.getId())) continue;
                    if(user.blocked.contains(doc.getId())) continue;

                    User nuser = doc.toObject(User.class);
                    nuser.setId(doc.getId());
                    users_list.add(nuser);
                }

                userAdapter.update(users_list);
            }
        });

        view.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if(msg.equals("")){
                    am.alert("Please enter chat message!");
                    return;
                }
                if (selected_user == null) {
                    am.alert("Please select one user to chat with!");
                    return;
                }
                ChatRoom chatRoom = new ChatRoom(user.display_name, selected_user.display_name, user.id, selected_user.id, new Date(), new Date(), msg);

                db.collection(Services.DB_CHATROOMS).add(chatRoom).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            chatRoom.setId(task.getResult().getId());

                            user.addChat(chatRoom.getId());
                            db.collection(Services.DB_USERS).document(user.getId()).update("chats", user.chats);
                            db.collection(Services.DB_USERS).document(selected_user.id).update("chats", FieldValue.arrayUnion(chatRoom.getId()));

                            Toast.makeText(getContext(), "Chatroom created", Toast.LENGTH_SHORT).show();
                            am.startChatFragment(chatRoom, msg);
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });

            }
        });

        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.goBack();
            }
        });

        return view;
    }

}