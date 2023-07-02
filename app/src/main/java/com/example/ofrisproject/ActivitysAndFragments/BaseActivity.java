package com.example.ofrisproject.ActivitysAndFragments;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class BaseActivity extends AppCompatActivity implements SongAdapter.AdapterCallback , UserAdapter.AdapterCallback, RecordingAdapter.AdapterCallback, FBDatabase.OnDocumentsLoadedListener{

public static  User user = null;
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

        if(user!=null)
            setNavigationView();
        else
        {
            FBAuthentication authentication = new FBAuthentication();
            String email =authentication.getUserEmail();
            FBDatabase fbDatabase = new FBDatabase();
            fbDatabase.getDocuments("User","email",email,this,FBDatabase.DEFAULT_ACTION);
        }
    }

    private Fragment currentFragment=null;

    private void setNavigationView() {
        currentFragment=new HomeFragment();
        replaceFragment(currentFragment);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    currentFragment = new HomeFragment();
                    replaceFragment(currentFragment);//new HomeFragment());
                    break;
                case R.id.profile:
                    currentFragment = new ProfileFragment();
                    replaceFragment(currentFragment);//new ProfileFragment());
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
        replaceFragment(new SongsFragment(genre));
    }

    public void Comeback(View view) {

        replaceFragment(new MusicFragment());
    }


    public void searchFriends(View view) {
        replaceFragment(new SearchFriendsFragment());
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
        MoveToEditorPage(s);
    }

    public void userChosen(User u) {
        replaceFragment(new otherUserFragment(u));
    }


    public void RecordingChosen(Recording r, SeekBar sb){
        fbStorage.downloadRecordingFromStorage(r, sb);
    }


    public void AlertDeleteRecording(Recording r) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ombre_background)
                .setTitle("Are you sure you want to delete the recording?")
                .setMessage("There is no way to restore the recording")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteRecording(r);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                }).show();
    }


    private FBStorage fbStorage=new FBStorage();
    private FBDatabase fbDatabase=new FBDatabase();

    public void DeleteRecording(Recording r) {
        fbStorage.deleteRecordingFromStorage(r);
        fbDatabase.getDocuments("recording", "url", r.getUrl(), this,FBDatabase.DELETE_ACTION);
    }

    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents,int action) {

        if(action == FBDatabase.DELETE_ACTION) {
            DocumentReference ref = documents.get(0).getReference();
            ref.delete();
            Toast.makeText(BaseActivity.this, "Recording deleted ", Toast.LENGTH_SHORT).show();
        }
        if(action == FBDatabase.DEFAULT_ACTION)
        {
            user = documents.get(0).toObject(User.class);
            setNavigationView();
        }
        if(action== FBDatabase.UPDATE_ACTION){

            Recording currentRec = documents.get(0).toObject(Recording.class);

            currentRec.isLiked(user.getEmail());
            DocumentReference ref = documents.get(0).getReference();
            ref.update("like",currentRec.getLike()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if(currentFragment instanceof HomeFragment) {
                        currentFragment = new HomeFragment();
                        replaceFragment(currentFragment);
                    }
                }
            });

        }
    }

    @Override
    public void onDocumentsError(Exception e) {
        Toast.makeText(BaseActivity.this, "problem ", Toast.LENGTH_SHORT).show();
    }


    public void LikeRecording(Recording r){
        // choose whether like or unlike?
     //   r.isLiked(user.getEmail());
        fbDatabase.getDocuments("recording", "url", r.getUrl(), this, FBDatabase.UPDATE_ACTION);

    }
}

