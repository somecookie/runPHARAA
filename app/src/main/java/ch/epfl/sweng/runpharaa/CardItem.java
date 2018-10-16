package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

public class CardItem {

    private int background;
    private String name;
    private String parentTrackID;

    public CardItem() {

    }

    public CardItem(int background, String name, String parentTrackID) {
        this.background = background;
        this.name = name;
        this.parentTrackID = parentTrackID;
    }

    public int getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }

    public String getParentTrackID() {
        return parentTrackID;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentTrackID(String parentTrackID) {
        this.parentTrackID = parentTrackID;
    }
}
