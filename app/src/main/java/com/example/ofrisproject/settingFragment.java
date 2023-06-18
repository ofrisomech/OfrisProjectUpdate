package com.example.ofrisproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class settingFragment extends Fragment {

    private FBAuthentication auth = new FBAuthentication();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
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


        imageView= getView().findViewById(R.id.profile);
        String mail=auth.getUserEmail();
        String path = "profiles/" + mail + ".jpg";

        fbStorage.downloadImageFromStorage(imageView,path);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");

            }
        });



    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                    String mail=auth.getUserEmail();
                    String picturePath="profiles/" + mail + ".jpg";

                    // upload new profile image to storage
                    fbStorage.uploadImageToStorage(imageView,picturePath);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

    public void SetNickname(String nickname)
    {
        User u=fbDatabase.GetCurrentUser();
        u.setUserName(nickname);
    }

}