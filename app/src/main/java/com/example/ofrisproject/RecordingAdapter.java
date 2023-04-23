package com.example.ofrisproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder>  {

private ArrayList<Recording> recordings;
private AdapterCallback currentActivity;

public RecordingAdapter(ArrayList<Recording> list, AdapterCallback activity) {
        recordings=list;
        this.currentActivity = activity;
        }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView UserName;
        public final TextView songName;
        public final ImageView recImage;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            UserName = view.findViewById(R.id.UserName);
            songName = view.findViewById(R.id.songName);
            recImage=view.findViewById(R.id.recImage);
        }

        public void select(View v){
            currentActivity.RecordingChosen(recordings.get(getAdapterPosition()));
        }
    }

    @Override
    public RecordingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.post, viewGroup, false);
        RecordingAdapter.ViewHolder viewHolder = new RecordingAdapter.ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecordingAdapter.ViewHolder viewHolder, int position)
    {
        viewHolder.UserName.setText(recordings.get(position).getArtistUser());
        viewHolder.songName.setText(recordings.get(position).getSongName());
        //viewHolder.recImage.setImageResource(recordings.get(position).getUrl());
        viewHolder.getAdapterPosition();
    }
    @Override
    public int getItemCount() {
        return recordings.size();
    }



    public interface AdapterCallback
    {
        void RecordingChosen(Recording r);
    }
}


