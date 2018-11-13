package ch.epfl.sweng.runpharaa.user;

import android.graphics.Bitmap;

public class UserCardItem {
    private Bitmap profilePic; //TODO: Keep for Image caching.
    private String name;
    private String parentUserID;
    private String imageURL;

    public UserCardItem() { }

    public UserCardItem(/*Bitmap profilePic,*/ String name, String parentUserID, String imageURL) {
        //this.background = background;
        this.name = name;
        this.parentUserID = parentUserID;
        this.imageURL = imageURL;
    }

    public Bitmap getBackground() { return profilePic; }

    public String getName() { return name; }

    public String getParentUserID() { return parentUserID; }

    public String getImageURL() { return this.imageURL; }

    public void setProfilePic(Bitmap background) { this.profilePic = background; }

    public void setName(String name) { this.name = name; }

    public void setParentUserID(String parentTrackID) { this.parentUserID = parentTrackID; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
