package com.example.ofrisproject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
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

    /*private void IsLikes(String RecId, ImageView imageView){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getRefernce().child("likes").child(RecId);
        reference.addValueEventListener(new ValueEventListener()){
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.child(firebaseUser.getUid()).exists()) {
                imageView.setColorFilter(0xffff0000);
                imageView.setTag("liked");

                }
                else {
                    imageView.setColorFilter(0xffff0000);
                    imageView.setTag("like");
                }
            }
            }
        });
    }*/


    /* public void displayNumberOfLikes(String postId, String currentUserId){
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child('recording').child(postId);
        likesRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long numOfLikes = 0;
                    if(dataSnapshot.hasChild("likes")){
                        numOfLikes = dataSnapshot.child("likes").getValue(Long.class);
                    }

                    //Populate numOfLikes on post i.e. textView.setText(""+numOfLikes)
                    //This is to check if the user has liked the post or not
                    btnLike.setSelected(dataSnapshot.hasChild(userId));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onLikeClicked(View v, String postId, String userId){
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child('Post').child(postId).child("likes");
        likesRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numLikes = 0;
                if(dataSnapshot.exists()){
                    numLikes = dataSnapshot.getValue(Long.class);
                }
                boolean isLiked = btnLike.isSelected();
                if(isLiked){
                    //If already liked then user wants to unlike the post
                    likesRef.set(numLikes-1);
                }else {
                    //If not liked already then user wants to like the post
                    likesRef.set(numLikes+1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public interface AdapterCallback
    {
        void RecordingChosen(Recording r);
    }
}


