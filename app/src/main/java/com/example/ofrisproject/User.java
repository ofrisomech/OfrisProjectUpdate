package com.example.ofrisproject;

import android.widget.ImageView;

public class User {

    private String userName;
    private String email;
    private String profileImage;
    private String followers;
    private String following;

    public User(String userName, String email, String profileImage, String followers, String following)
    {
        this.userName=userName;
        this.email=email;
        this.profileImage=profileImage;
        this.followers=followers;
        this.following=following;
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

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
