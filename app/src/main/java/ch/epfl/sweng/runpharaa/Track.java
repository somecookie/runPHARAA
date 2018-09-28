package ch.epfl.sweng.runpharaa;

import android.media.Image;

import java.util.ArrayList;
import java.util.Set;

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
    private static Set<Tag> tags;

    //Reviews
    private static Reactions reactions;
    private static ArrayList<Review> reviews;//TODO: maybe change add/remove review fonction and maybe change to Set since they will be unique
    //TODO: maybe add other review/feedback attributes


    //TODO: Make more constructors
    public Track(int uid, int creator_id, Image image, String location, double track_length, int average_time_length, double height_diff, Set<Tag> tags, Reactions reactions, ArrayList<Review> reviews)
    {
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


    public static Set<Tag> getTags() {
        return tags;
    }

    public static void setTags(Set<Tag> tags) {
        Track.tags = tags;
    }

    public static ArrayList<Review> getReviews() {
        return reviews;
    }

    public static Reactions getReactions() {
        return reactions;
    }

    public static void setReactions(Reactions reactions) {
        Track.reactions = reactions;
    }

    public static void setReviews(ArrayList<Review> reviews) {
        Track.reviews = reviews;
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


    /**
     * Add a new tag to the @tags set if it does not already contains it
     * @param tag
     */
    public static void addTag(Tag tag)
    {
        tags.add(tag);
    }

    /**
     * Remove a tag of the @tags set if it already contains it
     * @param tag
     */
    public static void removeTag(Tag tag)
    {
        tags.remove(tag);
    }

    /**
     * Add new tags to the @tags set if it does not already contains them
     * @param newTags
     */
    public static void addTags(Set<Tag> newTags)
    {
        tags.addAll(newTags);
    }

    /**
     * Remove tags of the @tags set if it already contains them
     * @param tagsToRemove
     */
    public static void removeTags(Set<Tag> tagsToRemove)
    {
        tags.removeAll(tagsToRemove);
    }

    /**
     * Add a new review to the list of @reviews
     * @param review
     */
    public static void addReview(Review review)
    {
        reviews.add(review);
    }

    /**
     * Remove the first occurence of the give review in @reviews
     * @param review
     */
    public static void removeReview(Review review)
    {
        reviews.remove(review);
    }
}
