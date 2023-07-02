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
import com.example.ofrisproject.Objects.User;
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

public class commentsFragment extends Fragment implements  FBDatabase.OnDocumentsLoadedListener, FBDatabase.OnDocumentUploadedListener{

    private FBDatabase fbDatabase=new FBDatabase();
    private ArrayList<Comment> arr;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private String urlRec;


    public commentsFragment() {
    }

    public commentsFragment(String url){
        urlRec=url;
    }

    public static commentsFragment newInstance(String param1, String param2) {
        commentsFragment fragment = new commentsFragment();
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


    public void AddComment(String content){
        User currentUser= BaseActivity.user;
        Comment comment=new Comment(currentUser.getUserName(), content, currentUser.getEmail(), urlRec);
        fbDatabase.uploadDocument("Comment", comment, this);
    }

     @Override
    public void onDocumentUploaded() {
        // Document uploaded successfully
         Toast.makeText(getContext(), "The comment has been uploaded " , Toast.LENGTH_SHORT).show();
         getComment();// הצג גם את ההקלטה הנוספת
    }

    @Override
    public void onUploadError(Exception e) {
        // Handle the error here
        e.printStackTrace();
        Toast.makeText(getContext(), "Error uploading document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    public void getComment(){
        fbDatabase.getDocuments("Comment", "urlRec", urlRec, this, FBDatabase.DEFAULT_ACTION);
    }

    public void onDocumentsLoaded(List<DocumentSnapshot> documents, int action) {
        arr = new ArrayList<>();
        if(action==FBDatabase.DEFAULT_ACTION) {
            if (documents.size() > 0) {
                arr.clear(); // שלא יהיו כפילויות
                for (DocumentSnapshot document : documents) {
                    Comment comment = document.toObject(Comment.class);
                    arr.add(comment);
                }
                adapter = new CommentAdapter(arr, (CommentAdapter.AdapterCallback) getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDocumentsError(Exception e) {
        Toast.makeText(getActivity(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }


}