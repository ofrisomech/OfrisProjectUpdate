package com.example.ofrisproject.ActivitysAndFragments;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.RecordingAdapter;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class otherUserFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener{
    private User otherUser;
    private FBStorage fbStorage=new FBStorage();
    private RecyclerView recyclerView;
    private RecordingAdapter adapter;
    private FBDatabase fbDatabase=new FBDatabase();
    private ArrayList<Recording> arr = new ArrayList<>();
    private User currentUser= BaseActivity.user;


    public otherUserFragment() {
        // Required empty public constructor
    }

    public otherUserFragment(User u) {
        // Required empty public constructor
        this.otherUser = u;
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
        textView.setText(otherUser.getUserName());
        Button button=getView().findViewById(R.id.Followers1);
        button.setText(""+ otherUser.getNumFollowers());
        Button button2=getView().findViewById(R.id.Following1);
        button2.setText(""+ otherUser.getNumFollowing());

        ImageView imageView= getView().findViewById(R.id.imageProfile);
        String path = "profiles/" + otherUser.getEmail() + ".jpg";
        fbStorage.downloadImageFromStorage(imageView, path);

        recyclerView= getView().findViewById(R.id.otheruserposts);
        fbDatabase.getDocuments("recording", "email", otherUser.getEmail(), otherUserFragment.this, FBDatabase.DEFAULT_ACTION);

        Button followCurrentUser= getView().findViewById(R.id.followButton);
        followCurrentUser.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v)
            {
                follow(currentUser.getEmail(), FBDatabase.FOLLOW_CURRENT_USER_ACTION);//addFollowingToCurrentUser
                follow(otherUser.getEmail(), FBDatabase.FOLLOW_OTHER_USER_ACTION);//addFollowerToUser
                int followers =Integer.valueOf(button.getText().toString());
                button.setText(""+(followers+1));
                followCurrentUser.setBackgroundColor(R.color.teal_200);
                followCurrentUser.setText("Following");
            }
        });

    }


    public void follow(String mail, int action){
        fbDatabase.getDocuments("user", "email", mail, otherUserFragment.this, action);
    }


    public void onDocumentsLoaded(List<DocumentSnapshot> documents, int action){
        if(action==FBDatabase.DEFAULT_ACTION) {
            if (documents.size() > 0) {
                for (DocumentSnapshot document : documents) {
                    Recording r = document.toObject(Recording.class);
                    if (!r.isPrivate())
                        arr.add(r);
                }
                adapter = new RecordingAdapter(arr, (RecordingAdapter.AdapterCallback) getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
            }
        }
        if(action==FBDatabase.FOLLOW_CURRENT_USER_ACTION){
            User currentUser = documents.get(0).toObject(User.class);
            DocumentReference ref = documents.get(0).getReference();
            currentUser.addFollowing(otherUser.getEmail());
            ref.update("following",currentUser.getFollowing());
        }
        if(action==FBDatabase.FOLLOW_OTHER_USER_ACTION){
            User other = documents.get(0).toObject(User.class);
            DocumentReference ref = documents.get(0).getReference();
            other.addFollower(currentUser.getEmail());
            ref.update("followers",other.getFollowers());
        }
    }

    public void onDocumentsError(Exception e){

    }

}