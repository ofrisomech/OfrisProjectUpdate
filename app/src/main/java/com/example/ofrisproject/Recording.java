package com.example.ofrisproject;

public class Recording{

    private String SongID;
    private String artistUser;
    private boolean Isprivate;
    private String url;

    public Recording(String songID, String artistUser, boolean isprivate, String url) {
        SongID = songID;
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

    public Recording(String id, String _artist)
    {
        SongID=id;
        artistUser=_artist;
        Isprivate=true; //מוגדר כערך התחלתי כפרטי
    }

    public Recording(String artistUser) {
        this.artistUser = artistUser;
        this.SongID = "123";
        Isprivate=true; //מוגדר כערך התחלתי כפרטי

    }

    public String getSongID() {
        return SongID;
    }

    public void setSongID(String songID) {
        SongID = songID;
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
