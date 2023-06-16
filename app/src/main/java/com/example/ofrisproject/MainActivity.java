package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements RegisterCallback {

    private static final int SELECT_PICTURE = 1;
    private FBAuthentication authentication;
    private String nickname="";
    private Uri selectedProfileImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        authentication=new FBAuthentication(this);
        if(authentication.isRegistered())//אם המשתמש כבר רשום למערכת
          MoveToHomePage();//עבור ישירות לעמוד הבית
        else
        {
            EditText editTextNickName= findViewById(R.id.editTextPersonName);
            String nickName = editTextNickName.getText().toString();
            checkNickNameLength(nickName);
        }
    }


    public void MoveToHomePage(){
        Intent intent = new Intent(this, BaseActivity.class);
       // intent.putExtra("nickname",nickname);
        //intent.putExtra("Uri",selectedProfileImageUri.toString());
        startActivity(intent);
    }

    public void checkNickNameLength(String nickname) {
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
    }

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
    }

    @Override
    public void authenticateResult(boolean success,String message) {
        if(success){
            EditText etMail = findViewById(R.id.editTextTextEmailAddress);
            String mail = etMail.getText().toString();
            EditText nickName=findViewById(R.id.editTextPersonName);
            nickname=nickName.toString();
            String userName = nickName.getText().toString();
            ImageView profileImg= findViewById(R.id.profileImage);
            UploadUserToFirebase(userName, mail, profileImg);
        }
        else
            Toast.makeText(this,"register fail " + message,Toast.LENGTH_SHORT).show();

    }


   
    public void imageChooser(View v) {

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
                ImageView profileImg= findViewById(R.id.profileImage);
                profileImg.setImageURI(selectedImageUri);
                selectedProfileImageUri=selectedImageUri;

            }
        }
    }
}
