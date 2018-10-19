package ch.epfl.sweng.runpharaa.tracks;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.CardItem;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.utils.Required;
import ch.epfl.sweng.runpharaa.utils.Util;

public class Track {
    //Track identifiers
    private static int COUNTER_ID = 0;
    private int TID;
    private final String uid;
    private final Bitmap image;
    private final CardItem cardItem;

    //Track specifics
    private final String name;
    private final LatLng[] path;
    private final LatLng startingPoint;

    private final TrackProperties properties;

    public Track(String uid, Bitmap image, String name, LatLng[] path, TrackProperties properties) {

        Required.nonNull(uid, "User ID must be non-null");
        Required.nonNull(image, "Image must be non-null");
        Required.nonNull(path, "Path must be non-null");
        Required.nonNull(properties, "Properties must be non null");

        this.TID = COUNTER_ID++;
        Log.i("hiii", "tid on create: " + TID + " " + name);

        this.uid = uid;
        this.image = image;
        this.name = name;

        if (path.length < 2)
            throw new IllegalArgumentException("A path must have at least 2 points");
        else this.path = Arrays.copyOf(path, path.length);

        startingPoint = path[0];
        this.properties = properties;

        this.cardItem = new CardItem(image, name, TID);
    }

    //TODO: either delete this or do it again when the database is on
    public static ArrayList<Track> allTracks;

    static {

        LatLng coord0 = new LatLng(46.518577, 6.563165); //inm
        LatLng coord1 = new LatLng(46.522735, 6.579772); //Banane
        LatLng coord2 = new LatLng(46.519380, 6.580669); //centre sportif
        LatLng coord3 = new LatLng(46.518475, 6.561960); //BC
        LatLng coord4 = new LatLng(46.517563, 6.562350); //Innovation parc
        LatLng coord5 = new LatLng(46.518447, 6.568238); //Rolex
        LatLng coord6 = new LatLng(46.523206, 6.564945); //SwissTech
        LatLng coord7 = new LatLng(46.520566, 6.567820); //Sat
        LatLng coord8 = new LatLng(46.506279, 6.626111); //Ouchy
        LatLng coord9 = new LatLng(46.517210, 6.630105); //Gare
        LatLng coord10 = new LatLng(46.519531, 6.633149);// Saint-Francois
        LatLng coord11 = new LatLng(46.522638, 6.634971); //Cathédrale
        LatLng coord12 = new LatLng(46.521412, 6.627383); //Flon

        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track t = new Track("Bob", b, "Cours forest !", new LatLng[]{coord0, coord1, coord2}, p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        allTracks = all;
    }

    public TrackProperties getProperties() {
        return properties;
    }

    /**
     * Compute the distance in meters between a track (its starting point) and a given coordinate.
     * See formula at: http://www.movable-type.co.uk/scripts/latlong.html
     * Set p1 as this and p2 as other
     *
     * @param other
     * @return the distance between a point and the track (this)
     */
    public double distance(LatLng other) {
        int R = 6378137; //Earth's mean radius in meter

        //angular differences in radians
        double dLat = Math.toRadians(other.latitude - this.startingPoint.latitude);
        double dLong = Math.toRadians(other.longitude - this.startingPoint.longitude);

        //this' and other's latitudes in radians
        double lat1 = Math.toRadians(this.startingPoint.latitude);
        double lat2 = Math.toRadians(other.latitude);

        //compute some factor a
        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double a2 = Math.cos(lat1) * Math.cos(lat2);
        double a3 = Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double a = a1 + a2 * a3;

        //compute some factor c
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public CardItem getCardItem() {
        return cardItem;
    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getTID() {
        return TID;
    }
}
