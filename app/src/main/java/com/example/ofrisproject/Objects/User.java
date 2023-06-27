package com.example.ofrisproject.Objects;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ofrisproject.R;

public class User {

    private String userName;
    private String email;
    private String profileImage;
    private String followers;
    private String following;

    public User(String userName, String email, String profileImage, String followers, String following)
    {
        this.userName=userName;
        this.email=email;
        this.profileImage=profileImage;
        this.followers=followers;
        this.following=following;
    }
    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public boolean addFollowing(String mail)
    {
        if(this.following.isEmpty())//אם המשתמש לא עוקב אחרי אף אחד לראשונה
        {
            this.following =mail;// הוסף את המשתמש השני למחרוזת
            return true;
        }
        if(this.following.contains(mail))// אם המשתמש הנוכחי כבר עוקב אחרי המשתמש השני
        {
            if(this.following.indexOf(mail)==0)// אם הוא הראשון שעקב אחריו
                this.following = this.following.replace(mail,"");// הורד אותו
            else
                this.following = this.following.replace(","+ mail,"");// הורד אותו ואת הפסיק אם הוא לא הראשון
            return false;
        }
        this.following+=","+mail;// בכל מקרה אחר- הוסף אותו לנעקבים
        return true;
    }

    public boolean addFollower(String mail)
    {
        if(this.followers.isEmpty())
        {
            this.followers=mail;
            return true;
        }
        if(this.followers.contains(mail))
        {
            // no comma on first
            if(this.followers.indexOf(mail)==0)
                this.followers = this.followers.replace(mail,"");
            else
                this.followers = this.followers.replace(","+ mail,"");
            return false;
        }
        this.followers+=","+mail;
        return true;
    }

    public int getNumFollowers(){
        String[] userFollowers=followers.split(",");
        return userFollowers.length;
    }

    public int getNumFollowing()
    {
        String[] userFollowings = following.split(",");
        return userFollowings.length;
    }


}
