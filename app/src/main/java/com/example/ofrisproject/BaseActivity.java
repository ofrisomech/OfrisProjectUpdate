package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofrisproject.databinding.ActivityBaseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AppCompatActivity implements SongAdapter.AdapterCallback , UserAdapter.AdapterCallback, RecordingAdapter.AdapterCallback {


    com.example.ofrisproject.databinding.ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());

        //לכל המסכים- העלמת ה
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.music:
                    replaceFragment(new MusicFragment());
                    break;

                case R.id.notifications:
                    replaceFragment(new NotificationsFragment());
                    break;
            }
            return true;
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    public void getGenre(View view) {
        ImageButton b = (ImageButton) view;
        String genre = b.getTag().toString();
        replaceFragment(new CreateFragment(genre));
    }

    public void Comeback(View view) {

        replaceFragment(new MusicFragment());
    }

    public void oveToSearchPage(View view) {

        replaceFragment(new SearchFriendsFragment());
    }

    public void searchFriends(View view) {
        replaceFragment(new SearchFriendsFragment());
    }

    public void moveToSetting(View view) {
        replaceFragment(new settingFragment());
    }

    public void MoveToEditorPage(Song s) {
        //ImageButton b = (ImageButton) view;
        Intent i = new Intent(getApplicationContext(), EditorActivity.class);
        i.putExtra("songName", s.getSongName());
        i.putExtra("artistName", s.getArtistName());
        i.putExtra("songId", s.getSongId());
        startActivity(i);
    }


    public void MoveToProfilePage(View view) {
        replaceFragment(new ProfileFragment());
    }

    public void MoveToHomePage(View view) {
        replaceFragment(new HomeFragment());
    }

    @Override
    public void songChosen(Song s) {
        // intent -> name,
        Toast.makeText(this, "received " + s.getSongName(), Toast.LENGTH_SHORT).show();
        MoveToEditorPage(s);
    }

    public void userChosen(User u) {
        Toast.makeText(this, "received " + u.getUserName(), Toast.LENGTH_SHORT).show();
        replaceFragment(new otherUserFragment(u));
    }

    public void RecordingChosen(Recording r, SeekBar sb) {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("recordings/" + r.getUrl() + ".mp3");

        try {
            File localFile = File.createTempFile("audio", "mp3");

            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        FileInputStream fis = new FileInputStream(localFile);
                        mediaPlayer.setDataSource(fis.getFD());

                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        sb.setMax(mediaPlayer.getDuration());

                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mediaPlayer.seekTo(progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                // No implementation needed
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                // No implementation needed
                            }
                        });

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                int currentPosition = mediaPlayer.getCurrentPosition();
                                sb.setProgress(currentPosition);

                                if (mediaPlayer.getDuration() < currentPosition + 1000) {
                                    timer.cancel();
                                }
                            }
                        }, 0, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BaseActivity.this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BaseActivity.this, "Error downloading audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(BaseActivity.this, "Error creating temporary file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void likePost(View view) {

    }

    public void CommentPost(View view) {

    }

    public void AlertDeleteRecording(Recording r) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ombre_background)
                .setTitle("Are you sure you want to delete the post")
                .setMessage("No way to ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteRecording(r);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(BaseActivity.this, "Nothing Happened", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void DeleteRecording(Recording r) {
        // FB and Storage
        // delete from storage



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("recording/" + r.getUrl() + ".mp3");
        ref.delete();


        // deletre from database
        db.collection("recording").whereEqualTo("url", r.getUrl()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Toast.makeText(BaseActivity.this, "Recording deleted ", Toast.LENGTH_SHORT).show();
                        DocumentReference ref = queryDocumentSnapshots.getDocuments().get(0).getReference();

                        ref.delete();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BaseActivity.this, "problem ", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void LikeRecording(Recording r, ImageView likeImg, TextView likeNum){
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        FirebaseFirestore fb= FirebaseFirestore.getInstance();
        String likes=r.getLike();
        String[] userLike = likes.split(",");
        int numlikes=Integer.valueOf(likeNum.getText().toString());
        fb.collection("recording").whereEqualTo("url",r.getUrl()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Recording currentRec = queryDocumentSnapshots.getDocuments().get(0).toObject(Recording.class);
                DocumentReference ref = queryDocumentSnapshots.getDocuments().get(0).getReference();
                if(r.addLike(mail))// עדיין לא עשה לייק
                {
                    //likeImg.setImageResou;
                    likeNum.setText(""+(numlikes+1));


                }
                else{
                    r.addLike(mail);
                    //likeImg.setImageResou;
                    likeNum.setText(""+(numlikes-1));
                }
                ref.update("like",currentRec.getLike());

            }
        });
    }

    public void MoveToCommentFragment(String url){
        replaceFragment(new commentsFragment(url));
    }


}


