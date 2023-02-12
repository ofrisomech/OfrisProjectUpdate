package com.example.ofrisproject;

public class Song {
    private String songName;
    private String genre;// מתוך סט אפשרויות
    private String artistName;

    public Song(String _name, String _genre, String _artist)
    {
        songName=_name;
        genre=_genre;
        artistName=_artist;
    }

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
