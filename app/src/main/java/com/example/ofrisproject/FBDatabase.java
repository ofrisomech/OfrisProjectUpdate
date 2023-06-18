package com.example.ofrisproject;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class FBDatabase {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FBAuthentication auth = new FBAuthentication();
    private String mail =auth.getUserEmail();
    private User currentUser;


    public void GetCurrentUser(){
        db.collection("User")
                .whereEqualTo("email", mail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUser= document.toObject(User.class);

                            }
                        }
                        else
                            Toast.makeText(getApplicationContext()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show(); };
                });
    }




}
