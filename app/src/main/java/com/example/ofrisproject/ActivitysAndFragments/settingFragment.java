package com.example.ofrisproject.ActivitysAndFragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.R;
import com.google.firebase.storage.FirebaseStorage;

public class settingFragment extends Fragment {

    private FBAuthentication auth = new FBAuthentication();
    private  ImageView imageView;
    private FBStorage fbStorage = new FBStorage();
    private FBDatabase fbDatabase=new FBDatabase();

    public settingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText newName= getView().findViewById(R.id.editTextTextPersonName);
        String text = newName.getText().toString();

        Button updateB= getView().findViewById(R.id.updateButton);

        boolean changeImg=false;

        imageView= getView().findViewById(R.id.profile);
        String mail=auth.getUserEmail();
        String path = "profiles/" + mail + ".jpg";

        fbStorage.downloadImageFromStorage(imageView,path);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
                //changeImg=true;

            }
        });

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(changeImg)
                   fbStorage.uploadImageToStorage(imageView,picturePath);

            }
        });




    }


    private String picturePath="profiles/" + auth.getUserEmail() + ".jpg";

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

    /* public void SetNickname(String nickname)
    {
        User u=fbDatabase.GetCurrentUser();
        u.setUserName(nickname);
    } */

}