package com.example.ofrisproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ProfileFragment extends Fragment implements RecordingAdapter.AdapterCallback{

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.profilerecycler);
        ImageView imageView= getView().findViewById(R.id.imageProfile);
        TextView textView=getView().findViewById(R.id.nickname);
        getCurrentUser(imageView,textView);

        ImageButton ibPublic = getView().findViewById(R.id.imagePublic);
        ibPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordingByPrivacy(false);
            }
        });

        ImageButton ibPrivate = getView().findViewById(R.id.imagePrivate);
        ibPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordingByPrivacy(true);
            }

        });
    }

     public void getCurrentUser(ImageView iv,TextView tv){
        final User[] user = {new User()};
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .whereEqualTo("email", mail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user[0] = document.toObject(User.class);

                            }
                            tv.setText(user[0].getUserName());
                            getImageFromFB(iv,user[0].getEmail());

                        }
                        else
                            Toast.makeText(getActivity()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show(); };
                });
    }

    private void getImageFromFB(ImageView iv, String email) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("profiles/" + email + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                // convert bytes to bitmap.. and set in image view

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

                Toast.makeText(getActivity()," image failed " + exception.getMessage(),Toast.LENGTH_SHORT).show();


            }
        });

    }



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference RecordingReference;
    private RecyclerView recyclerView;
    private RecordingAdapter adapter;
    private FBAuthentication auth = new FBAuthentication();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    public void getRecordingByPrivacy(boolean isPrivate) {
        String mail =auth.getUserEmail();

        ArrayList<Recording> arr = new ArrayList<>();
        db.collection("recording").whereEqualTo("email", mail)
                .whereEqualTo("private", isPrivate).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() -> array of recordings
                            if (task.getResult().getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Recording r = document.toObject(Recording.class);
                                    RecordingReference = document.getReference();
                                    arr.add(r);
                                }
                                    //חיבור לתצוגה
                                    adapter = new RecordingAdapter(arr,(RecordingAdapter.AdapterCallback) ProfileFragment.this);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    // display oin recycler view

                            } else {
                                Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getActivity()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void RecordingChosen(Recording r) {

    }

}