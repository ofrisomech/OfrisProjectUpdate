package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class SelectPhoto extends AppCompatActivity {


    // One Preview Image
    ImageView selectImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    String imageRecUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        selectImage = findViewById(R.id.BSelectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    public void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                imageRecUrl=selectedImageUri.toString();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    selectImage.setImageURI(selectedImageUri);
                }
            }
        }
    }


    // 1
    public void createRecording(User u){

        String songName= getIntent().getStringExtra("songName");
        String userName= u.getUserName();
        String artistName=getIntent().getStringExtra("artistName");
        boolean isPrivate= Isprivate();
    //    String Url= getIntent().getStringExtra("url");
        Recording r=new Recording(songName, userName, artistName, isPrivate, "", imageRecUrl);

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        DocumentReference ref = fb.collection("recording").document();
        r.setUrl(ref.toString());
        ref.set(r).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // recording document loaded to firebase
                // upload file to strorage

                // My Projetc reference
                UploadRecordingToFB(r);
    }

    public void UploadRecordingToFB(Recording r)
    {

                StorageReference sRef = storage.getReference().child(r.getUrl() + ".mp3");

                File f = new File(getApplicationInfo().dataDir + "/" + "recordingAudio.mp3");

                InputStream stream = null;
                try {
                    stream = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                sRef.putStream(stream).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                            Log.d("storage upload ", "onComplete: success ");
                        else
                            Log.d("storage upload ", "onComplete: fail " + task.getException());

                    }
                });
            }
        });
    }


    public boolean Isprivate()
    {
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        if(simpleSwitch.isChecked())
            return true;
        return false;
    }

    public void GetCurrentUser(View v){
        final User[] user = {new User()};
        FBAuthentication auth = new FBAuthentication();
        String mail =auth.getUserEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .whereEqualTo("email", mail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user[0] = document.toObject(User.class);

                            }
                            createRecording(user[0]);

                        }
                        else
                            Toast.makeText(getApplicationContext()," " + task.getException().getMessage(),Toast.LENGTH_SHORT).show(); };
                });
    }

}
