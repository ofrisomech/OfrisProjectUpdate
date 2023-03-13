package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class EditorActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer;
    boolean playerReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                // save reference to the youtube player
                EditorActivity.this.youTubePlayer=youTubePlayer;
                playerReady = true;
            }

        });
    }

    public void PlayVideo(View view){
        if(!playerReady)
            return;
        ImageView imageView=(ImageView)view;
        if(view.getId()==R.id.startVideo) {
            String videoId = "XwPEtD0_mx4";
            youTubePlayer.loadVideo(videoId, 0);
            view.setVisibility(view.GONE);
        }
        else if(view.getId()==R.id.stopVideo){
            youTubePlayer.pause();
            view.setVisibility(view.GONE);
        }
    }

}