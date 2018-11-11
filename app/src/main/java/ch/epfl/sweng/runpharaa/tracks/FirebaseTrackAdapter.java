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
