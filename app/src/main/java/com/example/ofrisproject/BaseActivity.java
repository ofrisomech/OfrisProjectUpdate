package com.example.ofrisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ofrisproject.databinding.ActivityBaseBinding;
import com.example.ofrisproject.databinding.ActivityMainBinding;

public class BaseActivity extends AppCompatActivity implements SongAdapter.AdapterCallback {




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
        //ImageButton b = (ImageButton) view;
        replaceFragment(new MusicFragment());
    }

    public void MoveToEditorPage(Song s){
        //ImageButton b = (ImageButton) view;
        Intent i = new Intent(getApplicationContext(),EditorActivity.class);
        i.putExtra("songName", s.getSongName());
        i.putExtra("songId", s.getSongId());
        startActivity(i);
    }

    @Override
    public void songChosen(Song s) {
        // intent -> name,
        Toast.makeText(this,"received " + s.getSongName(),Toast.LENGTH_SHORT).show();
        MoveToEditorPage(s);
    }
}

