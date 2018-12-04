package ch.epfl.sweng.runpharaa.gui;

public class TrackCardItem extends CardItem{
    private String parentTrackID;

    public TrackCardItem() { }

    public TrackCardItem( String name, String parentTrackID, String imageURL) {
        super(name, imageURL);
        this.parentTrackID = parentTrackID;
    }

    public String getParentTrackID() { return parentTrackID; }

    public void setParentTrackID(String parentTrackID) { this.parentTrackID = parentTrackID; }
}
