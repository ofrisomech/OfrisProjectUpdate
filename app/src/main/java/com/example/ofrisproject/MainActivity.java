package com.example.ofrisproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements RegisterCallback {

    private FBAuthentication authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authentication=new FBAuthentication(this);
        if(authentication.isRegistered())//אם המשתמש כבר רשום למערכת
           MoveToHomePage();//עבור ישירות לעמוד הבית
    }


    public void MoveToHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void Register(View view){

        //שמירת נתונים
        EditText etName=findViewById(R.id.editTextTextPersonName);
        String Name= etName.getText().toString();
        EditText etMail = findViewById(R.id.editTextTextEmailAddress);
        String mail = etMail.getText().toString();
        EditText etPassword = findViewById(R.id.editTextTextPassword);
        String password = etPassword.getText().toString();
        EditText etPhone = findViewById(R.id.editTextPhone);
        String Phone = etPhone.getText().toString();
        EditText etNickname = findViewById(R.id.editTextTextPersonName2);
        String Nickname = etNickname.getText().toString();
        authentication.registerUser(mail, password);

    }

    @Override
    public void authenticateResult(boolean success) {
        if(success){
            Intent intent= new Intent(this, MainActivity.class);
        }
    }
}
