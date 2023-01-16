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

    public String GetArtistName(){return artistName;}
    public String GetGenre(){return genre; }
    public String GetSongName(){return songName;}


}
