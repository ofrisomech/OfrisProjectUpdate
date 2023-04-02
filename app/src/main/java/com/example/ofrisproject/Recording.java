package com.example.ofrisproject;

public class Recording{

    private String SongName;
    private String artistUser;
    private boolean Isprivate;
    private String url;

    public Recording(String songname, String artistUser, boolean isprivate, String url) {
        SongName = songname;
        this.artistUser = artistUser;
        Isprivate = isprivate;
        this.url = url;
    }

    public Recording() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Recording(String name, String _artist)
    {
        SongName=name;
        artistUser=_artist;
        Isprivate=true; //מוגדר כערך התחלתי כפרטי
    }


    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songID) {
        SongName = songID;
    }

    public String getArtistUser() {
        return artistUser;
    }

    public void setArtistUser(String artistUser) {
        this.artistUser = artistUser;
    }

    public boolean isIsprivate() {
        return Isprivate;
    }

    public void setIsprivate(boolean isprivate) {
        Isprivate = isprivate;
    }

    public void SetPrivate(boolean _private){
        Isprivate=_private;
    }

}
