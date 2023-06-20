package com.example.ofrisproject.ActivitysAndFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.Adapters.SongAdapter;
import com.example.ofrisproject.Adapters.UserAdapter;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.Song;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.example.ofrisproject.databinding.ActivityBaseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AppCompatActivity implements SongAdapter.AdapterCallback , UserAdapter.AdapterCallback, RecordingAdapter.AdapterCallback, FBDatabase.OnDocumentsLoadedListener{


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

    public void MoveToProfilePage(View view) {
        replaceFragment(new ProfileFragment());
    }

    public void MoveToHomePage(View view) {
        replaceFragment(new HomeFragment());
    }

    public void MoveToCommentFragment(String url){
        replaceFragment(new commentsFragment(url));
    }

    public void MoveToEditorPage(Song s) {
        Intent i = new Intent(getApplicationContext(), EditorActivity.class);
        i.putExtra("songName", s.getSongName());
        i.putExtra("artistName", s.getArtistName());
        i.putExtra("songId", s.getSongId());
        startActivity(i);
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



    public void RecordingChosen(Recording r, SeekBar sb){
        fbStorage.downloadRecordingFromStorage(r, sb);

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
    private FBStorage fbStorage=new FBStorage();
    private FBDatabase fbDatabase=new FBDatabase();

    public void DeleteRecording(Recording r) {
        fbStorage.deleteRecordingFromStorage(r);
        fbDatabase.getDocuments("recording", "url", r.getUrl(), this);
    }

    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents) {
        DocumentReference ref = documents.get(0).getReference();
        ref.delete();
        Toast.makeText(BaseActivity.this, "Recording deleted ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDocumentsError(Exception e) {
        Toast.makeText(BaseActivity.this, "problem ", Toast.LENGTH_SHORT).show();
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
                    likeImg.setImageResource(R.drawable.ic_baseline_favorite2_24);
                    likeNum.setText(""+(numlikes+1));


                }
                else{
                    r.addLike(mail);
                    likeImg.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likeNum.setText(""+(numlikes-1));
                }
                ref.update("like",currentRec.getLike());

            }
        });
    }


}

