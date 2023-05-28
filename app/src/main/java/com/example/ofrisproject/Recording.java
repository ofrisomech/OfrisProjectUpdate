package com.example.ofrisproject;

import android.widget.ImageView;

import java.util.ArrayList;

public class Recording{

    private String songName;
    private String userName;
    private String artistName;
    private boolean isPrivate;
    private String url;
    private String email;
    private ArrayList<String> likeArr;
    //private ArrayList<Comment> commentsArr;
    private int numLikes;


    public Recording(String songName, String userName, String artistName, boolean isPrivate, String url, String imageRec, String email) {
        this.songName = songName;
        this.userName=userName;
        this.artistName=artistName;
        this.isPrivate=isPrivate;
        this.url=url;
        this.email=email;
        numLikes=0;
        likeArr=new ArrayList<>(numLikes);
    }

    public Recording() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumLikes(){return numLikes;}
    public void setNumLikes(int numLikes){this.numLikes=numLikes;}

}
