package com.example.ofrisproject.FireBase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.ofrisproject.Objects.Recording;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FBStorage {

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageRef = firebaseStorage.getReference();

    public void downloadImageFromStorage(ImageView ivPostPhoto, String picturePath)
    {
        // at the moment add random name
        StorageReference imageRef = storageRef.child(picturePath);
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivPostPhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("image failed ", exception.getMessage());
            }
        });

    }


    public void uploadImageToStorage(ImageView selectedImage, String picturePath){

        Bitmap b = ((BitmapDrawable) selectedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imageRef = storageRef.child(picturePath);
        imageRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("storage upload ", "onComplete: success ");
                }
                else
                    Log.d("storage upload ", "onComplete: fail " + task.getException());

            }
        });


    }

    public void downloadRecordingFromStorage()
    {

    }

    public void uploadRecordingToStorage(Recording r){
        StorageReference sRef = storageRef.child("recordings/" + r.getUrl() + ".mp3");
        File f= new File(getApplicationInfo().dataDir + "/" + "recordingAudio.mp3");
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
                }

                else
                    Log.d("storage upload ", "onComplete: fail " + task.getException());

            }
        });

    }


    public void playRecordingFromStorage(){

    }
}
