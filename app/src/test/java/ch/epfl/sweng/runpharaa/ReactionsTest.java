package ch.epfl.sweng.runpharaa;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReactionsTest {

    @Test
    public void testCreateAndGetReactions()
    {
        int likes = 2;
        int dislikes = 3;
        Reactions reac = new Reactions(likes, dislikes);

        assertEquals(reac.getLikes(), likes);
        assertEquals(reac.getDislikes(), dislikes);
        assertEquals(reac.getLikesCount(), likes - dislikes);
        assertEquals(reac.getLikesRatio(), (likes / (likes + dislikes)), 0);
    }

    @Test
    public void testGetLikesRatioWithZeroReactions()
    {
        Reactions reac = new Reactions(0,0);

        assertEquals(reac.getLikesRatio(), 1,0);
    }

    @Test
    public void testLikesDislikesIncrementation()
    {
        Reactions reac = new Reactions();

        assertEquals(reac.getLikes(), 0);
        assertEquals(reac.getDislikes(), 0);

        Reactions reac2 = reac.withNewLike();

        assertEquals(reac2.getLikes(), 1);
        assertEquals(reac2.getDislikes(), 0);

        Reactions reac3 = reac2.withNewDislike();

        assertEquals(reac3.getLikes(), 1);
        assertEquals(reac3.getDislikes(), 1);
        assertEquals(reac3.getLikesRatio(), 1/2, 0);
    }
}