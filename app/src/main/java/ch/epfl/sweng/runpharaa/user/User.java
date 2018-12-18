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
    private String notificationKey;
    private List<String> createdTracks;
    private List<String> favoriteTracks;
    private List<String> feedbackTracks;
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
        this.feedbackTracks = new ArrayList<>();
        this.location = location;
        this.uid = uid;
    }

    /**
     * General setter for various field
     * @param name a String for the User's name
     * @param preferredRadius a float for the User's preferred radius
     * @param picture an URI for the User's picture
     * @param location a LatLng containing the User's location
     * @param uId a String for the User's unique ID
     */
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

    /**
     * Deserialize a String back to a User
     *
     * @param s a String containing a serialized User
     * @return a deserialized User
     */
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
     * @param trackId the track's ID
     */
    public void like(String trackId) {
        if (!alreadyLiked(trackId)) likedTracks.add(trackId);
    }

    /**
     * Check if the track is already in user's favorites
     *
     * @param trackId the track's ID
     */
    public void unlike(String trackId) {
        likedTracks.remove(trackId);
    }

    /**
     * Check if Track is already in favourites
     *
     * @param trackId the track's ID
     * @return true if the given Track is in the favourites
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
     * @param trackId the track's id
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
     * @param u a User
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
     * @param u a User
     */
    public void addToFollowed(User u) {
        String serializedUser = u.serialize();
        if (!alreadyInFollowed(u))
            followedUsers.add(serializedUser);
    }

    /**
     * Remove a User id from the set of followed users
     *
     * @param u a User
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
     * Add a new feedback for a track
     *
     * @param trackID a Track ID
     */
    public void addNewFeedBack(String trackID){
        if(!feedbackTracks.contains(trackID)){
            feedbackTracks.add(trackID);
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
     * @param newLocation a LatLng representing the new location
     */
    @Exclude
    public void setLocation(LatLng newLocation) {
        this.location = newLocation;
    }

    /**
     * Get the User's name
     *
     * @return the User's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set a User's name
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the User's picture
     *
     * @return the User's picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Set the User's picture
     *
     * @param picture the new picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Get the User's unique ID
     *
     * @return the User's unique ID
     */
    public String getUid() {
        return uid;
    }

    /**
     * Set the User's unique ID
     *
     * @param uid the new unique ID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Get the User's notification key
     *
     * @return the User's notification key
     */
    public String getNotificationKey(){
        return notificationKey;
    }

    /**
     * Set a notification key
     *
     * @param key the new notification key
     */
    public void setNotificationKey(String key){
        notificationKey = key;
    }

    /**
     * Get the User's created tracks
     *
     * @return a list containing the created tracks ID
     */
    public List<String> getCreatedTracks() {
        return createdTracks;
    }

    /**
     * Set the User's created tracks
     *
     * @param createdTracks a list of tracks
     */
    public void setCreatedTracks(List<String> createdTracks) {
        this.createdTracks = createdTracks;
    }

    /**
     * Get the User's favorite tracks
     *
     * @return a list containing the User's favorite tracks
     */
    public List<String> getFavoriteTracks() {
        return favoriteTracks;
    }

    /**
     * Set the User's favorite tracks
     *
     * @param favoriteTracks a list of tracks
     */
    public void setFavoriteTracks(List<String> favoriteTracks) {
        this.favoriteTracks = favoriteTracks;
    }

    /**
     * Get the User's liked tracks
     *
     * @return a list containing the User's liked tracks
     */
    public List<String> getLikedTracks() {
        return likedTracks;
    }

    /**
     * Set the User's liked tracks
     *
     * @param likedTracks a list of tracks
     */
    public void setLikedTracks(List<String> likedTracks) {
        this.likedTracks = likedTracks;
    }

    /**
     * Get the User's followed users
     *
     * @return a list containing the User's followed users
     */
    public List<String> getFollowedUsers() {
        return followedUsers;
    }

    /**
     * Set the User's followed users
     *
     * @param followedUsers a list of users
     */
    public void setFollowedUsers(List<String> followedUsers) {
        this.followedUsers = followedUsers;
    }

    /**
     * Get the feedback
     *
     * @return a list of feedback
     */
    public List<String> getFeedbackTracks() {
        return feedbackTracks;
    }

    /**
     * Set the feedback
     *
     * @param feedbackTracks a list of feedback
     */
    public void setFeedbackTracks(List<String> feedbackTracks) {
        this.feedbackTracks = feedbackTracks;
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

    /**
     * Serialize a User into a unique String
     *
     * @return a String representing this User
     */
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

    /**
     * Get the User's CardItem
     *
     * @return the User's CardItem
     */
    @Exclude
    public UserCardItem getUserCardItem() {
        return userCardItem;
    }

    /**
     * Set the User's CardItem
     *
     * @param userCardItem a UserCardItem
     */
    public void setUserCardItem(UserCardItem userCardItem) {
        this.userCardItem = userCardItem;
    }

}