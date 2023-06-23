package com.example.ofrisproject.ActivitysAndFragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;


public class PostActivity extends AppCompatActivity implements FBDatabase.OnDocumentUploadedListener {

    private ImageView selectedImage;
    private FBStorage fbStorage=new FBStorage();
    private FBDatabase fbDatabase=new FBDatabase();
    private User currentUser= BaseActivity.user;
    private Recording r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        selectedImage = findViewById(R.id.BSelectImage);
        Button postRecording=findViewById(R.id.postRecording);
        postRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRecording();
            }
        });
        selectedImage.setOnClickListener(new View.OnClickListener() {
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
                    selectedImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


    public void createRecording() {
        String songName = getIntent().getStringExtra("songName");
        String userName = currentUser.getUserName();
        String artistName = getIntent().getStringExtra("artistName");
        boolean isPrivate = Isprivate();
        r = new Recording(songName, userName, artistName, isPrivate, "", currentUser.getEmail());
        fbDatabase.uploadDocument("recording",r, this);
    }


    @Override
    public void onDocumentUploaded() {
        // Document uploaded successfully
        fbStorage.uploadRecordingToStorage(r,this);
        String path=  "profiles/" + currentUser.getEmail() + ".jpg";
        fbStorage.uploadImageToStorage(selectedImage, path);
        Toast.makeText(this, "Document uploaded successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError(Exception e) {
        // Handle the error here
        e.printStackTrace();
        Toast.makeText(this, "Error uploading document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    public boolean Isprivate()
    {
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        if(simpleSwitch.isChecked())
            return true;
        return false;
    }
}
