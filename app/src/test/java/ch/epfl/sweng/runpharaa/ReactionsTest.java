package ch.epfl.sweng.runpharaa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReactionsTest {

    @Test
    public void testCreateAndGetReactions()
    {
        int likes = 2;
        int dislikes = 3;
        Reactions reac = new Reactions(likes, dislikes);

        assertEquals(likes, reac.getLikes());
        assertEquals(dislikes, reac.getDislikes());
        assertEquals(likes - dislikes, reac.getLikesCount());
        assertEquals((likes / (likes + dislikes)), reac.getLikesRatio(),  0);
    }

    @Test
    public void constructorNegativeLikeTest(){
        try{
            Reactions r = new Reactions(-1, 2);
        } catch (IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void constructorNegativeDislikeTest(){
        try{
            Reactions r = new Reactions(100, -2);
        } catch (IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void testGetLikesRatioWithZeroReactions()
    {
        Reactions reac = new Reactions(0,0);

        assertEquals(1, reac.getLikesRatio(), 0);
    }

    @Test
    public void testLikesDislikesIncrementation()
    {
        Reactions reac = new Reactions();

        assertEquals(0, reac.getLikes());
        assertEquals(0, reac.getDislikes());

        Reactions reac2 = reac.withNewLike();

        assertEquals(1, reac2.getLikes());
        assertEquals(0, reac2.getDislikes());

        Reactions reac3 = reac2.withNewDislike();

        assertEquals(1, reac3.getLikes());
        assertEquals(1, reac3.getDislikes());
        assertEquals(1/2, reac3.getLikesRatio(), 0);
    }
}