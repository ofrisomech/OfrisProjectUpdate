package com.example.ofrisproject;

import android.widget.ImageView;

public class Recording{

    private String songName;
    private String userName;
    private String artistName;
    private boolean isPrivate;
    private String url;

    public Recording(String songName, String userName, String artistName, boolean isPrivate, String url, String imageRec) {
        this.songName = songName;
        this.userName=userName;
        this.artistName=artistName;
        this.isPrivate=isPrivate;
        this.url=url;
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

}
