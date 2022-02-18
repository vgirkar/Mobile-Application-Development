package com.example.finalexamv2.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexamv2.R;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.UViewHolder> {

    ArrayList<ChatRoom> chatrooms = new ArrayList<>();

    IChatroomItem am;

    FirebaseFirestore db;

    User user;

    public ChatRoomAdapter() {
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_home_item, parent, false);
        am = (IChatroomItem) parent.getContext();
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatRoom chatRoom = chatrooms.get(position);
        holder.position = position;

        user = am.getUser();

        holder.name.setText(chatRoom.getOtherUserName(user.getDisplay_name()));

        holder.last_text.setText(chatRoom.getLastText());

        holder.last_text_at.setText(Services.getDateString(chatRoom.getLast_update()));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.startChatFragment(chatRoom, null);
            }
        });
    }

    public void update(ArrayList<ChatRoom> chatrooms){
        this.chatrooms.clear();
        this.chatrooms.addAll(chatrooms);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.chatrooms.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface IChatroomItem {

        void toggleDialog(boolean show);

        void alert(String msg);

        User getUser();

        void startChatFragment(ChatRoom chatRoom, String msg);

    }

    public static class UViewHolder extends RecyclerView.ViewHolder {

        TextView name, last_text, last_text_at;
        View rootView;
        int position;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            name = itemView.findViewById(R.id.textView);
            last_text = itemView.findViewById(R.id.textView2);
            last_text_at = itemView.findViewById(R.id.textView3);
        }

    }

}

