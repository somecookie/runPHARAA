package ch.epfl.sweng.runpharaa;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class User {
    private static final int RADIUS = 2000;
    private final String name;
    //TODO: put default picture
    private final File picture;
    private final ArrayList<Track> list_of_created_tracks;
    private final ArrayList<Track> list_of_pref;
    private final LatLng location;
    private final boolean admin;
    private final int uId;


    public User(String name, File picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, Location location, Boolean admin, int uId){
        this.name = name;
        this.picture = picture;
        this.list_of_created_tracks = list_of_created_tracks;
        this.list_of_pref = list_of_pref;
        this.location = new LatLng(46.518577, 6.563165); //TODO must be change to the real location of the user(for now in INM)
        this.admin = admin;
        this.uId = uId;
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
            if(tr.distance(location) <= RADIUS){
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



    /*
        List of setter
     */
    /*
    public User withName(String newName) {
        return new User(newName, picture, list_of_created_tracks, list_of_pref, location, admin, uId);
    }

    public User withPicture(File newPicture) {
        return new User(name, newPicture, list_of_created_tracks, list_of_pref, location, admin, uId);
    }

    public User withList_of_created_tracks(ArrayList<Track> new_list_of_created_tracks) {
        return new User(name, picture, new_list_of_created_tracks, list_of_pref, location, admin, uId);
    }

    public User withList_of_pref(ArrayList<Track> new_list_of_pref) {
        return new User(name, picture, list_of_created_tracks, new_list_of_pref, location, admin, uId);
    }

    public User withAdmin(Boolean newAdmin) {
        return new User(name, picture, list_of_created_tracks, list_of_pref, location, newAdmin, uId);
    }

    public User withLocation(Location newLocation) {
        return new User(name, picture, list_of_created_tracks, list_of_pref, newLocation, admin, uId);
    }

    public User withuId(int newuId) {
        return new User(name, picture, list_of_created_tracks, list_of_pref, location, admin, newuId);
    }
*/

}