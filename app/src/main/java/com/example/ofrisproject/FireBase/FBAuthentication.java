package com.example.ofrisproject.FireBase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ofrisproject.Adapters.SongAdapter;
import com.example.ofrisproject.Objects.Song;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.Registration.MainActivity;
import com.example.ofrisproject.Registration.RegisterCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FBAuthentication implements FBDatabase.OnDocumentsLoadedListener{

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private RegisterCallback MActivity;
    private FBDatabase fbDatabase= new FBDatabase();
    private RegisterCallback registerCallback;

    public FBAuthentication() {
    }

    public FBAuthentication(MainActivity activity){
        this.MActivity=activity;
    }


    public void registerUser(String email, String password) {
        //יצירת החשבון והרשמת המחשבון בFireBase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    MActivity.authenticateResult(true,"");

                }
                else
                {
                    MActivity.authenticateResult(false,task.getException().getMessage());

                }
            }
        });
    }

    private User currentUser;

    public User getCurrentUser(){
            fbDatabase.getDocuments("User","email", getUserEmail(), this);
            return currentUser;
    }

    public void onDocumentsLoaded(List<DocumentSnapshot> documents) {
        for (DocumentSnapshot document : documents) {
            currentUser= document.toObject(User.class);
        }
    }

    @Override
    public void onDocumentsError(Exception e) {
        // Handle the error here
        e.printStackTrace();
    }

    public boolean isRegistered()
    {
        return mAuth.getCurrentUser()!=null;
    }

    public String getUserEmail()
    {
        if(isRegistered())
            return mAuth.getCurrentUser().getEmail();
        else
            return "";
    }

    /* public User getCurrentUser(){
        if(isRegistered())
            return mAuth.getCurrentUser();
        else
            return null;
    } */


}
