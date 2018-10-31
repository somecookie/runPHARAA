package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.CardItem;
import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.utils.Required;
import ch.epfl.sweng.runpharaa.utils.Util;

public class Track {
    //Track identifiers
    private String trackUid;
    private String creatorUid;
    private String imageStorageUri;
    @Exclude
    private Bitmap image;
    @Exclude
    private  CardItem cardItem;
    private static int COUNTER_ID = 0;

    //Track specifics
    private String name;
    private List<CustLatLng> path;
    private CustLatLng startingPoint;

    private TrackProperties properties;

    public Track(String name, String creatorUid, Bitmap image, List<CustLatLng> path, TrackProperties properties){

        Required.nonNull(creatorUid, "Creator ID must be non-null.");
        //Required.nonNull(image, "Image must be non-null.");
        Required.nonNull(path, "Path must be non-null.");
        Required.nonNull(properties, "Properties must be non null.");

        this.name = name;
        this.creatorUid = creatorUid;
        this.image = image;
        this.path = path;
        startingPoint = path.get(0);
        this.properties = properties;
    }

    public Track(String trackUid, String creatorUid, Bitmap image, String name, List<CustLatLng> path, TrackProperties properties) {

        Required.nonNull(trackUid, "Track ID must be non-null.");
        Required.nonNull(creatorUid, "Creator ID must be non-null.");
        //Required.nonNull(image, "Image must be non-null.");
        Required.nonNull(path, "Path must be non-null.");
        Required.nonNull(properties, "Properties must be non null.");

        this.trackUid = trackUid;
        this.creatorUid = creatorUid;
        this.image = image;
        this.name = name;

        if (path.size() < 2)
            throw new IllegalArgumentException("A path must have at least 2 points");
        else this.path = path;

        startingPoint = path.get(0);
        this.properties = properties;
    }

    //For Firebase
    public Track() { }

    public TrackProperties getProperties() { return properties; }

    @Exclude
    public CardItem getCardItem() { return cardItem; }

    public CustLatLng getStartingPoint() { return startingPoint; }

    public String getName() { return name; }

    public String getTrackUid() { return trackUid; }

    @Exclude
    public String getCreatorUid() { return creatorUid; }

    public List<CustLatLng> getPath() { return path; }

    @Exclude
    public Bitmap getImage() { return image; }

    public String getImageStorageUri() { return imageStorageUri; }

    public void setImageStorageUri(String imageStorageUri) { this.imageStorageUri = imageStorageUri; }

    public void setCardItem(CardItem cardItem) { this.cardItem = cardItem; }

    public void setTrackUid(String trackUid) { this.trackUid = trackUid; }
}