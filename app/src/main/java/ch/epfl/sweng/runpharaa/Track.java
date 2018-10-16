package ch.epfl.sweng.runpharaa;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Track
{
    //Track identifiers
    private String trackUid;
    private String creatorUid; //TODO: Make a map from creator_if -> name?
    @Exclude
    private int image;//TODO for database download into local file
    private String imageStoragePath;
    @Exclude
    private CardItem cardItem;

    private Uri imageURL; //TODO ERASE

    //Track specifics
    private String location;
    private List<CustLatLng> path;
    private CustLatLng startingPoint;
    private double trackLength;        //meters
    private double averageTimeLength;  //minutes
    //private final String difficulty;  //TODO: Build a range for each difficulty based on height difference: easy = < 1m? Or create this based on the difficulty users report for same track?
    private double heightDiff;
    @Exclude
    private Set<Tag> tags;

    //Reviews
    @Exclude
    private int likes;
    @Exclude
    private int favourites;
    @Exclude
    private Reactions reactions;
    @Exclude
    private ArrayList<Review> reviews; //TODO: Implement this once we have a notion of a User.

    //TODO: Make more constructors
    public Track(String trackUid, String creatorUid, int image, String imageStoragePath, String location, List<CustLatLng> path, double trackLength, double averageTimeLength,
                 double heightDiff, Set<Tag> tags, int likes, int favourites, Reactions reactions, ArrayList<Review> reviews)
    {
        if(path == null){
            throw new NullPointerException("The path must me defined.");
        }else if(path.size() < 2){
            throw  new IllegalArgumentException("The path must contain at least 2 points (start and end).");
        }else{
            this.path = path;
            this.startingPoint = path.get(0);
            this.cardItem = new CardItem(image, location, trackUid, /*imageStoragePath*/"https://firebasestorage.googleapis.com/v0/b/runpharaa.appspot.com/o/TrackImages%2Fcentre_sportif.jpg?alt=media&token=488e4962-1201-4ce7-8389-4292a52847e4");
        }

        this.trackUid = trackUid;
        this.creatorUid = creatorUid;
        this.image = image;
        this.imageStoragePath = imageStoragePath;
        this.location = location;
        this.trackLength = trackLength;
        this.averageTimeLength = averageTimeLength;
        this.heightDiff = heightDiff;
        this.tags = tags;
        this.likes = likes;
        this.favourites = favourites;
        this.reactions = reactions;
        this.reviews = reviews;
    }

    //Testing purposes
    public Track(List<CustLatLng> path){
        this("0","0",0,"","Test", path,0,0,0,null,0,0,null,null);
    }

    public Track(String name, List<CustLatLng> path){
        this("0","0",0,"", name, path,0,0,0, null, 0,0,null,null);
    }

    public Track(String trackUid, String name, int image, List<CustLatLng> path){
        this(trackUid,"0", image,"", name, path,0,0,0,null,0,0,null,null);
    }

    public Track(String trackUid, int image, String name, List<CustLatLng> path, double track_length, int average_time, int likes, int favourites){
        this(trackUid,"0", image,"", name, path, track_length, average_time,0,null, likes, favourites, null, null);
    }

    public Track(String name){
        this("0","0",0, "",name, Arrays.asList(new CustLatLng(46.522735, 6.579772), new CustLatLng(46.519380, 6.580669)),0,0,0, null, 0,0,null,null);
    }

    public Track(int image, String name, List<CustLatLng> path, double track_length, int average_time, int likes, int favourites){
        this("0","0", image,"", name, path, track_length, average_time,0,null, likes, favourites, null, null);
    }

    public Track() {} // Default constructor required for calls to DataSnapshot.getValue(Track.class): Firebase Database

    public void setImageURL(Uri imageURL) { this.imageURL = imageURL; } //TODO ERASE

    public Uri getImageURL() { return this.imageURL; } //TODO ERASE


    //TODO: either delete this or do it again when the database is on
    public static ArrayList<Track> allTracks(){
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

        ArrayList<Track> all = new ArrayList<>();
        all.add(new Track("0", R.drawable.centre_sportif, "Banane -> Centre Sportif", Arrays.asList(coord1, coord2), 350, 10, 3, 4));
        all.add(new Track("1", R.drawable.innovation_park, "Innovation Parc -> BC", Arrays.asList(coord4, coord3), 300, 2, 1, 1));
        all.add(new Track("2", R.drawable.rolex, "Rolex -> Swisstech",Arrays.asList(coord5, coord6), 850, 8, 4, 2));
        all.add(new Track("3", R.drawable.rolex, "Sat -> INM", Arrays.asList(coord7, coord0), 450, 5, 6, 7));
        all.add(new Track("4", R.drawable.ouchy, "Ouchy -> Gare", Arrays.asList(coord8, coord9), 1300, 20, 10, 12));
        all.add(new Track("5", R.drawable.saint_francois, "SF -> Cath -> Flon", Arrays.asList(coord10, coord11, coord12), 0, 0, 0,0));

        //all.add(new Track("Test", );
        return all;

    }

    @Exclude
    public CardItem getCardItem() {
        return this.cardItem;
    }

    public LatLng getStartingPoint() {
        return new LatLng(startingPoint.getLatitude(), startingPoint.getLongitude());
    }

    public String getLocation() {
        return location;
    }

    public String getTrackUid() { return trackUid; }

    public void setTrackUid(String trackUid) { this.trackUid = trackUid; }

    public List<CustLatLng> getPath() { return path; }

    public String getCreatorUid() { return creatorUid; }

    @Exclude
    public int getImage() { return image; }

    public double getTrackLength() { return trackLength; }

    public double getAverageTimeLength() { return averageTimeLength; }

    public double getHeightDiff() { return heightDiff; }

    @Exclude
    public Set<Tag> getTags() { return tags; }

    public int getLikes() { return likes; }

    public int getFavourites() { return favourites; }

    @Exclude
    public Reactions getReactions() { return reactions; }

    @Exclude
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
        double dLat = Math.toRadians(other.latitude - this.startingPoint.getLatitude());
        double dLong = Math.toRadians(other.longitude-this.startingPoint.getLongitude());

        //this' and other's latitudes in radians
        double lat1 = Math.toRadians(this.startingPoint.getLatitude());
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
