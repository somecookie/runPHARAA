package ch.epfl.sweng.runpharaa;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagTest {

    @Test
    public void createUserAndGetValues() {
        Tag t;
        Tag city = Tag.CITY;
        Tag forest = Tag.FOREST;
        Tag easy = Tag.EASY;
        Tag medium = Tag.MEDIUM;
        Tag hard = Tag.HARD;


        assertEquals("CITY", city.toString());
        assertEquals("FOREST", forest.toString());
        assertEquals("EASY", easy.toString());
        assertEquals("MEDIUM", medium.toString());
        assertEquals("HARD", hard.toString());

    }
}
