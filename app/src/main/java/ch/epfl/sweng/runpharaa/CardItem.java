package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

public class CardItem {
    private String name;
    private String parentTrackID;
    private String imageURL;

    public CardItem() { }

    public CardItem(String name, String parentTrackID, String imageURL) {
        this.name = name;
        this.parentTrackID = parentTrackID;
        this.imageURL = imageURL;
    }

    public String getName() { return name; }

    public String getParentTrackID() { return parentTrackID; }

    public String getImageURL() { return this.imageURL; }

    public void setName(String name) { this.name = name; }

    public void setParentTrackID(String parentTrackID) { this.parentTrackID = parentTrackID; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
