package ch.epfl.sweng.runpharaa;

import android.media.Image;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@IgnoreExtraProperties
public class Track
{
    //Track identifiers
    private int tid;
    private int creator_id; //TODO: Make a map from creator_if -> name?
    @Exclude
    private int image;
    @Exclude
    private CardItem cardItem;

    //Track specifics
    private String location;
    private LatLng[] path;
    private LatLng startingPoint;
    private double track_length;        //meters
    private double average_time_length; //minutes
    //private final String difficulty;  //TODO: Build a range for each difficulty based on height difference: easy = < 1m? Or create this based on the difficulty users report for same track?
    private double height_diff;
    private Set<Tag> tags;

    //Reviews
    private int likes;
    private int favourites;
    private Reactions reactions;
    private ArrayList<Review> reviews; //TODO: Implement this once we have a notion of a User.

    //TODO: Make more constructors
    public Track(int tid, int creator_id, int image, String location, LatLng[] path, double track_length, double average_time_length,
                 double height_diff, Set<Tag> tags, int likes, int favourites, Reactions reactions, ArrayList<Review> reviews)
    {
        if(path == null){
            throw new NullPointerException("The path must me defined.");
        }else if(path.length < 2){
            throw  new IllegalArgumentException("The path must contain at least 2 points (start and end).");
        }else{
            this.path = path;
            this.startingPoint = path[0];
            this.cardItem = new CardItem(image, location, tid);
        }

        this.tid = tid;
        this.creator_id = creator_id;
        this.image = image;
        this.location = location;
        this.track_length = track_length;
        this.average_time_length = average_time_length;
        this.height_diff = height_diff;
        this.tags = tags;
        this.likes = likes;
        this.favourites = favourites;
        this.reactions = reactions;
        this.reviews = reviews;
    }

    //Testing purposes
    public Track(LatLng[] path){
        this(0,0,0,"Test", path,0,0,0,null,0,0,null,null);
    }

    public Track(String name, LatLng[] path){
        this(0,0,0, name, path,0,0,0, null, 0,0,null,null);
    }

    public Track(int tid, String name, int image, LatLng[] path){
        this(0,0, image, name, path,0,0,0,null,0,0,null,null);
    }

    public Track(int tid, int image, String name, LatLng[] path, double track_length, int average_time, int likes, int favourites){
        this(tid,0, image, name, path, track_length, average_time,0,null, likes, favourites, null, null);
    }

    public Track(String name){
        this(0,0,0, name, new LatLng[]{new LatLng(46.522735, 6.579772), new LatLng(46.519380, 6.580669)},0,0,0, null, 0,0,null,null);
    }

    public Track() {} // Default constructor required for calls to DataSnapshot.getValue(Track.class): Firebase Database

    //TODO: either delete this or do it again when the database is on
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
        LatLng coord10 = new LatLng(46.519531, 6.633149);// Saint-Francois
        LatLng coord11 = new LatLng(46.522638, 6.634971); //Cathédrale
        LatLng coord12 = new LatLng(46.521412, 6.627383); //Flon

        ArrayList<Track> all = new ArrayList<>();
        all.add(new Track(0, R.drawable.centre_sportif, "Banane -> Centre Sportif", new LatLng[]{coord1, coord2}, 350, 10, 3, 4));
        all.add(new Track(1, R.drawable.innovation_park, "Innovation Parc -> BC", new LatLng[]{coord4, coord3}, 300, 2, 1, 1));
        all.add(new Track(2, R.drawable.rolex, "Rolex -> Swisstech", new LatLng[]{coord5, coord6}, 850, 8, 4, 2));
        all.add(new Track(3, R.drawable.rolex, "Sat -> INM", new LatLng[]{coord7, coord0}, 450, 5, 6, 7));
        all.add(new Track(4, R.drawable.ouchy, "Ouchy -> Gare", new LatLng[]{coord8, coord9}, 1300, 20, 10, 12));
        all.add(new Track(5, R.drawable.saint_francois, "SF -> Cath -> Flon", new LatLng[]{coord10, coord11, coord12}, 0, 0, 0,0));

        return all;

    }

    public CardItem getCardItem() {
        return this.cardItem;
    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    public String getLocation() {
        return location;
    }

    public int getUid() { return tid; }

    public LatLng[] getPath() { return path; }

    public int getCreator_id() { return creator_id; }

    public int getImage() { return image; }

    public double getTrack_length() { return track_length; }

    public double getAverage_time_length() { return average_time_length; }

    public double getHeight_diff() { return height_diff; }

    public Set<Tag> getTags() { return tags; }

    public int getLikes() { return likes; }

    public int getFavourites() { return favourites; }

    public Reactions getReactions() { return reactions; }

    public ArrayList<Review> getReviews() { return reviews; }

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("location", location);
        result.put("LatLng", path);

        return result;
    }
    /*
    /**
     * Add a new tag to the @tags set if it does not already contains it
     * @param tag
     *
    public static void addTag(Tag tag)
    {
        tags.add(tag);
    }*/
}
