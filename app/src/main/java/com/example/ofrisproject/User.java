package com.example.ofrisproject;

import android.widget.ImageView;

public class User {

    private String userName;
    private String email;
    private String profileImage;

    public User(String userName, String email, String profileImage)
    {
        this.userName=userName;
        this.email=email;
        this.profileImage=profileImage;
    }
    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
