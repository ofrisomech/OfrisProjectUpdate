package com.example.ofrisproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>  {

    private ArrayList<Song> songs;

    private AdapterCallback currentActivity;
    public SongAdapter(ArrayList<Song> list, AdapterCallback activity) {
        songs=list;
        this.currentActivity = activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView UserName;
        public final TextView songName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            UserName = view.findViewById(R.id.artistName);
            songName = view.findViewById(R.id.songName);
        }

        public void select(View v){
            currentActivity.songChosen(songs.get(getAdapterPosition()));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.rc_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position)
    {
        //artistName
        ViewHolder.UserName.setText(songs.get(position).getArtistName());
        viewHolder.songName.setText(""+songs.get(position).getSongName());
        viewHolder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return songs.size();
    }



    public interface AdapterCallback
    {
        void songChosen(Song s);
    }

}

