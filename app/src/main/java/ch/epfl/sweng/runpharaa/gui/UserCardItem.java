package ch.epfl.sweng.runpharaa.gui;

public class UserCardItem extends CardItem {
    private String parentUserID;
    private int nbCreatedTracks;

    public UserCardItem() {
    }

    public UserCardItem(String userName, String parentUserID, String imageURL, int nbCreatedTracks) {
        super(userName, imageURL);
        this.parentUserID = parentUserID;
        this.nbCreatedTracks = nbCreatedTracks;
    }

    /**
     * Get the User's ID
     *
     * @return the User's ID
     */
    public String getParentUserID() {
        return parentUserID;
    }

    /**
     * Get the User's number of created tracks
     *
     * @return the User's number of created tracks
     */
    public int getNbCreatedTracks() {
        return this.nbCreatedTracks;
    }

    /**
     * Set the CardItem User given its ID
     *
     * @param parentUserID the User's unique ID
     */
    public void setParentUserID(String parentUserID) {
        this.parentUserID = parentUserID;
    }

}
