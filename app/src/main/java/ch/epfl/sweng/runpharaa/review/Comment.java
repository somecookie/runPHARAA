package ch.epfl.sweng.runpharaa.review;

import java.text.DateFormat;
import java.util.Date;

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
        if(!checkSizeComment(comment))throw new IllegalArgumentException("Maximum size of a comment is +"+MAX_LENGTH+" characters");

        this.creatorID = creatorID;
        this.comment = comment;
        this.dateStamp = postedDate.getTime();
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        Required.nonNull(comment, "A comment cannot be null");
        checkSizeComment(comment);
        this.comment = comment;

    }

    public long getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(long dateStamp) {
        this.dateStamp = dateStamp;
    }

    public static boolean checkSizeComment(String comment) {
        return comment.length() < MAX_LENGTH;
    }

    public String getDate(){
        Date d = new Date(dateStamp);

        DateFormat mediumDf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

        return mediumDf.format(d);
    }


    @Override
    public int compareTo(Comment rev) {
        Date thisDate = new Date(dateStamp);
        Date thatDate = new Date(dateStamp);
        return thisDate.compareTo(thatDate);
    }
}
