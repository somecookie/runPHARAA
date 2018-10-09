package ch.epfl.sweng.runpharaa;

public class CardItem {

    private int background;
    private String name;
    private int parentTrackID;

    public CardItem() {

    }

    public CardItem(int background, String name, int parentTrackID) {
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

    public int getParentTrackID() {
        return parentTrackID;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentTrackID(int parentTrackID) {
        this.parentTrackID = parentTrackID;
    }
}
