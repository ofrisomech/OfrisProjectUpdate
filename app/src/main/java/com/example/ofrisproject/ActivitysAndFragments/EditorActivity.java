package com.example.ofrisproject.ActivitysAndFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofrisproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;

public class EditorActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private boolean playerReady = false;
    private boolean isPlaying = false;
    private String songName;
    private String artistName;
    private String videoId;
    private boolean startedRecodingFirstTime = false;
    private ImageView imageView;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        if(!checkPermissions())
        {
            ActivityCompat.requestPermissions(EditorActivity.this, new String[]{
                    Manifest.permission.RECORD_AUDIO},1);
        }
        else
            startTheProcess();
    }


    private boolean checkPermissions(){
        int permission= ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return permission== PackageManager.PERMISSION_GRANTED;
    }

    private void startTheProcess() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        imageView =  findViewById(R.id.startVideo);

        songName= getIntent().getStringExtra("songName");
        artistName=getIntent().getStringExtra("artistName");
        videoId= getIntent().getStringExtra("songId");

        TextView songNameET= findViewById(R.id.songNameEditorPage);
        songNameET.setText(songName);
        TextView singerNameET= findViewById(R.id.singerNameEditorPage);
        singerNameET.setText(artistName);
        youTubePlayer.pause();
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                EditorActivity.this.youTubePlayer=youTubePlayer;
                playerReady = true;
                // get video id from intent
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }


    public void PlayVideo(View view) {
        if (!playerReady)// אם הנגן עוד לא הוריד את הסרטון
            return;
        if (!isPlaying)  //אם הווידאו לא מנגן
        {
            youTubePlayer.play();
            isPlaying = true;
            imageView.setImageResource(R.drawable.stopbutton);
            if (!startedRecodingFirstTime) // רק בפעם הראשונה שמקליטים יוצרים הקלטה
            {
                StartRecording(); //יצירת ההקלטה לראשונה
                startedRecodingFirstTime = true;
            } else// כאשר ההקלטה כבר נוצרה- החל מהפעם השנייה והלאה
                mediaRecorder.resume(); //שחרר את ההקלטה-המשך אותה מהנק האחרונה
            Toast.makeText(EditorActivity.this, "recording started", Toast.LENGTH_SHORT).show();
        } else// אם הוודיאו מנגן
        {
            youTubePlayer.pause();// עצור את הנגן
            mediaRecorder.pause();// עצור את ההקלטה
            isPlaying = false;
            imageView.setImageResource(R.drawable.button2);
            Toast.makeText(EditorActivity.this, "recording paused", Toast.LENGTH_SHORT).show();
        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {// אם יש לחיצה ארוכה על תמונת ההקלט
            @Override
            public boolean onLongClick(View v) {
                StopRecording();// עצור הקלטה לצמיתות ועבור לעמוד הבא
                return true;
            }
        });
    }


    public void StartRecording()// פעולה שמתחילה הקלטה רק בפעם הראשונה
    {
        String AudioSavePath=getApplicationInfo().dataDir +"/" + "recordingAudio.mp3";
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AudioSavePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void StopRecording()// הפסק הקלטה לצמיתות
    {
        youTubePlayer.pause();
        mediaRecorder.stop();
        mediaRecorder.release();
        Toast.makeText(EditorActivity.this, "recording stopped", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(EditorActivity.this, PostActivity.class);
        intent.putExtra("songName", songName);
        intent.putExtra("artistName",artistName);
        startActivity(intent);
        finish();
    }
}