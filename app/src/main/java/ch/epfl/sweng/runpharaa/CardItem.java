package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

public class CardItem {

    private Bitmap background;
    private String name;
    private int parentTrackID;

    public CardItem() {

    }

    public CardItem(Bitmap background, String name, int parentTrackID) {
        this.background = background;
        this.name = name;
        this.parentTrackID = parentTrackID;
    }

    public Bitmap getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }

    public int getParentTrackID() {
        return parentTrackID;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentTrackID(int parentTrackID) {
        this.parentTrackID = parentTrackID;
    }
}
