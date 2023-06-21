package com.example.ofrisproject.ActivitysAndFragments;

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

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FBStorage fbStorage=new FBStorage();
    private FBAuthentication fbAuthentication=new FBAuthentication();
    private User currentUser=BaseActivity.user;

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
/*
        ImageView imageView= getView().findViewById(R.id.imageProfile);
        String path = "profiles/" + currentUser.getEmail() + ".jpg";
        fbStorage.downloadImageFromStorage(imageView, path);

        TextView textView=getView().findViewById(R.id.nickname);
        textView.setText(currentUser.getUserName());

        TextView follow=getView().findViewById(R.id.Followers);
        follow.setText(currentUser.getNumFollowers());

        TextView following=getView().findViewById(R.id.Following);
        following.setText(currentUser.getNumFollowing());

 */

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
                                    adapter = new RecordingAdapter(arr,(RecordingAdapter.AdapterCallback) getActivity());
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



}