package ch.epfl.sweng.runpharaa.gui;

import android.graphics.Bitmap;

public class UserCardItem extends CardItem{
    private Bitmap profilePic; //TODO: Keep for Image caching.
    private String parentUserID;
    private int nbCreatedTracks;

    public UserCardItem() { }

    public UserCardItem(/*Bitmap profilePic,*/ String userName, String parentUserID, String imageURL, int nbCreatedTracks) {
        //this.background = background;
        super(userName, imageURL);
        this.parentUserID = parentUserID;
        this.nbCreatedTracks = nbCreatedTracks;
    }

    public Bitmap getBackground() { return profilePic; }

    public String getParentUserID() { return parentUserID; }

    public int getNbCreatedTracks() { return this.nbCreatedTracks; }

    public void setParentUserID(String parentUserID) { this.parentUserID = parentUserID; }

}
