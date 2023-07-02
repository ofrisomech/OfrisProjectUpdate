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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener {

    private FBStorage fbStorage=new FBStorage();
    private FBDatabase fbDatabase= new FBDatabase();
    private User currentUser=BaseActivity.user;
    private  ArrayList<Recording> arr = new ArrayList<>();
    private ImageButton imgButtonPrivate;
    private RecyclerView recyclerView;


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
        String path = "profiles/" + currentUser.getEmail() + ".jpg";
        fbStorage.downloadImageFromStorage(imageView, path);

        TextView textView=getView().findViewById(R.id.nickname);
        textView.setText(currentUser.getUserName());

        TextView follow=getView().findViewById(R.id.Followers);
        follow.setText(currentUser.getNumFollowers());

        TextView following=getView().findViewById(R.id.Following);
        following.setText(currentUser.getNumFollowing());

        fbDatabase.getDocuments("recording", "email", currentUser.getEmail(), this, FBDatabase.DEFAULT_ACTION);

        imgButtonPrivate= getView().findViewById(R.id.imagePrivate);
        imgButtonPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Recording> subArray = new ArrayList<>();
                for (Recording r:arr) {
                    if (r.isPrivate())
                        subArray.add(r);
                }
                displayAdapter(subArray);
            }
        });

        ImageButton imgButtonPublic = getView().findViewById(R.id.imagePublic);
        imgButtonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Recording> subArray = new ArrayList<>();
                for (Recording r:arr) {
                    if (!r.isPrivate())
                        subArray.add(r);
                }
                displayAdapter(subArray);
            }
        });
    }


    private void displayAdapter(ArrayList<Recording> arr) {
        RecordingAdapter adapter= new RecordingAdapter(arr, (RecordingAdapter.AdapterCallback) getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents, int action) {
        arr.clear();
        if (documents.size() > 0) {
            for (DocumentSnapshot document : documents) {
                Recording r = document.toObject(Recording.class);
                arr.add(r);
            }
            imgButtonPrivate.callOnClick();
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