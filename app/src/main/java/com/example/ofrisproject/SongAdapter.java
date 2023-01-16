package com.example.ofrisproject;

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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> songs;
    public SongAdapter(ArrayList<Song> list) {
        songs=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public final TextView artistName;
        public final TextView songName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            view.setOnLongClickListener(this);
            artistName = view.findViewById(R.id.artistName);
            songName = view.findViewById(R.id.songName);
        }

        public void select(View v){
            Toast.makeText(itemView.getContext(), "Click "+ getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View view) {
            songs.remove(getAdapterPosition());
            notifyDataSetChanged();
            return true;
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
        viewHolder.artistName.setText(songs.get(position).GetArtistName());
        viewHolder.songName.setText(""+songs.get(position).GetSongName());
        viewHolder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return songs.size();
    }
}

