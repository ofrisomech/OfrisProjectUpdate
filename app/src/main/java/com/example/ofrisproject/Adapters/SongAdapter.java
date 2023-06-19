package com.example.ofrisproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ofrisproject.R;
import com.example.ofrisproject.Objects.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>  {

    private ArrayList<Song> songs;

    private AdapterCallback currentActivity;
    public SongAdapter(ArrayList<Song> list, AdapterCallback activity) {
        songs=list;
        this.currentActivity = activity;
    }
    public void setSongs(ArrayList<Song> brr) {
        this.songs = brr;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView artistName;
        public final TextView songName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            artistName = view.findViewById(R.id.artistName);
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
        viewHolder.artistName.setText(songs.get(position).getArtistName());
        viewHolder.songName.setText(songs.get(position).getSongName());
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

