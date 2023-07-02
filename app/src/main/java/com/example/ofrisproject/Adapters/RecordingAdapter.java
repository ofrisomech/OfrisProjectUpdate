package com.example.ofrisproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ofrisproject.ActivitysAndFragments.BaseActivity;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.R;
import com.example.ofrisproject.Objects.Recording;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder>  {

private ArrayList<Recording> recordings;
private AdapterCallback currentActivity;
private FBStorage fbStorage;
//private ArrayList<String>

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
        public final ImageView playRec;
        public final SeekBar seekBarRec;
        public final ImageView likePost;
        public final ImageView commentPost;
        public final ImageView delRec;
        public final TextView numlike;
        public final TextView numComment;

        public ViewHolder(View view) {
            super(view);
            playRec=view.findViewById(R.id.playRecording);
            playRec.setOnClickListener(this::select);
            UserName = view.findViewById(R.id.UserName);
            songName = view.findViewById(R.id.songName);
            recImage=view.findViewById(R.id.recImage);
            singerName=view.findViewById(R.id.artistName);
            profileImage=view.findViewById(R.id.profileImage);
            seekBarRec=view.findViewById(R.id.seekBarRec);
            likePost=view.findViewById(R.id.likePost);
            likePost.setOnClickListener(this::like);
            delRec=view.findViewById(R.id.deleteRec);
            delRec.setOnClickListener(this::delete);
            commentPost=view.findViewById(R.id.commentPost);
            commentPost.setOnClickListener(this::commentPage);
            numlike=view.findViewById(R.id.numlike);
            numComment=view.findViewById(R.id.numcomment);
        }

        private void delete(View view) {


            currentActivity.AlertDeleteRecording(recordings.get(getAbsoluteAdapterPosition()));

        }

        public void select(View v){
            ImageView iv = (ImageView)v;

            if( iv.getDrawable().getConstantState() == ((Context)currentActivity).getResources().getDrawable( R.drawable.ic_baseline_pause_24).getConstantState())
                playRec.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            else {
                playRec.setImageResource(R.drawable.ic_baseline_pause_24);
                // pause playback

            }
            currentActivity.RecordingChosen(recordings.get(getAdapterPosition()), seekBarRec);
        }

        public void like(View view){
            Recording r = recordings.get(getAbsoluteAdapterPosition());
        /*    if(r.getLike().contains(BaseActivity.user.getEmail()))
            {
                // this means i already liked it
                // change the color to white
                // decrease counter
                // update in firebase

                ViewParent parentView = view.getParent();


            }

         */

            currentActivity.LikeRecording(r);

        }
        public void commentPage(View v){
            currentActivity.MoveToCommentFragment(recordings.get(getAdapterPosition()).getUrl());
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
        viewHolder.songName.setSelected(true);
        viewHolder.singerName.setText(r.getArtistName());
        viewHolder.singerName.setSelected(true);

        // the image - red or white
        if(r.getLike().contains(BaseActivity.user.getEmail()))
        {
            viewHolder.likePost.setImageResource(R.drawable.ic_baseline_favorite2_24);
        }
        else
            viewHolder.likePost.setImageResource(R.drawable.ic_baseline_favorite_24);
        viewHolder.numlike.setText(""+ r.getNumLikes());




        String thisEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(!thisEmail.equals(r.getEmail()))
            viewHolder.delRec.setVisibility(View.INVISIBLE);

        fbStorage.downloadImageFromStorage(viewHolder.profileImage,"profiles/" + r.getEmail() +".jpg");
        fbStorage.downloadImageFromStorage(viewHolder.recImage,"recordingphoto/" + r.getUrl() +".jpeg");
        fbStorage.downloadRecordingFromStorage(r, viewHolder.seekBarRec);
        viewHolder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }


    public interface AdapterCallback
    {
        void RecordingChosen(Recording r, SeekBar s);
        void AlertDeleteRecording(Recording r);
        void LikeRecording(Recording r);
        void MoveToCommentFragment(String url);
    }
}


