package ch.epfl.sweng.runpharaa;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public final class User {
    private int preferredRadius = 2000;
    private final String name;
    //TODO: put default picture
    private final File picture;
    private final ArrayList<Track> list_of_created_tracks;
    private final ArrayList<Track> list_of_pref;
    private LatLng location;
    private final boolean admin;
    private final int uId;

    public static User FAKE_USER = new User("Toto", new LatLng(46.518510, 6.563199), 6000);


    public User(String name,int preferredRadius, File picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, LatLng location, Boolean admin, int uId){
        this.preferredRadius = preferredRadius;
        this.name = name;
        this.picture = picture;
        this.list_of_created_tracks = list_of_created_tracks;
        this.list_of_pref = list_of_pref;
        this.location = location;
        this.admin = admin;
        this.uId = uId;
    }

    public User(String name, LatLng location, int preferredRadius){
        //TODO must be changed later when the user's login and the database are on
        this(name,preferredRadius, null, null, null, location, false, 0);
    }

    public int getPreferredRadius() {
        return preferredRadius;
    }

    /**
     * Make an ordered list of all the tracks that are in a RADIUS of 2km.
     * @return ordered list of tracks
     */
    @TargetApi(Build.VERSION_CODES.N)
    public ArrayList<Track> tracksNearMe(){
        ArrayList<Track> nm = new ArrayList<>();
        ArrayList<Track> allTracks = Track.allTracks(); //Todo muste be changed when the database is done

        //filter the tracks that start too far from the location
        for(Track tr: allTracks){
            if(tr.distance(location) <= preferredRadius){
                nm.add(tr);
            }
        }

        //order them from the nearest to the furthest
        nm.sort(new Comparator<Track>() {
            @Override
            public int compare(Track o1, Track o2) {
                double d1 = o1.distance(location);
                double d2 = o2.distance(location);
                if(d1 < d2) return -1;
                else if(d1 == d2) return 0;
                else return 1;
            }
        });

        return nm;
    }

    /**
     * Getter for the user's location
     * @return location
     */
    public LatLng getLocation(){
        return location;
    }

    /**
     * Return the name of the user
     * @return name
     */
    public String getName(){
        return name;
    }


    /**
     * Update the user's location
     * @param newLocation
     */
    public void setLocation(LatLng newLocation){
        this.location = newLocation;
    }

}