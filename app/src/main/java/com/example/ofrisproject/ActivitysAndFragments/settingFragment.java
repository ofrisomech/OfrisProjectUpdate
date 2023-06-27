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
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class settingFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener{

    private User currentUser= BaseActivity.user;
    private  ImageView imageView;
    private String newName;
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
        EditText nickname= getView().findViewById(R.id.editTextTextPersonName);
        newName = nickname.getText().toString();
        if(newName!=currentUser.getUserName())// אם השם חדש
            SetNickname();
        Button updateB= getView().findViewById(R.id.updateButton);
        imageView= getView().findViewById(R.id.profile);

        boolean changeImg=false;
        String picturePath = "profiles/" + currentUser.getEmail() + ".jpg";

        fbStorage.downloadImageFromStorage(imageView,picturePath);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImg=true;
                mGetContent.launch("image/*");

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


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

    public void SetNickname()
    {
       fbDatabase.getDocuments("User", "email", currentUser.getEmail(), this, FBDatabase.UPDATE_ACTION);
    }


    public void onDocumentsLoaded(List<DocumentSnapshot> documents, int action){

        User current = documents.get(0).toObject(User.class);
        DocumentReference ref = documents.get(0).getReference();
        current.setUserName(newName);
        ref.update("userName",newName);
    }

     public void onDocumentsError(Exception e){

    }







}