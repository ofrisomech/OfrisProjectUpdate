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
import android.widget.Button;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener {

    private FBDatabase fbDatabase=new FBDatabase();
    private  ArrayList<Recording> arr = new ArrayList<>();

    private  Button foryouB;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        fbDatabase.getDocuments("recording","private", false, HomeFragment.this,FBDatabase.GET_USER_NOT_FRIENDS_ACTION);


        //   fbDatabase.getDocuments("recording","private", false, this,FBDatabase.GET_USER_NOT_FRIENDS_ACTION);
         foryouB = getView().findViewById(R.id.foryou);
        foryouB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<Recording> subArray = new ArrayList<>();

                for (Recording r:arr) {
                    if (!currentUser.getFollowing().contains(r.getEmail()))
                        subArray.add(r);

                }

                displayAdapter(subArray);





            }
        });


        Button friendsB = getView().findViewById(R.id.friends);
        friendsB.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                ArrayList<Recording> subArray = new ArrayList<>();

                for (Recording r:arr) {
                    if (currentUser.getFollowing().contains(r.getEmail()))
                        subArray.add(r);

                }

                displayAdapter(subArray);



            }

        });


     //   foryouB.performClick();

    }

    private void displayAdapter(ArrayList<Recording> arr) {

        adapter = new RecordingAdapter(arr, (RecordingAdapter.AdapterCallback) getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private User currentUser= BaseActivity.user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference RecordingReference;

    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents,int action) {

        arr.clear();
        if (documents.size() > 0) {
            for (DocumentSnapshot document : documents) {
                Recording r = document.toObject(Recording.class);


                        arr.add(r);


            }



            foryouB.callOnClick();









        }
        else
            Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDocumentsError(Exception e) {
        // Handle the error here
        e.printStackTrace();
    }


}