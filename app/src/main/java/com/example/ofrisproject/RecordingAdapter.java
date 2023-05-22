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
private FBStorage fbStorage;

public RecordingAdapter(ArrayList<Recording> list, AdapterCallback activity) {
        recordings=list;
        this.currentActivity = activity;

        fbStorage = new FBStorage();
        }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView UserName;
        public final TextView songName;
        public final TextView singerName;
        public final ImageView recImage;
        public final ImageView profileImage;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this::select);
            UserName = view.findViewById(R.id.UserName);
            songName = view.findViewById(R.id.songName);
            recImage=view.findViewById(R.id.recImage);
            singerName=view.findViewById(R.id.artistName);
            profileImage=view.findViewById(R.id.profileImage);
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
        Recording r = recordings.get(position);
        viewHolder.UserName.setText(r.getUserName());
        viewHolder.songName.setText(r.getSongName());
        viewHolder.singerName.setText(r.getArtistName());
        //viewHolder.recImage.setImageResource(recordings.get(position).getUrl().getBytes());
        //viewHolder.profileImage.setImageResource(recordings.get(position).get);

       // fbStorage.downloadImageFromStorage(viewHolder.profileImage,"profiles/" + r.getEmail() +".jpeg");
        fbStorage.downloadImageFromStorage(viewHolder.recImage,"recordingphoto/" + r.getUrl() +".jpeg");

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


