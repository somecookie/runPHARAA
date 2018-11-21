package ch.epfl.sweng.runpharaa.review;

import ch.epfl.sweng.runpharaa.utils.Required;

public class Review {
    private String name;
    private String comment;

    public Review(){}

    public Review(String name, String comment){
        Required.nonNull(name, "Name of a comment cannot be null");
        Required.nonNull(comment, "Comment cannot be null");
        checkSizeComment(comment);

        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        Required.nonNull(comment, "A comment cannot be null");
        checkSizeComment(comment);
        this.comment = comment;

    }

    private void checkSizeComment(String comment) {
        if(comment.length() > 140) throw new IllegalArgumentException("Maximum size of a comment is 140 characters");
    }


}
