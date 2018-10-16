package ch.epfl.sweng.runpharaa;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class User {
    private int preferredRadius = 2000;
    private final String name;
    //TODO: put default picture
    private final Uri picture;
    private final ArrayList<Track> list_of_created_tracks;
    private final ArrayList<Track> list_of_pref;
    private LatLng location;
    private final boolean admin;

    private Set<Integer> idTracksLiked;
    private Set<Integer> favorites;
    private final String uId;
    public static User instance;

    //public static User FAKE_USER = new User("Toto", new LatLng(46.518510, 6.563199), 2000);

    public User(String name, int preferredRadius, Uri picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, LatLng location, Boolean admin, String uId) {
        this.preferredRadius = preferredRadius;
        this.name = name;
        this.picture = picture;
        this.list_of_created_tracks = list_of_created_tracks;
        this.list_of_pref = list_of_pref;
        this.location = location;
        this.admin = admin;
        this.uId = uId;
        this.idTracksLiked = new HashSet<>();
        this.favorites = new HashSet<>();
    }

    public User(String name, LatLng location, int preferredRadius) {
        //TODO must be changed later when the user's login and the database are on
        this(name, preferredRadius, null, null, null, location, false, "");
    }

    public int getPreferredRadius() {
        return preferredRadius;
    }

    /**
     * Make an ordered list of all the tracks that are in a RADIUS of 2km.
     *
     * @return ordered list of tracks
     */
    @TargetApi(Build.VERSION_CODES.N)
    public ArrayList<Track> tracksNearMe() {
        ArrayList<Track> nm = new ArrayList<>();
        ArrayList<Track> allTracks = Track.allTracks(); //Todo muste be changed when the database is done

        //filter the tracks that start too far from the location


        for (Track tr : allTracks) {
            System.out.println("------------------------------------- preferedradius ---------- :" + tr.distance(location));
            if (tr.distance(location) <= preferredRadius) {
                nm.add(tr);
            }
        }

        //order them from the nearest to the furthest
        Collections.sort(nm, new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                double d1 = o1.distance(location);
                double d2 = o2.distance(location);
                if (d1 < d2) return -1;
                else if (d1 == d2) return 0;
                else return 1;
            }
        });

        return nm;
    }

    /**
     * Check if the user already liked a particular track
     * @param trackId the track's id
     * @return true if the user already liked the track
     */
    public boolean alreadyLiked(int trackId) {
        return idTracksLiked.contains(trackId);
    }

    /**
     * Add a track id in the set of liked tracks if it is not already there
     * @param trackId the track's id
     */
    public void like(int trackId) {
        if (!alreadyLiked(trackId)) {
            idTracksLiked.add(trackId);
        }
    }

    /**
     * Check if the track is already in user's favorites
     * @param trackId the track's id
     * @return true if the track is in the favorites
     */
    public void unlike(int trackId) {
        idTracksLiked.remove(trackId);
    }

    public Set<Integer> getFavorites() {
        return favorites;
    }

    /**
     *
     * @param trackId
     * @return
     */
    public boolean alreadyInFavorites(int trackId) {
        return favorites.contains(trackId);
    }

    /**
     * Add a track id in the set of favorite tracks if it is not already there
     * @param trackId the track's id
     */
    public void addToFavorites(int trackId) {
        if (!alreadyInFavorites(trackId)) {
            favorites.add(trackId);
        }
    }

    /**
     * Remove a track id from the set of favorite tracks if it is present
     * @param trackId the track's id
     */
    public void removeFromFavorites(int trackId) {
        favorites.remove(trackId);
    }

    /**
     * Getter for the user's location
     *
     * @return location
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * Return the name of the user
     *
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Update the user's location
     *
     * @param newLocation
     */
    public void setLocation(LatLng newLocation) {
        this.location = newLocation;
    }

    public Uri getPicture(){
        return picture;
    }

    public ArrayList<Track> getCreatedTracks(){
        return list_of_created_tracks;
    }

    public ArrayList<Track> getFavoriteTracks(){
        return list_of_pref;
    }

    public static void set(String name, int preferredRadius, Uri picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, LatLng location, Boolean admin, String uId){
        instance = new User(name, preferredRadius, picture, list_of_created_tracks, list_of_pref, location, admin, uId);
    }

}