package com.example.ofrisproject;

public class post {

    private String userName;
    private String songName;
    private String artistName;
    private String content;

    public post(String userName, String songName, String artistName, String content) {
        this.userName = userName;
        this.songName = songName;
        this.artistName=artistName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName(){return artistName;}

    public String getContent() {
        return content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
    public void setArtistNameName(String artistName) {
        this.artistName = artistName;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
