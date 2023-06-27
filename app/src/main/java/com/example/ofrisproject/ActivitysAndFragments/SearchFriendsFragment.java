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
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendsFragment extends Fragment implements FBDatabase.OnDocumentsLoadedListener {
    private ArrayList<User> arr;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private FBDatabase fbDatabase=new FBDatabase();


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
        User currentUser= BaseActivity.user;
        fbDatabase.getDocumentsExceptSpecificObject("User", "email", currentUser.getEmail(),SearchFriendsFragment.this, FBDatabase.DEFAULT_ACTION );
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
        ArrayList<User> brr = new ArrayList<>();// תת מערך מסונן
        if (!text.trim().isEmpty()) // אם המחרוזת אינה ריקה או מכילה רק רווחים
        {
            for (int i = 0; i < arr.size(); i++) // מעבר על המערך
            {
                // אם המשתמש באותו מיקום מתחיל במה שהמשתמש הקליד, הוא מכניס אותו למערך אופצינלי
                if (arr.get(i).getUserName() != null && arr.get(i).getUserName().startsWith(text.trim()))
                    brr.add(arr.get(i));
            }
            if (brr.size() == 0)// אם אין תוצאות מתאימות מוצגת הודעה מתאימה
                Toast.makeText(getActivity(), "NO RESULTS FOUND", Toast.LENGTH_SHORT).show();
            adapter.setUsers(brr);// הצג את תת המערך
        } else// אם המחרוזת ריקה
            adapter.setUsers(arr);//- הצג את המערך הרגיל כל המשתמשים
        adapter.notifyDataSetChanged();// משתנה בהתאם לחיפוש המשתמש
    }


    @Override
    public void onDocumentsLoaded(List<DocumentSnapshot> documents, int action) {
        if (documents.size() > 0) {
            for (DocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                arr.add(user);
            }
            adapter = new UserAdapter(arr, (UserAdapter.AdapterCallback) getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getActivity(), "Error getting documents: no documents ", Toast.LENGTH_SHORT).show();
        getView().findViewById(R.id.searchUsers).setEnabled(true);
    }


    @Override
    public void onDocumentsError(Exception e) {
        // Handle the error here
        e.printStackTrace();
        Toast.makeText(getActivity(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}