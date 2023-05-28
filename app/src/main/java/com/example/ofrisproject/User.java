package com.example.ofrisproject;

import android.widget.ImageView;

public class User {

    private String userName;
    private String email;
    private String profileImage;
    private int numFollowers;
    private int numFollowing;

    public User(String userName, String email, String profileImage)
    {
        this.userName=userName;
        this.email=email;
        this.profileImage=profileImage;
        numFollowers=0;
        numFollowing=0;
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

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public void setNumFollowing(int numFollowing) {
        this.numFollowing = numFollowing;
    }
}
