package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.TrackCardItem;
import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.comment.Comment;
import ch.epfl.sweng.runpharaa.utils.Required;
import ch.epfl.sweng.runpharaa.utils.Util;

public class Track {
    //Track identifiers
    private String trackUid;
    private String creatorUid;
    private String creatorName;
    private String imageStorageUri;

    @Exclude
    private TrackCardItem trackCardItem;

    //Track specifics
    private String name;
    private List<CustLatLng> path;
    private CustLatLng startingPoint;
    private List<Comment> comments;

    private TrackProperties properties;

    public Track(){};

    public Track(String trackUid, String creatorUid, String name, List<CustLatLng> path, List<Comment> comment, TrackProperties properties) {

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Track(FirebaseTrackAdapter trackAdapter) {
        Required.nonNull(trackAdapter.getTrackUid(), "Track ID must be non-null.");
        Required.nonNull(trackAdapter.getCreatorId(), "Creator ID must be non-null.");
        Required.nonNull(trackAdapter.getCreatorName(), "Creator name must be non-null.");
        Required.nonNull(trackAdapter.getName(), "Track name must be non-null");
        Required.nonNull(trackAdapter.getPath(), "Path must be non-null.");
        if (trackAdapter.getPath().size() < 2) throw new IllegalArgumentException("A path must have at least 2 points");
        Required.nonNull(trackAdapter.getTrackTypes(), "Set of track types must be non null.");

        Set<TrackType> trackTypes = new HashSet<>();
        for (String t : trackAdapter.getTrackTypes()){
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

        if(comments == null) comments = new ArrayList<>();
        else Collections.sort(comments, (Comment::compareTo));
    }

    public static ArrayList<Track> allTracks;

    static {

        CustLatLng coord0 = new CustLatLng(46.518577, 6.563165); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        CustLatLng coord3 = new CustLatLng(46.518475, 6.561960); //BC
        CustLatLng coord4 = new CustLatLng(46.517563, 6.562350); //Innovation parc
        CustLatLng coord5 = new CustLatLng(46.518447, 6.568238); //Rolex
        CustLatLng coord6 = new CustLatLng(46.523206, 6.564945); //SwissTech
        CustLatLng coord7 = new CustLatLng(46.520566, 6.567820); //Sat
        CustLatLng coord8 = new CustLatLng(46.506279, 6.626111); //Ouchy
        CustLatLng coord9 = new CustLatLng(46.517210, 6.630105); //Gare
        CustLatLng coord10 = new CustLatLng(46.519531, 6.633149);// Saint-Francois
        CustLatLng coord11 = new CustLatLng(46.522638, 6.634971); //Cathédrale
        CustLatLng coord12 = new CustLatLng(46.521412, 6.627383); //Flon

        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track t = new Track("0", "Bobzer", "Cours forest !", Arrays.asList(coord0, coord1, coord2),new ArrayList<>(), p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        allTracks = all;
    }

    public TrackProperties getProperties() { return properties; }

    @Exclude
    public TrackCardItem getTrackCardItem() { return trackCardItem; }

    public CustLatLng getStartingPoint() { return startingPoint; }

    public String getName() { return name; }

    public String getTrackUid() { return trackUid; }

    public String getCreatorUid() { return creatorUid; }

    public String getCreatorName() { return creatorName; }

    public Double getHeightDifference() {return properties.getHeightDifference();}

    public List<CustLatLng> getPath() { return path; }

    public String getImageStorageUri() { return imageStorageUri; }

    public void setTrackCardItem(TrackCardItem trackCardItem) { this.trackCardItem = trackCardItem; }

    public List<Comment> getComments() {
        return comments;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addComment(Comment comment){
        if(!comments.contains(comment)){
            comments.add(comment);
            comments.sort(Comment::compareTo);
        }
    }
}