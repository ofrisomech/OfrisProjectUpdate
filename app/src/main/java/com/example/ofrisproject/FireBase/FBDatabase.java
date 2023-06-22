package com.example.ofrisproject.FireBase;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ofrisproject.ActivitysAndFragments.BaseActivity;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FBDatabase {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final int DEFAULT_ACTION = 0;
    public static final int DELETE_ACTION = 1;
    public static final int GET_USER_ACTION = 2;
    public static final int GET_USER_FRIENDS_ACTION = 3;
    public static final int GET_USER_NOT_FRIENDS_ACTION = 4;
    public static final int UPDATE_ACTION = 5;
    public static final int FOLLOW_CURRENT_USER=9;
    public static final int FOLLOW_OTHER_USER=10;



    public void getDocuments(String collectionName, String fieldName, Object fieldValue, final OnDocumentsLoadedListener listener,int action) {
        db.collection(collectionName).whereEqualTo(fieldName, fieldValue).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        // Retrieve the results
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        // Pass the documents to the listener
                        listener.onDocumentsLoaded(documents,action);
                    }
                } else {
                    // Handle the error
                    Exception e = task.getException();
                    listener.onDocumentsError(e);
                }
            }
        });
    }

    public interface OnDocumentsLoadedListener {
        void onDocumentsLoaded(List<DocumentSnapshot> documents,int action);
        void onDocumentsError(Exception e);


    }

    public void uploadDocument(String collectionName, Object object, final OnDocumentUploadedListener listener) {
        // Create a reference to the document

        DocumentReference ref = db.collection("recording").document();

        if(object instanceof Recording)
        {
            Recording r = (Recording) object;
            r.setUrl(ref.toString());
        }
        db.collection(collectionName).document().set(object)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Document uploaded successfully
                            listener.onDocumentUploaded();
                        } else {
                            // Error uploading document
                            Exception e = task.getException();
                            listener.onUploadError(e);
                        }
                    }
                });
    }

    public interface OnDocumentUploadedListener {
        void onDocumentUploaded();
        void onUploadError(Exception e);
    }


    public void getAllDocuments(String collectionName, final OnDocumentsLoadedListener listener,int action) {
        db.collection(collectionName).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                // Retrieve the results
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                                listener.onDocumentsLoaded(documents,action);
                            }
                        } else {
                            // Handle the error
                            Exception e = task.getException();
                            listener.onDocumentsError(e);
                        }
                    }
                });
    }

}
