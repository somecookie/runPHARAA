package ch.epfl.sweng.runpharaa.user;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.gui.UserCardItem;
import ch.epfl.sweng.runpharaa.utils.Required;

public final class User implements Serializable {
    @Exclude
    public transient static User instance;
    @Exclude
    private transient int preferredRadius = 2000;
    @Exclude
    private transient LatLng location;
    @Exclude
    private UserCardItem userCardItem;
    @Exclude
    private StreakManager streakManager;

    private String name;
    private String picture;
    private String uid;
    private List<String> createdTracks;
    private List<String> favoriteTracks;
    private transient List<String> likedTracks;
    private transient List<String> followedUsers;

    public User() {
    }

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
        this.followedUsers = new ArrayList<>();
        this.location = location;
        this.uid = uid;
    }

    public static void set(String name, float preferredRadius, Uri picture, LatLng location, String uId) {
        instance = new User(name, (int) (preferredRadius * 1000), picture, location, uId);
    }

    public static void setLoadedData(User otherUser) {
        instance.setCreatedTracks(otherUser.createdTracks);
        instance.setFavoriteTracks(otherUser.favoriteTracks);
        instance.setLikedTracks(otherUser.likedTracks);
        instance.setFollowedUsers(otherUser.followedUsers);
    }

    public static void setStreakManager(StreakManager streakManager) {
        Required.nonNull(streakManager, "The streak manager can't be null");
        User.instance.streakManager = streakManager;
    }

    public static StreakManager getStreakManager() {
        return User.instance.streakManager;
    }

    public static void set(User u) {
        instance = u;
    }

    public static User deserialize(String s) {
        try {
            byte b[] = Base64.decode(s.getBytes(), 0);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (User) objectInputStream.readObject();
        } catch (Exception e) {
            Log.d("Deserialization Error", e.toString());
            return null;
        }
    }

    @Exclude
    public int getPreferredRadius() {
        return preferredRadius;
    }

    @Exclude
    public void setPreferredRadius(float newRadius) {
        this.preferredRadius = (int) (newRadius * 1000);
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
     * Check if a User id is in the set of followed users
     *
     * @param u
     */
    public boolean alreadyInFollowed(User u) {
        for (String serializedUser : followedUsers) {
            User user = deserialize(serializedUser);
            if (user != null && user.getUid().equals(u.getUid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a User id in the set of followed users
     *
     * @param u
     */
    public void addToFollowed(User u) {
        String serializedUser = u.serialize();
        if (!alreadyInFollowed(u))
            followedUsers.add(serializedUser);
    }

    /**
     * Remove a User id from the set of followed users
     *
     * @param u
     */
    public void removeFromFollowed(User u) {
        for (String serializedUser : followedUsers) {
            User deserializedUser = deserialize(serializedUser);
            if (deserializedUser != null && deserializedUser.getUid().equals(u.getUid())) {
                followedUsers.remove(serializedUser);
                return;
            }
        }
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

    public List<String> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<String> followedUsers) {
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

    private String serialize() {
        String serialized = "";

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            serialized = new String(Base64.encode(byteArrayOutputStream.toByteArray(), 0));
        } catch (Exception e) {
            Log.d("Serialization Error", e.toString());
        }

        return serialized;
    }

    @Exclude
    public UserCardItem getUserCardItem() {
        return userCardItem;
    }

    public void setUserCardItem(UserCardItem userCardItem) {
        this.userCardItem = userCardItem;
    }

}