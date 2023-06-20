package com.example.ofrisproject.Objects;

public class Song {
    private String songName;
    private String genre;
    private String artistName;
    private String songId;

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

    public String getSongId(){ return songId;}

    public void setSongId(String songId){ this.songId=songId;}

}
