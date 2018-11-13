package ch.epfl.sweng.runpharaa.user;

import android.graphics.Bitmap;

public class UserCardItem {
    private Bitmap profilePic; //TODO: Keep for Image caching.
    private String name;
    private String parentUserID;
    private String imageURL;
    private int nbCreatedTracks;

    public UserCardItem() { }

    public UserCardItem(/*Bitmap profilePic,*/ String userName, String parentUserID, String imageURL, int nbCreatedTracks) {
        //this.background = background;
        this.name = userName;
        this.parentUserID = parentUserID;
        this.imageURL = imageURL;
        this.nbCreatedTracks = nbCreatedTracks;
    }

    public Bitmap getBackground() { return profilePic; }

    public String getName() { return name; }

    public String getParentUserID() { return parentUserID; }

    public String getImageURL() { return this.imageURL; }

    public int getNbCreatedTracks() {return this.nbCreatedTracks; }

    public void setProfilePic(Bitmap background) { this.profilePic = background; }

    public void setName(String name) { this.name = name; }

    public void setParentUserID(String parentUserID) { this.parentUserID = parentUserID; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public void setNbCreatedTracks(int nbCreatedTracks) { this.nbCreatedTracks = nbCreatedTracks; }
}
