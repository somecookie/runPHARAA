package ch.epfl.sweng.runpharaa.gui;

public class UserCardItem extends CardItem {
    private String parentUserID;
    private int nbCreatedTracks;

    public UserCardItem() { }

    public UserCardItem(String userName, String parentUserID, String imageURL, int nbCreatedTracks) {
        super(userName, imageURL);
        this.parentUserID = parentUserID;
        this.nbCreatedTracks = nbCreatedTracks;
    }

    public String getParentUserID() { return parentUserID; }

    public int getNbCreatedTracks() { return this.nbCreatedTracks; }

    public void setParentUserID(String parentUserID) { this.parentUserID = parentUserID; }

}
