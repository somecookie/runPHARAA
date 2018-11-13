package ch.epfl.sweng.runpharaa.user;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.utils.Required;

public final class User {
    @Exclude
    public static User instance;
    @Exclude
    private int preferredRadius = 2000;
    @Exclude
    private LatLng location;

    private String name;
    private String picture;
    private String uid;
    private List<String> createdTracks;
    private List<String> favoriteTracks;
    private List<String> likedTracks;
    private List<User> followedUsers;

    public User(){}

    public User(String name, int preferredRadius, Uri picture, LatLng location, String uid) {
        Required.nonNull(name, "The name of an user cannot be null");
        Required.nonNull(location, "The location of an user cannot be null");
        Required.nonNull(uid, "The uid of an user cannot be null");
        Required.nonNull(picture, "The picture of an user cannot be null");

        this.preferredRadius = preferredRadius;
        this.name = name;
        this.picture = picture.toString();
        this.createdTracks = new ArrayList<>();
        this.favoriteTracks = new ArrayList<>();
        this.likedTracks = new ArrayList<>();
        this.location = location;
        this.uid = uid;
    }

    public static void set(String name, float preferredRadius, Uri picture, LatLng location, String uId) {
        instance = new User(name, (int) (preferredRadius * 1000), picture, location, uId);
    }

    @Exclude
    public int getPreferredRadius() {
        return preferredRadius;
    }

    @Exclude
    public void setPreferredRadius(float newRadius) {
        this.preferredRadius = (int) (newRadius * 1000);
    }


    public boolean alreadyFollowed(User u) {
        return followedUsers.contains(u);
    }

    public void addFollower(User u) {
        if (!alreadyFollowed(u)) followedUsers.add(u);
    }

    /**
     * Check if the user already liked a particular track
     *
     * @param trackId the track's id
     * @return true if the user already liked the track
     */
    public boolean alreadyLiked(String trackId) {
        return likedTracks.contains(trackId);
    }

    /**
     * Add a track id in the set of liked tracks if it is not already there
     *
     * @param trackId the track's id
     */
    public void like(String trackId) {
        if (!alreadyLiked(trackId)) likedTracks.add(trackId);
    }

    /**
     * Check if the track is already in user's favorites
     *
     * @param trackId the track's id
     * @return true if the track is in the favorites
     */
    public void unlike(String trackId) {
        likedTracks.remove(trackId);
    }

    /**
     * Check if Track is already in favourites.
     *
     * @param trackId
     * @return
     */
    public boolean alreadyInFavorites(String trackId) {
        return favoriteTracks.contains(trackId);
    }

    /**
     * Add a Track id in the set of favourite tracks if it is not already there.
     *
     * @param trackId the track's id
     */
    public void addToFavorites(String trackId) {
        if (!alreadyInFavorites(trackId)) favoriteTracks.add(trackId);

    }

    /**
     * Add a Track id in the set of created tracks.
     *
     * @param trackId
     */
    public void addToCreatedTracks(String trackId) {
        if (!createdTracks.contains(trackId)) createdTracks.add(trackId);
    }

    /**
     * Remove a track id from the set of favorite tracks if it is present.
     *
     * @param trackId the track's id
     */
    public void removeFromFavorites(String trackId) {
        favoriteTracks.remove(trackId);
    }

    /**
     * Getter for the user's location
     *
     * @return location
     */
    @Exclude
    public LatLng getLocation() {
        return location;
    }

    /**
     * Update the user's location.
     *
     * @param newLocation
     */
    @Exclude
    public void setLocation(LatLng newLocation) {
        this.location = newLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getCreatedTracks() {
        return createdTracks;
    }

    public void setCreatedTracks(List<String> createdTracks) {
        this.createdTracks = createdTracks;
    }

    public List<String> getFavoriteTracks() {
        return favoriteTracks;
    }

    public void setFavoriteTracks(List<String> favoriteTracks) {
        this.favoriteTracks = favoriteTracks;
    }

    public List<String> getLikedTracks() {
        return likedTracks;
    }

    public void setLikedTracks(List<String> likedTracks) {
        this.likedTracks = likedTracks;
    }

    public List<User> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<User> followedUsers) {
        this.followedUsers = followedUsers;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        else {
            User that = (User) obj;
            return this.uid.equals(that.uid);
        }
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

}