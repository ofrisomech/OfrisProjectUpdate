package com.example.ofrisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ofrisproject.databinding.ActivityBaseBinding;
import com.example.ofrisproject.databinding.ActivityMainBinding;

public class BaseActivity extends AppCompatActivity {

    com.example.ofrisproject.databinding.ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityBaseBinding.inflate(getLayoutInflater());
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
}

