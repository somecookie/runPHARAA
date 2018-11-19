package ch.epfl.sweng.runpharaa;

public class TrackCardItem {
    private String name;
    private String parentTrackID;
    private String imageURL;

    public TrackCardItem() { }

    public TrackCardItem( String name, String parentTrackID, String imageURL) {
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
