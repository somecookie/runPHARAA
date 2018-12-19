package ch.epfl.sweng.runpharaa.tracks.properties.comment;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import ch.epfl.sweng.runpharaa.utils.Required;

public class Comment implements Comparable<Comment>{

    public static final int MAX_LENGTH = 140;

    private long dateStamp;
    private String creatorID;
    private String comment;

    public Comment(){}

    public Comment(String creatorID, String comment, Date postedDate){
        Required.nonNull(creatorID, "Creator ID of a comment cannot be null");
        Required.nonNull(comment, "Comment cannot be null");
        Required.nonNull(postedDate, "Date cannot be null");
        if(!checkSizeComment(comment))throw new IllegalArgumentException("The size of a comment must be between 0 and "+MAX_LENGTH+" characters");

        this.creatorID = creatorID;
        this.comment = comment;
        this.dateStamp = postedDate.getTime();
    }

    /**
     * Get the comment's creator ID
     *
     * @return the creator ID
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Set the creator's ID
     *
     * @param creatorID a String
     */
    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * Get the comment
     *
     * @return a String containing the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment
     *
     * @param comment a String, cannot be null
     */
    public void setComment(String comment) {
        Required.nonNull(comment, "A comment cannot be null");
        checkSizeComment(comment);
        this.comment = comment;

    }

    /**
     * Get the Comment's data stamp
     *
     * @return the data stamp
     */
    public long getDateStamp() {
        return dateStamp;
    }

    /**
     * Set the Comment's data stamp
     *
     * @param dateStamp a long
     */
    public void setDateStamp(long dateStamp) {
        this.dateStamp = dateStamp;
    }

    /**
     * Check that the comment is of size smaller than MAX_LENGTH
     *
     * @param comment a String
     * @return true if the comment is smaller than MAX_LENGTH
     */
    public static boolean checkSizeComment(String comment) {
        return comment.length() < MAX_LENGTH && comment.length() > 0;
    }

    /**
     * Get the Comment's date of creation
     *
     * @return a String containing the date
     */
    public String getDate(){
        Date d = new Date(dateStamp);

        DateFormat mediumDf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

        return mediumDf.format(d);
    }

    @Override
    public int compareTo(Comment rev) {
        if(this.getDateStamp() < rev.dateStamp) return 1;
        else if(this.getDateStamp() > rev.getDateStamp()) return -1;
        else return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return dateStamp == comment1.dateStamp &&
                Objects.equals(creatorID, comment1.creatorID) &&
                Objects.equals(comment, comment1.comment);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(dateStamp, creatorID, comment);
    }

    @Override
    public String toString() {
        return getComment()+getDateStamp();
    }
}
