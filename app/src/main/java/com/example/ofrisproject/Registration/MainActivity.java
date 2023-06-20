package com.example.ofrisproject.Registration;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ofrisproject.ActivitysAndFragments.BaseActivity;
import com.example.ofrisproject.FireBase.FBAuthentication;
import com.example.ofrisproject.FireBase.FBDatabase;
import com.example.ofrisproject.FireBase.FBStorage;
import com.example.ofrisproject.Objects.User;
import com.example.ofrisproject.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements RegisterCallback, FBDatabase.OnDocumentUploadedListener{

    private FBAuthentication authentication;
    private String nickname="";
    private ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        profileImg= findViewById(R.id.profileImage);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");

            }
        });

        authentication=new FBAuthentication(this);
        if(authentication.isRegistered())//אם המשתמש כבר רשום למערכת
          MoveToHomePage();//עבור ישירות לעמוד הבית
        else
        {
            EditText editTextNickName= findViewById(R.id.editTextPersonName);
            String nickName = editTextNickName.getText().toString();
            //checkNickNameLength(nickName);
        }
    }


    public void MoveToHomePage(){
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }

   /* public void checkNickNameLength(String nickname) {
        int maxLength = 12; // Set the desired maximum password length

        if (nickname.length() > maxLength) {
            // Password is too long
            showMessage("Password is too long");
        } else {
            // Password length is within the limit
            showMessage("Password is valid");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }*/

    /*public boolean checkEmailExists(String email) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance().(email);
            // User with the specified email exists
            showMessage("This email is already registered");
            return true;
        } catch (FirebaseAuthException e) {
            // User with the specified email does not exist
            return false;
        }
    }*/

    /*
    public boolean checkNameExists(String nickname){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Query query = db.collection("User").whereEqualTo("userName", nickname).limit(1);
        QuerySnapshot querySnapshot = query.get().getResult();
        // Check if any document matches the query
        if (querySnapshot != null && !querySnapshot.isEmpty()) {
            // The string exists in the collection
            showMessage("The name is not available");
            return true;
        }

        // The string does not exist in the collection
        return false;


    }*/


    public void Register(View view){

        //שמירת נתונים
        EditText etMail = findViewById(R.id.editTextTextEmailAddress);
        String mail = etMail.getText().toString();
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        String password = etPassword.getText().toString();
        authentication.registerUser(mail, password);
    }


    FBDatabase fbDatabase=new FBDatabase();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    FBStorage fbStorage=new FBStorage();

    public void UploadUserToFirebase(String userName, String email, ImageView profileImage){
        User user=new User(userName, email, email, "", "");
        fbDatabase.uploadDocument("User", user, this);
    }


    public void onDocumentUploaded() {
        // Document uploaded successfully
        String mail=authentication.getUserEmail();
        String picturePath="profiles/" + mail + ".jpg";
        fbStorage.uploadImageToStorage(profileImg,picturePath);
        Toast.makeText(this, "Document uploaded successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError(Exception e) {
        // Handle the error here
        e.printStackTrace();
        Toast.makeText(this, "Error uploading document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /* public void UploadUserToFirebase(String userName, String email, ImageView profileImage){
        User user=new User(userName, email, email, "", "");
        fb.collection("User").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Bitmap b = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference ref = storage.getReference().child("profiles/" + email + ".jpg");

                    ref.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(MainActivity.this, BaseActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(MainActivity.this, "fail " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            };
        });
    }*/

    @Override
    public void authenticateResult(boolean success,String message) {
        if(success){
            EditText etMail = findViewById(R.id.editTextTextEmailAddress);
            String mail = etMail.getText().toString();
            EditText nickName=findViewById(R.id.editTextPersonName);
            nickname=nickName.toString();
            String userName = nickName.getText().toString();
            profileImg= findViewById(R.id.profileImage);
            UploadUserToFirebase(userName, mail, profileImg);
            MoveToHomePage();
        }
        else
            Toast.makeText(this,"register fail " + message,Toast.LENGTH_SHORT).show();

    }


   
    public void imageChooser(View v) {
        mGetContent.launch("image/*");

    }

    // Launcher
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    profileImg.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


}

