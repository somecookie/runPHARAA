package ch.epfl.sweng.runpharaa.user;

import java.util.List;

import ch.epfl.sweng.runpharaa.utils.Required;

public class FirebaseUserAdapter {

    private String uId;
    private List<String> createdTracks;
    private List<String> favoritesTracks;
    private List<String> likedTracks;

    public FirebaseUserAdapter(){}

    public FirebaseUserAdapter(User user){
        Required.nonNull(user,"User we want to send to the database must be non-null");
        uId = user.getID();
        createdTracks = user.getCreatedTracksKeys();
        favoritesTracks = user.getFavoriteTracks();
        likedTracks = user.getLikedTracksKeys();

    }

    public String getuId() {
        return uId;
    }

    public List<String> getCreatedTracks() {
        return createdTracks;
    }

    public List<String> getFavoritesTracks() {
        return favoritesTracks;
    }

    public List<String> getLikedTracks() {
        return likedTracks;
    }
}
