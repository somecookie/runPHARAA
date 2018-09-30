package ch.epfl.sweng.runpharaa;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Track
{
    private final int uid;
    private final LatLng[] path;
    private final LatLng startingPoint;
    private final int creator_id;
    private final Image image; //TODO: check if it is the right type


    //Track specifics
    private final String location;
    private final double track_length;
    private final int average_time_length;
    private final double height_diff;
    private final Set<Tag> tags;

    //Reviews
    private final Reactions reactions;
    private final ArrayList<Review> reviews;//TODO: maybe change add/remove review fonction and maybe change to Set since they will be unique
    //TODO: maybe add other review/feedback attributes


    //TODO: Make more constructors
    public Track(LatLng[] path, int uid, int creator_id, Image image, String location, double track_length, int average_time_length, double height_diff, Set<Tag> tags, Reactions reactions, ArrayList<Review> reviews)
    {
        if(path == null){
            throw new NullPointerException("The path must me defined.");
        }else if(path.length < 2){
            throw  new IllegalArgumentException("The path must contain at least 2 points (start and end).");
        }else{
            this.path = path;
            this.startingPoint = path[0];
        }

        this.uid = uid;
        this.creator_id = creator_id;
        this.image = image;
        this.location = location;
        this.track_length = track_length;
        this.average_time_length = average_time_length;
        this.height_diff = height_diff;
        this.tags = tags;
        this.reactions = reactions;
        this.reviews = reviews;
    }

    //testing puposes
    public Track(LatLng[] path){
        this(path, 0, 0, null, "Test", 0, 0, 0,null, null, null);
    }

    //must either delete it or do it again when the database is on
    public static ArrayList<Track> allTracks(){
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
        LatLng coord10 = new LatLng(46.519531, 6.633149);//Saint-Francois
        LatLng coord11 = new LatLng(46.522638, 6.634971); //Cathédrale
        LatLng coord12 = new LatLng(46.521412, 6.627383); //Flon

        ArrayList<Track> all = new ArrayList<>();
        all.add(new Track(new LatLng[]{coord1, coord2}));
        all.add(new Track(new LatLng[]{coord3, coord4}));
        all.add(new Track(new LatLng[]{coord5, coord6}));
        all.add(new Track(new LatLng[]{coord7, coord0}));
        all.add(new Track(new LatLng[]{coord8, coord9}));
        all.add(new Track(new LatLng[]{coord10, coord11, coord12}));

        return all;

    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    /**
     * Compute the distance in meters between a track (its starting point) and a given coordinate.
     * See formula at: http://www.movable-type.co.uk/scripts/latlong.html
     * Set p1 as this and p2 as other
     * @param other
     * @return the distance between a point and the track (this)
     */
    public double distance(LatLng other){
        int R = 6378137; //Earth's mean radius in meter

        //angular differences in radians
        double dLat = Math.toRadians(other.latitude - this.startingPoint.latitude);
        double dLong = Math.toRadians(other.longitude-this.startingPoint.longitude);

        //this' and other's latitudes in radians
        double lat1 = Math.toRadians(this.startingPoint.latitude);
        double lat2 = Math.toRadians(other.latitude);

        //compute some factor a
        double a1 = Math.sin(dLat/2)*Math.sin(dLat/2);
        double a2 = Math.cos(lat1)*Math.cos(lat2);
        double a3 = Math.sin(dLong/2)*Math.sin(dLong/2);

        double a = a1 + a2*a3;

        //compute some factor c
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R*c;
    }

    /*
    /**
     * Add a new tag to the @tags set if it does not already contains it
     * @param tag
     *
    public static void addTag(Tag tag)
    {
        tags.add(tag);
    }

    /**
     * Remove a tag of the @tags set if it already contains it
     * @param tag
     *
    public static void removeTag(Tag tag)
    {
        tags.remove(tag);
    }

    /**
     * Add new tags to the @tags set if it does not already contains them
     * @param newTags
     *
    public static void addTags(Set<Tag> newTags)
    {
        tags.addAll(newTags);
    }

    /**
     * Remove tags of the @tags set if it already contains them
     * @param tagsToRemove
     *
    public static void removeTags(Set<Tag> tagsToRemove)
    {
        tags.removeAll(tagsToRemove);
    }

    /**
     * Add a new review to the list of @reviews
     * @param review
     *
    public static void addReview(Review review)
    {
        reviews.add(review);
    }

    /**
     * Remove the first occurence of the give review in @reviews
     * @param review
     *
    public static void removeReview(Review review)
    {
        reviews.remove(review);
    }
    */
}
