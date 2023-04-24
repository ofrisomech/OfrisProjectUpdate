package com.example.ofrisproject;

public class Recording{

    private String songName;
    private String userName;
    private String artistName;
    private String emailUser;
    private boolean isPrivate;
    private String url;

    public Recording(String songName, String userName, String artistName, String emailUser, boolean isPrivate, String url) {
        this.songName = songName;
        this.userName=userName;
        this.artistName=artistName;
        this.emailUser=emailUser;
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
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
