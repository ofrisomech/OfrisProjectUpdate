package com.example.ofrisproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ofrisproject.Objects.Comment;
import com.example.ofrisproject.R;

import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {

    private ArrayList<Comment> comments;

    private AdapterCallback currentActivity;
    public CommentAdapter(ArrayList<Comment> list, AdapterCallback activity) {
        comments=list;
        this.currentActivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView UserName;
        public final TextView Content;

        public ViewHolder(View view) {
            super(view);
            UserName = view.findViewById(R.id.userName1);
            Content = view.findViewById(R.id.content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.commentstyle, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position)
    {
        viewHolder.UserName.setText(comments.get(position).getUserName());
        viewHolder.Content.setText(comments.get(position).getContent());
        viewHolder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }



    public interface AdapterCallback
    {
        void CommentChosen(Comment comment);
    }

}


