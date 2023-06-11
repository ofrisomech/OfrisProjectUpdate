package com.example.ofrisproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class otherUserFragment extends Fragment {
private User user;

public otherUserFragment() {
        // Required empty public constructor
    }


    public otherUserFragment(User u) {
        // Required empty public constructor
        this.user = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_user, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView=getView().findViewById(R.id.nickname);
        textView.setText(user.getUserName());
        Button button=getView().findViewById(R.id.Followers1);
        String[] userFollowers = user.getFollowers().split(",");
        button.setText(""+(userFollowers.length-1));
        Button button2=getView().findViewById(R.id.Following1);
        String[] userFollowings = user.getFollowing().split(",");
        button2.setText(""+(userFollowings.length-1));
       // ImageView imageView=getView().findViewById(R.id.imageProfile);
        //imageView.setImageResource(user.getProfileImage().getBytes());
        recyclerView= getView().findViewById(R.id.otheruserposts);
        getOtherUserPosts();


        Button followCurrentUser= getView().findViewById(R.id.followButton);
        followCurrentUser.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v)
            {
                follow();
                int followers =Integer.valueOf(button.getText().toString());
                button.setText(""+(followers+1));
                followCurrentUser.setBackgroundColor(R.color.teal_200);
                followCurrentUser.setText("Following");


            }
        });

    }




    public void follow(){

        addFollowerToUser();
        addFollowingToCurrentUser();
    }

    private void addFollowingToCurrentUser() {
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        FirebaseFirestore fb= FirebaseFirestore.getInstance();
        fb.collection("User").whereEqualTo("email",mail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                User currentUser = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                DocumentReference ref = queryDocumentSnapshots.getDocuments().get(0).getReference();
                currentUser.addFollowing(user.getEmail());
               ref.update("following",currentUser.getFollowing());


            }
        });
    }

    private void addFollowerToUser() {
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        FirebaseFirestore fb= FirebaseFirestore.getInstance();
        fb.collection("User").whereEqualTo("email",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                User other = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                DocumentReference ref = queryDocumentSnapshots.getDocuments().get(0).getReference();
                other.addFollower(mail);
                ref.update("followers",other.getFollowers());

            }
        });


    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecordingAdapter adapter;


    public void getOtherUserPosts(){
        ArrayList<Recording> arr = new ArrayList<>();
        db.collection("recording").whereEqualTo("private", false).whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() -> array of songs
                            if (task.getResult().getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Recording r = document.toObject(Recording.class);
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



}