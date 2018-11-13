package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.utils.Required;

public class FirebaseTrackAdapter {

    private String name;
    private String trackUid;
    private String creatorId;
    private Bitmap image;
    private List<CustLatLng> path;
    private String imageStorageUri;

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

    public FirebaseTrackAdapter(String name, String creatorId, Bitmap image, List<CustLatLng> path, TrackProperties properties){
        Required.nonNull(name, "Track name send to database must be non null");
        Required.nonNull(creatorId, "Track creatorId send to database must be non null");
        Required.nonNull(path, "Track path sent to database must be non null");
        Required.nonNull(properties, "Track properties sent to database must must be non null");

        this.name = name;
        this.creatorId = creatorId;
        this.image = image;
        this.path = path;

        //Initializing track properties
        List<String> types = new ArrayList<>();
        for(TrackType t : properties.getType()){
            types.add(t.toString());
        }
        this.trackTypes = types;
        this.length= properties.getLength();
        this.heightDifference = properties.getHeightDifference();
        this.avgDiffTotal = properties.getAvgDifficultyTotal();
        this.avgDiffNbr = properties.getAvgDifficultyNbr();
        this.avgDurTotal = properties.getAvgDurationTotal();
        this.avgDurNbr = properties.getAvgDurationNbr();
        this.likes = 0;
        this.favorites = 0;
    }

    public FirebaseTrackAdapter(Track track){
        Required.nonNull(track.getName(), "Track name send to database must be non null");
        Required.nonNull(track.getCreatorUid(), "Track creatorId send to database must be non null");
        Required.nonNull(track.getPath(), "Track path sent to database must be non null");
        Required.nonNull(track.getProperties(), "Track properties sent to database must must be non null");

        this.name = track.getName();
        this.creatorId = track.getCreatorUid();
        this.path = track.getPath();
        this.trackUid = track.getTrackUid();

        //Initializing track properties
        List<String> types = new ArrayList<>();
        for(TrackType t : track.getProperties().getType()){
            types.add(t.toString());
        }
        this.trackTypes = types;
        this.length= track.getProperties().getLength();
        this.heightDifference = track.getProperties().getHeightDifference();
        this.avgDiffTotal = track.getProperties().getAvgDifficultyTotal();
        this.avgDiffNbr = track.getProperties().getAvgDifficultyNbr();
        this.avgDurTotal = track.getProperties().getAvgDurationTotal();
        this.avgDurNbr = track.getProperties().getAvgDurationNbr();
        this.likes = track.getProperties().getLikes();
        this.favorites = track.getProperties().getFavorites();
        this.imageStorageUri = track.getImageStorageUri();
    }

    public FirebaseTrackAdapter(String name, String trackUid, String creatorId, List<CustLatLng> path,
                                String imageStorageUri, List<String> trackTypes, double length, double heightDifference,
                                int avgDiffTotal, int avgDiffNbr, double avgDurTotal, int avgDurNbr, int likes, int favorites) {
        Required.nonNull(name, "Track name send to database must be non null");
        Required.nonNull(creatorId, "Track creatorId send to database must be non null");
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
    }

    //For Firebase
    public FirebaseTrackAdapter(){}

    public String getName() {
        return name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

    public List<CustLatLng> getPath() {
        return path;
    }

    public String getImageStorageUri() {
        return imageStorageUri;
    }

    public String getTrackUid() {
        return trackUid;
    }

    public List<String> getTrackTypes() {
        return trackTypes;
    }

    public double getLength() {
        return length;
    }

    public double getHeightDifference() {
        return heightDifference;
    }

    public int getAvgDiffTotal() {
        return avgDiffTotal;
    }

    public int getAvgDiffNbr() {
        return avgDiffNbr;
    }

    public double getAvgDurTotal() {
        return avgDurTotal;
    }

    public int getAvgDurNbr() {
        return avgDurNbr;
    }

    public int getLikes() {
        return likes;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setTrackUid(String trackUid) {
        this.trackUid = trackUid;
    }

    public void setImageStorageUri(String imageStorageUri) {
        this.imageStorageUri = imageStorageUri;
    }
}
