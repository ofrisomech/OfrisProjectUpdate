package com.example.ofrisproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFriendsFragment extends Fragment {




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFriendsFragment newInstance(String param1, String param2) {
        SearchFriendsFragment fragment = new SearchFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_friends, container, false);

    }


    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.searchFriendsRV);
        getUsers();
    }


    public void getUsers(){
        ArrayList<User> arr = new ArrayList<>();
        db.collection("User").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // task.getResult() -> array of songs
                            if (task.getResult().getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    arr.add(user);
                                }
                                //חיבור לתצוגה
                                adapter = new UserAdapter(arr,(UserAdapter.AdapterCallback) SearchFriendsFragment.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                // display on recycler view
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getActivity()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}