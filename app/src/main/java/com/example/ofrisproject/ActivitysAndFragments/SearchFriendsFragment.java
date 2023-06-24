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

import com.example.ofrisproject.Adapters.UserAdapter;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFriendsFragment extends Fragment {
    private ArrayList<User> arr;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public SearchFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_friends, container, false);

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.searchFriendsRV);
        getUsers();
        EditText edittext = getView().findViewById(R.id.searchUsers);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                findMatchedFriends(editable.toString());
            }
        });
    }

    public void findMatchedFriends(String text) {
        ArrayList<User> brr = new ArrayList<>();
        if (!text.trim().isEmpty()) {
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getUserName() != null && arr.get(i).getUserName().startsWith(text.trim()))
                    brr.add(arr.get(i));
            }
            if (brr.size() == 0)
                Toast.makeText(getActivity(), "NO RESULTS FOUND", Toast.LENGTH_SHORT).show();
            adapter.setUsers(brr);
        } else
            adapter.setUsers(arr);
        adapter.notifyDataSetChanged();
    }


    public void getUsers() {
        arr = new ArrayList<>();
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        db.collection("User").whereNotEqualTo("email", mail).get()
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
                                adapter = new UserAdapter(arr, (UserAdapter.AdapterCallback) getActivity());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                // display on recycler view
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
                            }
                            getView().findViewById(R.id.searchUsers).setEnabled(true);
                        } else
                            Toast.makeText(getActivity(), " " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}