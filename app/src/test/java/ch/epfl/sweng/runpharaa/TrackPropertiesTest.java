package ch.epfl.sweng.runpharaa;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

@RunWith(JUnit4.class)
public class TrackPropertiesTest {

    private TrackProperties tp;
    private Set<TrackType> types;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void init(){

         types = new HashSet<>();
        types.add(TrackType.BEACH);
        tp = new TrackProperties(5.0, 200, 20, 5, types);
    }

    @Test
    public void newIllegalTrackProperties(){
        exception.expect(IllegalArgumentException.class);
        new TrackProperties(-50, 200, 20, 5, types);
    }

    @Test
    public void addNewLegalDifficulty(){
        tp.addNewDifficulty(2);

        assert tp.getAvgDifficulty() == 3.5;
    }

    @Test
    public void addIllegalDifficulty(){
        exception.expect(IllegalArgumentException.class);
        tp.addNewDifficulty(-5);
    }

    @Test
    public void addNewLegalDuration(){
        tp.addNewDuration(30);

        assert tp.getAvgDuration() == 25.0;
    }

    @Test
    public void addIllegalDuration(){
        exception.expect(IllegalArgumentException.class);
        tp.addNewDuration(-2.0);
    }

    @Test
    public void testLikes(){
        tp.addLike();

        assert tp.getLikes() == 1;
    }

    @Test
    public void testFavorites(){
        tp.addFavorite();

        assert tp.getFavorites() == 1;
    }

    @Test
    public void testGetLength(){
        assert tp.getLength() == 5.0;
    }
    @Test
    public void testGetHeihtDiff(){
        assert tp.getHeightDifference() == 200.0;
    }

    @Test
    public void testSetters(){
        tp.setFavorites(1);
        tp.setHeightDifference(10);
        tp.setLength(300);
        tp.setLikes(3);
    }

}
