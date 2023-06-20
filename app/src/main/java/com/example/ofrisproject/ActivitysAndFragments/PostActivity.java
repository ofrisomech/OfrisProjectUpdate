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

import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.Recording;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;


public class PostActivity extends AppCompatActivity implements FBDatabase.OnDocumentUploadedListener {

    private ImageView selectImage;
    private FBStorage fbStorage=new FBStorage();
    String imageRecUrl;
    FBAuthentication auth = new FBAuthentication();
    String mail =auth.getUserEmail();
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        selectImage = findViewById(R.id.BSelectImage);
        Button postRecording=findViewById(R.id.postRecording);
        postRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRecording(currentUser);
            }
        });
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

    private FBDatabase fbDatabase=new FBDatabase();
    private Recording r;
    private String email;


    // 1
    public void createRecording(User u) {

        String songName = getIntent().getStringExtra("songName");
        String userName = u.getUserName();
        String artistName = getIntent().getStringExtra("artistName");
        boolean isPrivate = Isprivate();
        email = u.getEmail();
        //    String Url= getIntent().getStringExtra("url");
        r = new Recording(songName, userName, artistName, isPrivate, "", imageRecUrl, email);

        fbDatabase.uploadDocument("recording",r, this);
    }


    @Override
    public void onDocumentUploaded() {
        // Document uploaded successfully
        fbStorage.uploadRecordingToStorage(r);
        String path=  "profiles/" + email + ".jpg";
        fbStorage.uploadImageToStorage(selectImage, path);
        Toast.makeText(this, "Document uploaded successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError(Exception e) {
        // Handle the error here
        e.printStackTrace();
        Toast.makeText(this, "Error uploading document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

     /*   FirebaseFirestore fb = FirebaseFirestore.getInstance();
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
*/



    public boolean Isprivate()
    {
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        if(simpleSwitch.isChecked())
            return true;
        return false;
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
