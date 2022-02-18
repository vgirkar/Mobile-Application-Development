package com.example.finalexamv2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.finalexamv2.R;
import com.example.finalexamv2.adapters.ChatAdapter;
import com.example.finalexamv2.adapters.ChatRoomAdapter;
import com.example.finalexamv2.models.Chat;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class ChatFragment extends Fragment {

    private static final String CHATROOM = "chatroom";
    private static final String CHAT = "chat";

    IChat am;

    FirebaseFirestore db;

    ChatRoom chatRoom;

    ChatAdapter chatAdapter;

    EditText message;

    ArrayList<Chat> chats = new ArrayList<>();

    User user = null;

    public static ChatFragment newInstance(ChatRoom chatRoom, String msg) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(CHATROOM, chatRoom);
        args.putSerializable(CHAT, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IChat) {
            am = (IChat) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = am.getUser();
        if (getArguments() != null) {
            db = FirebaseFirestore.getInstance();
            chatRoom = (ChatRoom) getArguments().getSerializable(CHATROOM);
            String first_msg = (String) getArguments().getSerializable(CHAT);
            if(first_msg != null){
                sendMsg(first_msg);
            }
        }
    }

    public interface IChat{

        void alert(String msg);

        User getUser();

        void sendChatsFragment();

        void goBack();

        void toggleDialog(boolean show);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        getActivity().setTitle("Chat - " + chatRoom.getOtherUserName(user.getDisplay_name()));

        message = view.findViewById(R.id.editTextTextPersonName);

        RecyclerView chat = view.findViewById(R.id.chatView);
        chat.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        chat.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(chat.getContext(),
                llm.getOrientation());
        chat.addItemDecoration(dividerItemDecoration);

        chatAdapter = new ChatAdapter();
        chat.setAdapter(chatAdapter);

        am.toggleDialog(true);
        db.collection(Services.DB_CHATROOMS).document(chatRoom.getId()).collection(Services.DB_CHATS).orderBy("sent_at", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                chats.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Chat chat = doc.toObject(Chat.class);
                    chat.setId(doc.getId());
                    chat.setChatroom_id(chatRoom.getId());
                    chats.add(chat);
                }

                chatAdapter.update(chats);
            }
        });

        view.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ddb = db.collection(Services.DB_CHATROOMS).document(chatRoom.getId());

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this chatroom?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        am.toggleDialog(true);
                        ddb.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                am.toggleDialog(false);
                                if (task.isSuccessful()) {
                                    am.alert("Chatroom deleted!");
                                    am.goBack();
                                } else {
                                    task.getException().printStackTrace();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }
        });

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ddb = db.collection(Services.DB_CHATROOMS).document(chatRoom.getId());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Block User")
                        .setMessage("Are you sure you want to block this user?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        am.toggleDialog(true);
                        ddb.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                String other = chatRoom.getOtherUserId(user.id);
                                user.addBlocked(other);
                                db.collection(Services.DB_USERS).document(other).update("blocked", FieldValue.arrayUnion(user.id));
                                db.collection(Services.DB_USERS).document(user.getId()).update("blocked", user.getBlocked());

                                am.toggleDialog(false);
                                if (task.isSuccessful()) {
                                    am.alert("Blocked User!");
                                    am.goBack();
                                } else {
                                    task.getException().printStackTrace();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }
        });

        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if(msg.equals("")){
                    am.alert("Please enter something");
                    return;
                }

                sendMsg(msg);
                updateChatroom(msg);
            }
        });

        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.goBack();
            }
        });

        return view;
    }

    public void updateChatroom(String msg){
        HashMap<String, Object> upd = new HashMap<>();
        upd.put("last_update", new Date());
        upd.put("lastText", msg);

        db.collection(Services.DB_CHATROOMS).document(chatRoom.getId()).update(upd);

    }

    public void sendMsg(String msg){
        HashMap<String, Object> chat = new HashMap<>();
        chat.put("sent_at", new Date());
        chat.put("message", msg);
        chat.put("sender_id", user.id);
        chat.put("sender_name", user.display_name);

        db.collection(Services.DB_CHATROOMS).document(chatRoom.getId()).collection(Services.DB_CHATS).add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                    message.setText("");
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
    }

}