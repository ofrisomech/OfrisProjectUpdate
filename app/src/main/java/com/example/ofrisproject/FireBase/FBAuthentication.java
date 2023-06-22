package com.example.ofrisproject.FireBase;

import androidx.annotation.NonNull;

import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.Registration.registerActivity;
import com.example.ofrisproject.Registration.RegisterCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class FBAuthentication {

    private FirebaseAuth mAuth;
    private RegisterCallback registerCallback;

    public FBAuthentication() {
        this.mAuth=FirebaseAuth.getInstance();
    }

    public FBAuthentication(registerActivity activity){
        this.registerCallback=activity;
        this.mAuth=FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    registerCallback.authenticateResult(true,"");
                else
                    registerCallback.authenticateResult(false,task.getException().getMessage());
            }
        });
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

}
