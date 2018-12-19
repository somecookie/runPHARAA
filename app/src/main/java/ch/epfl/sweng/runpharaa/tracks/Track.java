package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.gui.TrackCardItem;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackType;
import ch.epfl.sweng.runpharaa.tracks.properties.comment.Comment;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;
import ch.epfl.sweng.runpharaa.utils.Required;
import ch.epfl.sweng.runpharaa.utils.Util;

public class Track implements Comparable<Track> {
    //Track identifiers
    private String trackUid;
    private String creatorUid;
    private String creatorName;
    private String imageStorageUri;

    @Exclude
    private TrackCardItem trackCardItem;

    //Track specifics
    private String name;
    private List<LatLngAdapter> path;
    private LatLngAdapter startingPoint;
    private List<Comment> comments;

    private TrackProperties properties;

    public Track() {
    }

    public Track(String trackUid, String creatorUid, String name, List<LatLngAdapter> path, List<Comment> comment, TrackProperties properties) {

        Required.nonNull(trackUid, "Track ID must be non-null.");
        Required.nonNull(creatorUid, "Creator ID must be non-null.");
        Required.nonNull(path, "Path must be non-null.");
        Required.nonNull(properties, "Properties must be non null.");

        this.trackUid = trackUid;
        this.creatorUid = creatorUid;
        this.name = name;

        if (path.size() < 2)
            throw new IllegalArgumentException("A path must have at least 2 points");
        else this.path = path;

        startingPoint = path.get(0);
        comments = comment;
        this.properties = properties;
    }

    //For Firebase
    public Track(FirebaseTrackAdapter trackAdapter) {
        Required.nonNull(trackAdapter.getTrackUid(), "Track ID must be non-null.");
        Required.nonNull(trackAdapter.getCreatorId(), "Creator ID must be non-null.");
        Required.nonNull(trackAdapter.getCreatorName(), "Creator name must be non-null.");
        Required.nonNull(trackAdapter.getName(), "Track name must be non-null");
        Required.nonNull(trackAdapter.getPath(), "Path must be non-null.");
        if (trackAdapter.getPath().size() < 2)
            throw new IllegalArgumentException("A path must have at least 2 points");
        Required.nonNull(trackAdapter.getTrackTypes(), "Set of track types must be non null.");

        Set<TrackType> trackTypes = new HashSet<>();
        for (String t : trackAdapter.getTrackTypes()) {
            trackTypes.add(TrackType.valueOf(t));
        }
        properties = new TrackProperties(trackAdapter.getLikes(), trackAdapter.getFavorites(), trackAdapter.getLength(),
                trackAdapter.getHeightDifference(), trackAdapter.getAvgDurTotal(), trackAdapter.getAvgDurNbr(),
                trackAdapter.getAvgDiffTotal(), trackAdapter.getAvgDiffNbr(), trackTypes);

        this.trackUid = trackAdapter.getTrackUid();
        this.creatorUid = trackAdapter.getCreatorId();
        this.creatorName = trackAdapter.getCreatorName();
        this.name = trackAdapter.getName();
        this.path = trackAdapter.getPath();
        this.startingPoint = path.get(0);
        this.imageStorageUri = trackAdapter.getImageStorageUri();
        this.comments = trackAdapter.getComments();

        if (comments == null) comments = new ArrayList<>();
        else Collections.sort(comments, (Comment::compareTo));
    }

    public static ArrayList<Track> allTracks;

    static {

        LatLngAdapter coord0 = new LatLngAdapter(46.518577, 6.563165); //inm
        LatLngAdapter coord1 = new LatLngAdapter(46.522735, 6.579772); //Banane
        LatLngAdapter coord2 = new LatLngAdapter(46.519380, 6.580669); //centre sportif
        LatLngAdapter coord3 = new LatLngAdapter(46.518475, 6.561960); //BC
        LatLngAdapter coord4 = new LatLngAdapter(46.517563, 6.562350); //Innovation parc
        LatLngAdapter coord5 = new LatLngAdapter(46.518447, 6.568238); //Rolex
        LatLngAdapter coord6 = new LatLngAdapter(46.523206, 6.564945); //SwissTech
        LatLngAdapter coord7 = new LatLngAdapter(46.520566, 6.567820); //Sat
        LatLngAdapter coord8 = new LatLngAdapter(46.506279, 6.626111); //Ouchy
        LatLngAdapter coord9 = new LatLngAdapter(46.517210, 6.630105); //Gare
        LatLngAdapter coord10 = new LatLngAdapter(46.519531, 6.633149);// Saint-Francois
        LatLngAdapter coord11 = new LatLngAdapter(46.522638, 6.634971); //Cathédrale
        LatLngAdapter coord12 = new LatLngAdapter(46.521412, 6.627383); //Flon

        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track t = new Track("0", "Bobzer", "Cours forest !", Arrays.asList(coord0, coord1, coord2), new ArrayList<>(), p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        allTracks = all;
    }

    /**
     * Get the properties of the Track
     *
     * @return the Track's properties
     */
    public TrackProperties getProperties() {
        return properties;
    }

    /**
     * Get the Track's CardItem
     *
     * @return the Track's CardItem
     */
    @Exclude
    public TrackCardItem getTrackCardItem() {
        return trackCardItem;
    }

    /**
     * Get the Track's starting point
     *
     * @return a LatLngAdapter corresponding to the Track's starting point
     */
    public LatLngAdapter getStartingPoint() {
        return startingPoint;
    }

    /**
     * Get the Track's name
     *
     * @return the Track's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Track's unique ID
     *
     * @return the Track's unique ID
     */
    public String getTrackUid() {
        return trackUid;
    }

    /**
     * Get the Track's creator unique ID
     *
     * @return the unique ID of the Track's creator
     */
    public String getCreatorUid() {
        return creatorUid;
    }

    /**
     * Get the Track's creator name
     *
     * @return the Track's creator name
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Get the Track's path
     *
     * @return a List<LatLngAdapter> containing the Track's path
     */
    public List<LatLngAdapter> getPath() {
        return path;
    }

    /**
     * Get the Track's image URI
     *
     * @return the Track's image URI
     */
    public String getImageStorageUri() {
        return imageStorageUri;
    }

    /**
     * Set this Track's CardItem with the one given in argument
     *
     * @param trackCardItem the CardItem we want to set for the Track
     */
    public void setTrackCardItem(TrackCardItem trackCardItem) {
        this.trackCardItem = trackCardItem;
    }

    /**
     * Get this Track's oomments
     *
     * @return a List<Comment>
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Add the given comment to this Track's comments
     *
     * @param comment the comment we want to add to the Track
     */
    public void addComment(Comment comment) {
        if (!comments.contains(comment)) {
            comments.add(comment);
            Collections.sort(comments, (Comment::compareTo));
        }
    }

    @Override
    public int compareTo(@NonNull Track o) {
        if (User.instance.getLocation() == null)
            return 0;
        double d1 = this.getStartingPoint().distance(LatLngAdapter.LatLngToCustLatLng(User.instance.getLocation()));
        double d2 = o.getStartingPoint().distance(LatLngAdapter.LatLngToCustLatLng(User.instance.getLocation()));
        return Double.compare(d1, d2);
    }
}