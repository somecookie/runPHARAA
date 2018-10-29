package ch.epfl.sweng.runpharaa.user;

import com.google.firebase.database.Exclude;

import java.util.List;

import ch.epfl.sweng.runpharaa.utils.Required;

/**
 * Adapter between the User class and the Firebase database
 */
public class FirebaseUserAdapter {
    @Exclude
    private User user;
    private String uId;
    private List<String> createdTracks;
    private List<String> favoriteTracks;
    private List<String> likedTracks;

    public FirebaseUserAdapter(){}

    public FirebaseUserAdapter(User user){
        Required.nonNull(user,"User we want to send to the database must be non-null");
        this.user= user;
        uId = user.getID();
        createdTracks = user.getCreatedTracks();
        favoriteTracks = user.getFavoriteTracks();
        likedTracks = user.getLikedTracks();

    }

    public String getuId() {
        return uId;
    }

    public List<String> getCreatedTracks() {
        return createdTracks;
    }

    public List<String> getFavoriteTracks() {
        return favoriteTracks;
    }

    public List<String> getLikedTracks() {
        return likedTracks;
    }
}
