package com.example.ofrisproject.ActivitysAndFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ofrisproject.Adapters.SongAdapter;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.Objects.Song;
import com.example.ofrisproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener{

    private String genre;
    private ArrayList<Song> arr;

    public CreateFragment() {
        // Required empty public constructor
    }

    public CreateFragment(String genre) {
        this.genre = genre;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);

    }

    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private FBDatabase fbDatabase=new FBDatabase();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);

        fbDatabase.getDocuments("Song","genre", genre, this);

        //getSongsByGenre();
        EditText edittext = getView().findViewById(R.id.searchSongs);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                findSongs(editable.toString());
            }
        });

    }

    public void findSongs(String text) {
        ArrayList<Song> brr = new ArrayList<>();
        if (!text.trim().isEmpty()) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getSongName().startsWith(text.trim()))
                    brr.add(arr.get(i));
            }
            if (brr.size() == 0) {
                Toast.makeText(getActivity(), "NO RESULTS FOUND", Toast.LENGTH_SHORT).show();
                adapter.setSongs(brr);
            }

            } else
                adapter.setSongs(arr);
            adapter.notifyDataSetChanged();

    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference songReference;


    public void onDocumentsLoaded(List<DocumentSnapshot> documents) {
        arr = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            Song s = document.toObject(Song.class);
            arr.add(s);
        }

        adapter = new SongAdapter(arr,(SongAdapter.AdapterCallback) getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onDocumentsError(Exception e) {
        // Handle the error here
        e.printStackTrace();
    }


    /* public void getSongsByGenre() {
        arr = new ArrayList<>();
        db.collection("Song")
               .whereEqualTo("genre", genre).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() -> array of songs
                            if (task.getResult().getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Song s = document.toObject(Song.class);
                                    songReference = document.getReference();
                                    arr.add(s);
                                }
                                    //חיבור לתצוגה
                                    adapter = new SongAdapter(arr,(SongAdapter.AdapterCallback) getActivity());
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    // display oin recycler view
                                }
                             else {
                                Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getActivity()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

     */
}