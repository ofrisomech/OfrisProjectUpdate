package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.UUID;

public class EditorActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer;
    boolean playerReady = false;
    private boolean isPlaying = false;

    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();



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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            startTheProcess();

        }
    }

    private void startTheProcess() {

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        /*youTubePlayerView.getPlayerUIController().showVideoTitle(false);
        youTubePlayerView.getPlayerUIController().showMenuButton(false);
        youTubePlayerView.getPlayerUIController().showYouTubeButton(false);

        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        */

     //   youTubePlayer.setPlayerStyle(youTubePlayer.PlayerStyle.CHROMELESS);



        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                // save reference to the youtube player
                EditorActivity.this.youTubePlayer=youTubePlayer;
                playerReady = true;
                // get video id from intent
                String videoId = "XwPEtD0_mx4";
                youTubePlayer.loadVideo(videoId, 0);

            }
        });

    }

    public void PlayVideo(View view) {
        if(!playerReady)
            return;
        ImageView b = (ImageView) view;
        if(!isPlaying) {
            youTubePlayer.play();
            isPlaying = true;
            b.setImageResource(R.drawable.stopbutton);
           StartRecording();
        }
        // currently playing - stop, and change background
        else{
            youTubePlayer.pause();
            isPlaying = false;
            b.setImageResource(R.drawable.button2);
            StopRecording();
        }
    }


    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String AudioSavePath=null;

    private boolean checkPermissions(){
        int first= ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        return first== PackageManager.PERMISSION_GRANTED;
    }


    public void StartRecording(){

                    AudioSavePath=getApplicationInfo().dataDir +"/" + "recordingAudio.mp3";

//Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"recordingAudio.mp3";
                    mediaRecorder=new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecorder.setOutputFile(AudioSavePath);

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        Toast.makeText(EditorActivity.this, "recording started", Toast.LENGTH_SHORT).show();

                        Log.d("Media Recorder test", "StartRecording: start" );
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("Media Recorder test", "StartRecording: fail" + e.getMessage() );

                    }
                }




    public void StopRecording(){

                mediaRecorder.stop();

                mediaRecorder.release();
                Toast.makeText(EditorActivity.this, "recording stopped", Toast.LENGTH_SHORT).show();
                finishRecordingAndUploadToFirebase();

            }


    public void finishRecordingAndUploadToFirebase()
    {
        // recording name -> documet reference
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        // create recoding in firestore database
        Recording r = new Recording(mail);
        // create new document and get the reference
        DocumentReference ref = fb.collection("recording").document();

        r.setUrl(ref.toString());

        ref.set(r).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // recording document loaded to firebase
                // upload file to strorage

                // My Projetc reference
                StorageReference sRef = storage.getReference().child(r.getUrl()+".mp3");

                File f = new File(getApplicationInfo().dataDir +"/" + "recordingAudio.mp3");

                InputStream stream = null;
                try {
                    stream = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                sRef.putStream(stream).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                           Log.d("storage upload ", "onComplete: success " );
                        else
                             Log.d("storage upload ", "onComplete: fail "  + task.getException());

                    }
                });




            }
        });




    }


}