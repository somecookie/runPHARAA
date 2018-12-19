package ch.epfl.sweng.runpharaa.gui;

public class TrackCardItem extends CardItem {
    private String parentTrackID;

    public TrackCardItem() {
    }

    public TrackCardItem(String name, String parentTrackID, String imageURL) {
        super(name, imageURL);
        this.parentTrackID = parentTrackID;
    }

    /**
     * Get the track ID
     *
     * @return the track ID
     */
    public String getParentTrackID() {
        return parentTrackID;
    }

    /**
     * Set the CardItem track given its unique ID
     *
     * @param parentTrackID the track ID
     */
    public void setParentTrackID(String parentTrackID) {
        this.parentTrackID = parentTrackID;
    }
}
