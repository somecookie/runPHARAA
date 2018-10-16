package ch.epfl.sweng.runpharaa;

public class CardItem {

    private int background;
    private String name;
    private String parentTrackID;
    private String imageURL;

    public CardItem() {

    }

    public CardItem(int background, String name, String parentTrackID, String imageURL) {
        this.background = background;
        this.name = name;
        this.parentTrackID = parentTrackID;
        this.imageURL = imageURL;
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

    public String getImageURL() { return this.imageURL; }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentTrackID(String parentTrackID) {
        this.parentTrackID = parentTrackID;
    }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
