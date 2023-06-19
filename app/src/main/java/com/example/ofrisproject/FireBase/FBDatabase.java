package com.example.ofrisproject.FireBase;

import com.example.ofrisproject.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class FBDatabase {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FBAuthentication auth = new FBAuthentication();
    private String mail =auth.getUserEmail();
    private User currentUser;


    /*
    public User GetCurrentUser(){
        db.collection("User")
                .whereEqualTo("email", mail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUser= document.toObject(User.class);
                            }
                            return currentUser;
                        }
                        else
                            Toast.makeText(getApplicationContext()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show(); };
                });
        return null;
    }*/


/*
    public void getDocuments(Object type, String collectionPath, String field, Boolean valueField) {
        ArrayList<Object> arr = new ArrayList<>();
        // פעולה של עצמים בכללי
        db.collection(collectionPath).whereEqualTo(field, valueField).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() -> array of songs
                            if (task.getResult().getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Recording r = document.toObject(Recording.class);
                                    RecordingReference = document.getReference();
                                    arr.add(r);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getActivity(), " " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    public void getDocuments(String collectionName, String fieldName, Object fieldValue, final OnDocumentsLoadedListener listener) {
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
                        listener.onDocumentsLoaded(documents);
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
        void onDocumentsLoaded(List<DocumentSnapshot> documents);
        void onDocumentsError(Exception e);
    }

    public void uploadDocument(String collectionName, Object object, final OnDocumentUploadedListener listener) {
        // Create a reference to the document
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




}
