package com.example.ofrisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ofrisproject.databinding.ActivityBaseBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AppCompatActivity implements SongAdapter.AdapterCallback , UserAdapter.AdapterCallback, RecordingAdapter.AdapterCallback{


    com.example.ofrisproject.databinding.ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityBaseBinding.inflate(getLayoutInflater());

        //לכל המסכים- העלמת ה
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
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
        } );

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    public void getGenre(View view) {
        ImageButton b = (ImageButton) view;
        String genre = b.getTag().toString();
        replaceFragment(new CreateFragment(genre));
    }

    public void Comeback(View view){

        replaceFragment(new MusicFragment());
    }

    public void oveToSearchPage(View view){

        replaceFragment(new SearchFriendsFragment());}

    public void searchFriends(View view){
        replaceFragment(new SearchFriendsFragment());
    }

    public void moveToSetting(View view){replaceFragment(new settingFragment());}

    public void MoveToEditorPage(Song s){
        //ImageButton b = (ImageButton) view;
        Intent i = new Intent(getApplicationContext(),EditorActivity.class);
        i.putExtra("songName", s.getSongName());
        i.putExtra("artistName", s.getArtistName());
        i.putExtra("songId", s.getSongId());
        startActivity(i);
    }


    public void MoveToProfilePage(View view){
        replaceFragment(new ProfileFragment());
    }
    public void MoveToHomePage(View view){
        replaceFragment(new HomeFragment());
    }

    @Override
    public void songChosen(Song s) {
        // intent -> name,
        Toast.makeText(this,"received " + s.getSongName(),Toast.LENGTH_SHORT).show();
        MoveToEditorPage(s);
    }

    public void userChosen(User u){
        Toast.makeText(this,"received " + u.getUserName(),Toast.LENGTH_SHORT).show();
        replaceFragment(new otherUserFragment(u));
    }

    public void RecordingChosen(Recording r, SeekBar sb) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref = ref.child("recordings/" + r.getUrl() + ".mp3");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {

                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        String path = uri.toString();
                        player.setDataSource(path);
                        player.prepare();
                        player.start();


                        sb.setMax(player.getDuration());

                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            player.seekTo(i);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });




                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            int p = player.getCurrentPosition();
                            sb.setProgress(p);

                            if(player.getDuration() < player.getCurrentPosition()+1000)
                                timer.cancel();
                        }
                    },0,1000);




                    }




                catch (IOException e) {
                    Toast.makeText(BaseActivity.this,"error playing recording " +e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void likePost(View view)
    {

    }

    public void CommentPost(View view)
    {

    }


}


