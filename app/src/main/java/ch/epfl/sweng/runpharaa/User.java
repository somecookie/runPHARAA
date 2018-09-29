package ch.epfl.sweng.runpharaa;

import android.location.Location;

import java.io.File;
import java.util.ArrayList;

public final class User {
    private static final int RADIUS = 5;
    private final String name;
    //TODO: put default picture
    private final File picture;
    private final ArrayList<Track> list_of_created_tracks;
    private final ArrayList<Track> list_of_pref;
    private final Location location;
    private final boolean admin;
    private final int uId;


    public User(String name, File picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, Location location, Boolean admin, int uId){
        //TODO: set localisation champ in constructor
        this.name = name;
        this.picture = picture;
        this.list_of_created_tracks = list_of_created_tracks;
        this.list_of_pref = list_of_pref;
        this.location = location;
        this.admin = admin;
        this.uId = uId;
    }

    //TODO : Other constructor for different case : No pictures, no tracks



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