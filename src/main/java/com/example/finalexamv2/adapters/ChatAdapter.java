package com.example.finalexamv2.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexamv2.R;
import com.example.finalexamv2.models.Chat;
import com.example.finalexamv2.models.ChatRoom;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.UViewHolder> {

    ArrayList<Chat> chats = new ArrayList<>();

    IChatItem am;

    FirebaseFirestore db;

    User user;

    public ChatAdapter() {
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        am = (IChatItem) parent.getContext();
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Chat chat = chats.get(position);
        holder.position = position;

        user = am.getUser();

        if(chat.sender_id.equals(user.id)){
            holder.sender.setText("Me");
            holder.del.setVisibility(View.VISIBLE);
        }else{
            holder.del.setVisibility(View.GONE);
            holder.sender.setText(chat.sender_name);
        }

        holder.text.setText(chat.getMessage());

        holder.text_sent_at.setText(Services.getDateString(chat.sent_at));

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                CollectionReference ddb = db.collection(Services.DB_CHATROOMS).document(chat.getChatroom_id()).collection(Services.DB_CHATS);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this chat?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        am.toggleDialog(true);
                        ddb.document(chat.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                am.toggleDialog(false);
                                if (task.isSuccessful()) {
                                    am.alert("Chat deleted!");
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

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void update(ArrayList<Chat> chats){
        this.chats.clear();
        this.chats.addAll(chats);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface IChatItem {

        void toggleDialog(boolean show);

        void alert(String msg);

        User getUser();

        void goBack();

    }

    public static class UViewHolder extends RecyclerView.ViewHolder {

        TextView sender, text, text_sent_at;
        ImageView del;
        View rootView;
        int position;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            sender = itemView.findViewById(R.id.textView4);
            text = itemView.findViewById(R.id.textView5);
            text_sent_at = itemView.findViewById(R.id.textView6);
            del = itemView.findViewById(R.id.imageView2);
        }

    }

}

