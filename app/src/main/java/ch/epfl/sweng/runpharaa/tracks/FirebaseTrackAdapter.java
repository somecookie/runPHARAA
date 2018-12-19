package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.properties.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackType;
import ch.epfl.sweng.runpharaa.tracks.properties.comment.Comment;
import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;
import ch.epfl.sweng.runpharaa.utils.Required;

/**
 * Adapter for database track to local {@link Track}
 */
public class FirebaseTrackAdapter {

    private String name;
    private String trackUid;
    private String creatorId;
    private String creatorName;
    private Bitmap image;
    private List<LatLngAdapter> path;
    private String imageStorageUri;
    private List<Comment> comments;

    //Track properties
    private List<String> trackTypes;
    private double length;
    private double heightDifference;
    private int avgDiffTotal;
    private int avgDiffNbr;
    private double avgDurTotal;
    private int avgDurNbr;
    private int likes;
    private int favorites;

    private Boolean isDeleted;

    public FirebaseTrackAdapter(String name, String creatorId, String creatorName, Bitmap image, List<LatLngAdapter> path, TrackProperties properties, List<Comment> comments) {
        Required.nonNull(name, "Track name send to database must be non null");
        Required.nonNull(creatorId, "Track creatorId send to database must be non null");
        Required.nonNull(creatorName, "Track creator name send to database must be non null");
        Required.nonNull(path, "Track path sent to database must be non null");
        Required.nonNull(properties, "Track properties sent to database must must be non null");
        Required.nonNull(comments, "Comments must be non-null");

        this.name = name;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.image = image;
        this.path = path;
        this.comments = comments;

        //Initializing track properties
        List<String> types = new ArrayList<>();
        for (TrackType t : properties.getType()) {
            types.add(t.toString());
        }
        this.trackTypes = types;
        this.length = properties.getLength();
        this.heightDifference = properties.getHeightDifference();
        this.avgDiffTotal = properties.getAvgDifficultyTotal();
        this.avgDiffNbr = properties.getAvgDifficultyNbr();
        this.avgDurTotal = properties.getAvgDurationTotal();
        this.avgDurNbr = properties.getAvgDurationNbr();
        this.likes = 0;
        this.favorites = 0;

        this.isDeleted = false;
    }

    public FirebaseTrackAdapter(Track track, Boolean isDeleted) {
        //Track attributes are assumed non-null
        Required.nonNull(isDeleted, "Specify if the track is deleted or not");

        this.name = track.getName();
        this.creatorId = track.getCreatorUid();
        this.creatorName = track.getCreatorName();
        this.path = track.getPath();
        this.trackUid = track.getTrackUid();

        //Initializing track properties
        List<String> types = new ArrayList<>();
        for (TrackType t : track.getProperties().getType()) {
            types.add(t.toString());
        }
        this.trackTypes = types;
        this.length = track.getProperties().getLength();
        this.heightDifference = track.getProperties().getHeightDifference();
        this.avgDiffTotal = track.getProperties().getAvgDifficultyTotal();
        this.avgDiffNbr = track.getProperties().getAvgDifficultyNbr();
        this.avgDurTotal = track.getProperties().getAvgDurationTotal();
        this.avgDurNbr = track.getProperties().getAvgDurationNbr();
        this.likes = track.getProperties().getLikes();
        this.favorites = track.getProperties().getFavorites();
        this.imageStorageUri = track.getImageStorageUri();
        this.comments = track.getComments();
        this.isDeleted = isDeleted;
    }

    public FirebaseTrackAdapter(String name, String trackUid, String creatorId, String creatorName, List<LatLngAdapter> path,
                                String imageStorageUri, List<String> trackTypes, double length, double heightDifference,
                                int avgDiffTotal, int avgDiffNbr, double avgDurTotal, int avgDurNbr, int likes, int favorites, List<Comment> comments) {
        Required.nonNull(name, "Track name send to database must be non null");
        Required.nonNull(creatorId, "Track creatorId send to database must be non null");
        Required.nonNull(creatorName, "Track creator name send to database must be non null");
        Required.nonNull(path, "Track path sent to database must be non null");
        Required.nonNull(imageStorageUri, "Track imageStorageUri sent to database must be non null");
        Required.nonNull(trackTypes, "Track types sent to database must be non null");
        Required.nonNull(length, "Track length sent to database must be non null");
        Required.nonNull(heightDifference, "Track height difference sent to database must be non null");
        Required.nonNull(avgDiffTotal, "Track average difficulty total sent to database must be non null");
        Required.nonNull(avgDurTotal, "Track average duration total sent to database must be non null");
        Required.nonNull(avgDiffNbr, "Track number of average difficulties sent to database must be non null");
        Required.nonNull(avgDurNbr, "Track number of average durations sent to database must be non null");
        Required.nonNull(likes, "Track likes sent to database must be non null");
        Required.nonNull(favorites, "Track favorites sent to database must be non null");

        this.name = name;
        this.trackUid = trackUid;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.path = path;
        this.imageStorageUri = imageStorageUri;
        this.trackTypes = trackTypes;
        this.length = length;
        this.heightDifference = heightDifference;
        this.avgDiffTotal = avgDiffTotal;
        this.avgDiffNbr = avgDiffNbr;
        this.avgDurTotal = avgDurTotal;
        this.avgDurNbr = avgDurNbr;
        this.likes = likes;
        this.favorites = favorites;
        this.comments = comments;
        this.isDeleted = false;
    }

    //For Firebase
    public FirebaseTrackAdapter() {
    }

    /**
     * Get name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get creatorID.
     *
     * @return String
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * Get creatorName.
     *
     * @return String
     */
    public String getCreatorName() {
        return creatorName;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

    /**
     * Get path.
     *
     * @return List<LatLngAdapter>
     */
    public List<LatLngAdapter> getPath() {
        return path;
    }

    /**
     * Get imageStorageUri.
     *
     * @return String
     */
    public String getImageStorageUri() {
        return imageStorageUri;
    }

    /**
     * Get track id.
     *
     * @return String
     */
    public String getTrackUid() {
        return trackUid;
    }

    /**
     * Get track types.
     *
     * @return List<String>
     */
    public List<String> getTrackTypes() {
        return trackTypes;
    }

    /**
     * Get length.
     *
     * @return double
     */
    public double getLength() {
        return length;
    }

    /**
     * Get height difference.
     *
     * @return double
     */
    public double getHeightDifference() {
        return heightDifference;
    }

    /**
     * Get average total difference.
     *
     * @return int
     */
    public int getAvgDiffTotal() {
        return avgDiffTotal;
    }

    /**
     * Get average difference number.
     *
     * @return int
     */
    public int getAvgDiffNbr() {
        return avgDiffNbr;
    }

    /**
     * Get average total duration.
     *
     * @return double
     */
    public double getAvgDurTotal() {
        return avgDurTotal;
    }

    /**
     * Get average duration number.
     *
     * @return int
     */
    public int getAvgDurNbr() {
        return avgDurNbr;
    }

    /**
     * Get number of likes.
     *
     * @return int
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Get number of favourites.
     *
     * @return
     */
    public int getFavorites() {
        return favorites;
    }

    /**
     * Return true if track is deleted.
     *
     * @return Boolean
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * Set track id.
     *
     * @param trackUid
     */
    public void setTrackUid(String trackUid) {
        this.trackUid = trackUid;
    }

    /**
     * Set image storage uri.
     *
     * @param imageStorageUri
     */
    public void setImageStorageUri(String imageStorageUri) {
        this.imageStorageUri = imageStorageUri;
    }

    /**
     * Get comments.
     *
     * @return List<Comment>
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Set comments.
     *
     * @param comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
