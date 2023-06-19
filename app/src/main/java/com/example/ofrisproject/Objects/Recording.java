package com.example.ofrisproject.Objects;

import android.widget.ImageView;

import java.util.ArrayList;

public class Recording{

    private String songName;
    private String userName;
    private String artistName;
    private boolean isPrivate;
    private String url;
    private String email;
    private String like;
    //private ArrayList<Comment> commentsArr;


    public Recording(String songName, String userName, String artistName, boolean isPrivate, String url, String imageRec, String email) {
        this.songName = songName;
        this.userName=userName;
        this.artistName=artistName;
        this.isPrivate=isPrivate;
        this.url=url;
        this.email=email;
        this.like="";
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

    public boolean addLike(String mail)
    {
        if(this.like.isEmpty())
        {
            this.like =mail;
            return true;
        }

        if(this.like.contains(mail))
        {
            if(this.like.indexOf(mail)==0)
                this.like = this.like.replace(mail,"");
            else
                this.like = this.like.replace(","+ mail,"");
            return false;
        }
        this.like+=","+mail;
        return true;

    }

    public String getLike(){return like; }
    public void setLike(String like){this.like=like;}



}
