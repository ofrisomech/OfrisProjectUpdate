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

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener {

    private FBDatabase fbDatabase=new FBDatabase();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private RecyclerView recyclerView;
    private RecordingAdapter adapter;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView1);

        /* Button foryouB = getView().findViewById(R.id.foryou);
        foryouB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordingByPrivacy(false);
            }
        });

        Button friendsB = getView().findViewById(R.id.friends);
        friendsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordingByPrivacy(true);
            }

        }); */

        fbDatabase.getDocuments("recording","private", false, this);
    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference RecordingReference;

    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents) {
        ArrayList<Recording> arr = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            Recording r = document.toObject(Recording.class);
            arr.add(r);
        }

        adapter = new RecordingAdapter(arr, (RecordingAdapter.AdapterCallback) getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDocumentsError(Exception e) {
        // Handle the error here
        e.printStackTrace();
    }



   /* public void getPosts() {
        ArrayList<Recording> arr = new ArrayList<>();
        db.collection("recording").whereEqualTo("private", false).get()
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
                                //חיבור לתצוגה
                                adapter = new RecordingAdapter(arr, (RecordingAdapter.AdapterCallback) getActivity());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                // display on recycler view
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getActivity(), " " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
*/



}