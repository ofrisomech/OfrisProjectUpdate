package com.example.ofrisproject.FireBase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.example.ofrisproject.ActivitysAndFragments.BaseActivity;
import com.example.ofrisproject.Objects.Recording;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class FBStorage {

    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    public FBStorage(){
        firebaseStorage= FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
    }
    public void downloadImageFromStorage(ImageView ivPostPhoto, String picturePath) {
        StorageReference imageRef = storageRef.child(picturePath);
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivPostPhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("image failed ", exception.getMessage());
            }
        });
    }
    public void uploadImageToStorage(ImageView selectedImage, String picturePath) {
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
                } else
                    Log.d("storage upload ", "onComplete: fail " + task.getException());
            }
        });
    }
    public void downloadRecordingFromStorage(Recording r, SeekBar sb) {
        StorageReference recordingRef = storageRef.child("recordings/" + r.getUrl() + ".mp3");
        try {
            File localFile = File.createTempFile("audio", "mp3");
            recordingRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        FileInputStream fis = new FileInputStream(localFile);
                        mediaPlayer.setDataSource(fis.getFD());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        sb.setMax(mediaPlayer.getDuration());
                        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mediaPlayer.seekTo(progress);
                                }
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) { }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        });

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                int currentPosition = mediaPlayer.getCurrentPosition();
                                sb.setProgress(currentPosition);
                                if (mediaPlayer.getDuration() < currentPosition + 1000) {
                                    timer.cancel();
                                }
                            }
                        }, 0, 1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("Error playing audio:  ", e.getMessage());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.d("Error downloading audio:  ", e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error creating temporary file: ", e.getMessage());
        }

    }
    public void deleteRecordingFromStorage(Recording r) {
        StorageReference ref = storageRef.child("recording/" + r.getUrl() + ".mp3");
        ref.delete();
    }
    public void uploadRecordingToStorage(Recording r, Context c) {
        StorageReference sRef = storageRef.child("recordings/" + r.getUrl() + ".mp3");
        File f = new File(c.getApplicationInfo().dataDir + "/" + "recordingAudio.mp3");
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

                    //  ((Activity)c).finish();
                    Intent i = new Intent(c, BaseActivity.class);
                    c.startActivity(i);
                } else
                    Log.d("storage upload ", "onComplete: fail " + task.getException());
            }
        });
    }
}
