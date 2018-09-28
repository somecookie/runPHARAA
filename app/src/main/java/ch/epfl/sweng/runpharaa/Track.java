package ch.epfl.sweng.runpharaa;

import android.media.Image;

import java.util.ArrayList;
import java.util.Set;

public class Track
{
    private final int uid;
    //TODO: list of longitude latitude (maybe make object/sub-class) talk with Ricardo
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
