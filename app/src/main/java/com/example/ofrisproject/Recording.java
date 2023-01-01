package com.example.ofrisproject;

public class Recording{

    private String SongID;
    private String artistUser;
    private boolean Isprivate;

    public Recording(String id, String _artist)
    {
        SongID=id;
        artistUser=_artist;
        Isprivate=true; //מוגדר כערך התחלתי כפרטי
    }

    public void SetPrivate(boolean _private){
        Isprivate=_private;
    }

}
