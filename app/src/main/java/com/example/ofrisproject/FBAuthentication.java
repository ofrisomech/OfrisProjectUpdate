package com.example.ofrisproject;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FBAuthentication {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private RegisterCallback MActivity;
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
