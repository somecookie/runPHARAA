package ch.epfl.sweng.runpharaa;

import java.io.File;
import java.util.ArrayList;

public class User {

    private static String name = "";
    //TODO: put default picture
    private static File picture = null;
    private static ArrayList<Track> list_of_created_tracks = new ArrayList<>();
    private static ArrayList<Track> list_of_pref = new ArrayList<>();
    //TODO: set localisation champ
    private static Boolean admin = false;
    private static int uId = -1;


    public User(String name, File picture, ArrayList<Track> list_of_created_tracks, ArrayList<Track> list_of_pref, Boolean admin, int uId){
        //TODO: set localisation champ in constructor
        this.name = name;
        this.picture = picture;
        this.list_of_created_tracks = list_of_created_tracks;
        this.list_of_pref = list_of_pref;
        this.admin = admin;
        this.uId = uId;
    }

    //TODO : Other constructor for different case : No pictures, no tracks

    /*
        List of getter
     */

    public static String getName() {
        return name;
    }

    public static File getPicture() {
        return picture;
    }

    public static ArrayList<Track> getList_of_created_tracks() {
        return list_of_created_tracks;
    }

    public static ArrayList<Track> getList_of_pref() {
        return list_of_pref;
    }

    public static Boolean isAdmin() {
        return admin;
    }

    public static int getuId() {
        return uId;
    }

    /*
        List of setter
     */

    public static void setName(String name) {
        User.name = name;
    }

    public static void setPicture(File picture) {
        User.picture = picture;
    }

    public static void setList_of_created_tracks(ArrayList<Track> list_of_created_tracks) {
        User.list_of_created_tracks = list_of_created_tracks;
    }

    public static void setList_of_pref(ArrayList<Track> list_of_pref) {
        User.list_of_pref = list_of_pref;
    }

    public static void setAdmin(Boolean admin) {
        User.admin = admin;
    }

    public static void setuId(int uId) {
        User.uId = uId;
    }


}
