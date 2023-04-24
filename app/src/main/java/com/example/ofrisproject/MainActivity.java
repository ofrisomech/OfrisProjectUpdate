package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements RegisterCallback {

    private FBAuthentication authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authentication=new FBAuthentication(this);
       // if(authentication.isRegistered())//אם המשתמש כבר רשום למערכת
         //  MoveToHomePage();//עבור ישירות לעמוד הבית
    }


    public void MoveToHomePage(){
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }

    public void Register(View view){

        //שמירת נתונים
        EditText etMail = findViewById(R.id.editTextTextEmailAddress);
        String mail = etMail.getText().toString();
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        String password = etPassword.getText().toString();
        authentication.registerUser(mail, password);


    }

    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void UploadUserToFirebase(String userName, String email, ImageView profileImage){

        // upload image to fb
        // get the link
        // set the user in fb with the link

        // 1
        // convert image to byte array
        User user=new User(userName, email, email);
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
    }

    @Override
    public void authenticateResult(boolean success,String message) {
        if(success){
            EditText etMail = findViewById(R.id.editTextTextEmailAddress);
            String mail = etMail.getText().toString();
            EditText nickName=findViewById(R.id.editTextPersonName);
            String userName = nickName.getText().toString();
            ImageView profileImg= findViewById(R.id.profileImage);
            UploadUserToFirebase(userName, mail, profileImg);
        }
        else
            Toast.makeText(this,"register fail " + message,Toast.LENGTH_SHORT).show();

    }


    public void selectImageProfile(){


    }
}
