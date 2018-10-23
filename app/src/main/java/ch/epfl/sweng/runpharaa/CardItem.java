package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

public class CardItem {
    private Bitmap background; //TODO: Keep for Image caching
    private String name;
    private String parentTrackID;
    private String imageURL;

    public CardItem() { }

    public CardItem(/*Bitmap background,*/ String name, String parentTrackID, String imageURL) {
        //this.background = background;
        this.name = name;
        this.parentTrackID = parentTrackID;
        this.imageURL = imageURL;
    }

    public Bitmap getBackground() { return background; }

    public String getName() { return name; }

    public String getParentTrackID() { return parentTrackID; }

    public String getImageURL() { return this.imageURL; }

    public void setBackground(Bitmap background) { this.background = background; }

    public void setName(String name) { this.name = name; }

    public void setParentTrackID(String parentTrackID) { this.parentTrackID = parentTrackID; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
