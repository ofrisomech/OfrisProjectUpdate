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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.CommentAdapter;
import com.example.ofrisproject.Adapters.SongAdapter;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.Objects.Comment;
import com.example.ofrisproject.Objects.Song;
import com.example.ofrisproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class commentsFragment extends Fragment implements  FBDatabase.OnDocumentsLoadedListener{

    private String urlRec;
    public commentsFragment() {
        // Required empty public constructor
    }

    public commentsFragment(String url){
        urlRec=url;
    }

    public static commentsFragment newInstance(String param1, String param2) {
        commentsFragment fragment = new commentsFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText content= getView().findViewById(R.id.newComment);
        ImageView addComment= getView().findViewById(R.id.addComment);
        recyclerView=getView().findViewById(R.id.commentsRecyclerView);
        getComment();
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.toString().isEmpty())
                    AddComment(content.toString());
                else
                    Toast.makeText(getContext(), "The comment is empty " , Toast.LENGTH_SHORT).show();
            }
        });


    }

    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private CommentAdapter adapter;

    public void AddComment(String content){
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        Comment comment=new Comment("", content, mail, urlRec);
        fb.collection("Comment").add(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "The comment is upload " , Toast.LENGTH_SHORT).show();
                }
            };
        });
    }


    private FBDatabase fbDatabase=new FBDatabase();
    private ArrayList<Comment> arr;

    public void getComment(){
        fbDatabase.getDocuments("Comment", "urlRec", urlRec, this);
    }

    public void onDocumentsLoaded(List<DocumentSnapshot> documents) {
        arr = new ArrayList<>();
        if(documents.size()>0){
            for (DocumentSnapshot document : documents) {
                Comment comment = document.toObject(Comment.class);
                arr.add(comment);
        }
            adapter = new CommentAdapter(arr, (CommentAdapter.AdapterCallback) getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDocumentsError(Exception e) {
        Toast.makeText(getActivity(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }


}