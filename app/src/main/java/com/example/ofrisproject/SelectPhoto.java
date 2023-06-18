package com.example.ofrisproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class SelectPhoto extends AppCompatActivity {

    private ImageView selectImage;
    private FBStorage fbStorage=new FBStorage();
    String imageRecUrl;
    FBAuthentication auth = new FBAuthentication();
    String mail =auth.getUserEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        selectImage = findViewById(R.id.BSelectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    selectImage.setImageBitmap(bitmap);
                    String picturePath="profiles/" + mail + ".jpg";
                    fbStorage.uploadImageToStorage(selectImage,picturePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });



    // 1
    public void createRecording(User u) {

        String songName = getIntent().getStringExtra("songName");
        String userName = u.getUserName();
        String artistName = getIntent().getStringExtra("artistName");
        boolean isPrivate = Isprivate();
        String email=u.getEmail();
        //    String Url= getIntent().getStringExtra("url");
        Recording r = new Recording(songName, userName, artistName, isPrivate, "", imageRecUrl, email);

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        FBStorage fbStorage=new FBStorage();

        DocumentReference ref = fb.collection("recording").document();
        r.setUrl(ref.toString());
        ref.set(r).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // upload recording and recording image
               fbStorage.uploadRecordingToStorage(r);
               String path=  "profiles/" + email + ".jpg";
                fbStorage.uploadImageToStorage(selectImage, path);

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String m = e.getMessage();
                        Toast.makeText(SelectPhoto.this,m,Toast.LENGTH_SHORT).show();
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


     /* public void UploadRecordingToFB(Recording r)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference sRef = storage.getReference().child("recordings/" + r.getUrl() + ".mp3");

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
                        if (task.isSuccessful()) {

                            Log.d("storage upload ", "onComplete: success ");
                            uploadRecordingImageToFB(r);

                        }

                        else
                            Log.d("storage upload ", "onComplete: fail " + task.getException());

                    }
                });
            }

    private void uploadRecordingImageToFB(Recording r) {


        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference sRef = storage.getReference().child("recordingphoto/" + r.getUrl() + ".jpeg");
        Bitmap b = ((BitmapDrawable) selectImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        sRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("storage upload ", "onComplete: success ");

                    Intent i = new Intent(SelectPhoto.this,BaseActivity.class);
                    startActivity(i);
                    finish();



                }

                else
                    Log.d("storage upload ", "onComplete: fail " + task.getException());

            }
        });
    }*/

}
