package com.example.ofrisproject.Objects;

public class Comment {
    private String userName;
    private String content;
    private String Useremail;
    private String urlRec;

    public Comment(String userName, String content,String Useremail, String urlRec ){
        this.userName=userName;
        this.content=content;
        this.Useremail=Useremail;
        this.urlRec=urlRec;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){this.userName=userName;}

    public String getContent(){
        return content;
    }

    public void setContent(String content){this.content=content;}

    public String getUseremail(){return Useremail;}

    public void setUseremail(String useremail){this.Useremail=useremail;}
    public String getUrlRec(){
        return urlRec;
    }

    public void setUrlRec(String urlRec){this.urlRec=urlRec;}


}
