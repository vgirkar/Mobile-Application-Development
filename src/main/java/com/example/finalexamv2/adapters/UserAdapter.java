package com.example.finalexamv2.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexamv2.R;
import com.example.finalexamv2.models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UViewHolder> {

    ArrayList<User> users = new ArrayList<>();

    IUserAdapter am;

    public UserAdapter(IUserAdapter am) {
        this.am = am;
    }

    public interface IUserAdapter{
        void onClick(User recipient);
    }

    @NonNull
    @Override
    public UViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = users.get(position);
        holder.position = position;


        holder.user_name.setText(user.getDisplay_name());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.onClick(user);
            }
        });
    }

    public void update(ArrayList<User> users){
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class UViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        View rootView;
        int position;

        public UViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            user_name = itemView.findViewById(R.id.textView9);
        }

    }

}

