package com.example.ofrisproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<User> users;

    private AdapterCallback currentActivity;

    public UserAdapter(ArrayList<User> list, AdapterCallback activity) {
        users = list;
        this.currentActivity = activity;
    }

    public void setUsers(ArrayList<User> brr) {
        this.users = brr;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView UserName;
        public final ImageView UserImage;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            UserName = view.findViewById(R.id.userName);
            UserImage = view.findViewById(R.id.imageUser);
        }

        public void select(View v) {
            currentActivity.userChosen(users.get(getAdapterPosition()));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.userlayout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.UserName.setText(users.get(position).getUserName());
        //viewHolder.UserImage.setI(users.get(position).getProfileImage());
        viewHolder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public interface AdapterCallback {
        void userChosen(User user);
    }

}


