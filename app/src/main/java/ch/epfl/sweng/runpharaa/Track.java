package ch.epfl.sweng.runpharaa;

import android.media.Image;

public class Track
{
    private static int uid;
    //TODO: list of longitude latitude (maybe make object/sub-class) talk with Ricardo
    private static int creator_id;
    private static Image image; //TODO: check if it is the right type


    //Track specifics
    private static String location;
    private static double track_length;
    private static int average_time_length;
    private static double height_diff;
    //TODO: make a list of Tags attribute (maybe object or enumeration for the Tags type)

    //Reviews
    private static int likes;
    private static int dislikes;
    private static double like_ratio;
    //TODO: make list of reviews (create Review Type)
    //TODO: maybe add other review attributes


    public Track() {
    }

    public static void setUid(int uid) {
        Track.uid = uid;
    }

    public static void setCreator_id(int creator_id) {
        Track.creator_id = creator_id;
    }

    public static void setImage(Image image) {
        Track.image = image;
    }

    public static void setLocation(String location) {
        Track.location = location;
    }

    public static void setTrack_length(double track_length) {
        Track.track_length = track_length;
    }

    public static void setAverage_time_length(int average_time_length) {
        Track.average_time_length = average_time_length;
    }

    public static void setHeight_diff(double height_diff) {
        Track.height_diff = height_diff;
    }

    public static void setLikes(int likes) {
        Track.likes = likes;
    }

    public static void setDislikes(int dislikes) {
        Track.dislikes = dislikes;
    }

    public static void setLike_ratio(double like_ratio) {
        Track.like_ratio = like_ratio;
    }

    public static int getUid() {
        return uid;
    }

    public static int getCreator_id() {
        return creator_id;
    }

    public static Image getImage() {
        return image;
    }

    public static String getLocation() {
        return location;
    }

    public static double getTrack_length() {
        return track_length;
    }

    public static int getAverage_time_length() {
        return average_time_length;
    }

    public static double getHeight_diff() {
        return height_diff;
    }

    public static int getLikes() {
        return likes;
    }

    public static int getDislikes() {
        return dislikes;
    }

    public static double getLike_ratio() {
        return like_ratio;
    }
}
