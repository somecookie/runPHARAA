package ch.epfl.sweng.runpharaa;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.runpharaa.comment.Comment;

import static android.os.SystemClock.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class CommentTest {

    Date d = new Date();
    Comment c1 = new Comment("Bob", "This is a comment.", d);
    Comment c2 = new Comment("Bob", "This is a 2nd comment.", d);
    Comment c3 = new Comment("Amanda", "This is a comment.", d);
    Comment c4 = new Comment("Bob", "This is a comment.", new Date(d.getTime()+1));

    Comment c5 = new Comment("Bob", "First", d);
    Comment c6 = new Comment("Bob", "Second", new Date(d.getTime()+1));
    Comment c7 = new Comment("Bob", "Third", new Date(d.getTime()+2));
    Comment c8 = new Comment("Bob", "Last", new Date(d.getTime()+3));

    @Test
    public void equalsWorksOnSelf() {
        assertEquals(c1, c1);
    }

    @Test
    public void equalWorksOnDifferent() {
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
        assertNotEquals(c1, c2);
    }

    @Test
    public void sortsCommentsCorrectly() {
        List<Comment> ordered = new ArrayList<>(Arrays.asList(c8, c7, c6, c5));
        List<Comment> random = new ArrayList<>(Arrays.asList(c5, c8, c6, c7));
        Collections.sort(random);
        assertEquals(ordered, random);
    }

    @Test
    public void buildComment() {
        Comment c9 = new Comment();
        c9.setCreatorID("Bob");
        assertEquals(c9.getCreatorID(), "Bob");
        c9.setComment("Hi");
        assertEquals(c9.getComment(), "Hi");
        c9.setDateStamp(1234567890);
        assertEquals(c9.getDateStamp(), 1234567890);
        assertEquals(c9.getDate(), DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(1234567890)));
    }

}
