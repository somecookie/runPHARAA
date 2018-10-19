package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;
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

    //Track specifics
    private String name;
    private List<CustLatLng> path;
    private CustLatLng startingPoint;

    private TrackProperties properties;

    public Track(String trackUid, Bitmap image, String name, List<CustLatLng> path, TrackProperties properties) {

        Required.nonNull(image, "Image must be non-null");
        Required.nonNull(path, "Path must be non-null");
        Required.nonNull(properties, "Properties must be non null");

        this.trackUid = trackUid;
        this.image = image;
        this.name = name;

        if (path.size() < 2)
            throw new IllegalArgumentException("A path must have at least 2 points");
        else this.path = path;

        startingPoint = path.get(0);
        this.properties = properties;

        //this.cardItem = new CardItem(image, name, TID);
    }

    //TODO: either delete this or do it again when the database is on
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
        Track t = new Track("Bob", b, "Cours forest !", Arrays.asList(coord0, coord1, coord2), p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        allTracks = all;
    }

    public TrackProperties getProperties() {
        return properties;
    }

    public CardItem getCardItem() {
        return cardItem;
    }

    public CustLatLng getStartingPoint() {
        return startingPoint;
    }

    public String getName() {
        return name;
    }

    public String getTrackUid() {
        return trackUid;
    }

    public Bitmap getImage() {
        return image;
    }

    //public int getTID() { return TID; }
}
