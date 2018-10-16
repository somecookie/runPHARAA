package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.runpharaa.tracks.Track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void basicConstructorGetAttributesTest() {

        User FAKE_USER = new User("test1", 30, null, new ArrayList<Track>(), new ArrayList<Track>(), new LatLng(46.518510, 6.563199), false,  "2000");

        assertEquals(new LatLng(46.518510, 6.563199), FAKE_USER.getLocation());
        assertEquals(30, FAKE_USER.getPreferredRadius());

    }

    @Test
    public void secondConstructorGetAttributesTest() {

        User FAKE_USER = new User("test1", new LatLng(46.518510, 6.563199), 2000);
        User FAKE_USER2 = new User("Toto", new LatLng(29.518510, 36.563199), 30);

        assertEquals(new LatLng(29.518510, 36.563199), FAKE_USER2.getLocation());
        assertEquals("test1", FAKE_USER.getName());
        assertEquals("Toto", FAKE_USER2.getName());
    }

    @Test
    public void changeLocationTest(){
        User FAKE_USER = new User("test1", new LatLng(16.518510, 26.563199), 2000);

        FAKE_USER.setLocation(new LatLng(42.518510, 42.563199));

        assertEquals(new LatLng(42.518510, 42.563199), FAKE_USER.getLocation());
    }

    @Test
    public void trackNearMeTest(){
        User FAKE_USER = new User("test1", new LatLng(46.518510, 6.563199), 2000);

        ArrayList<Track> all = new ArrayList<>();

        //Test done in function of the location, maybe override equals in tracks to test
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i).getName(), FAKE_USER.tracksNearMe().get(i).getName());
        }
    }

    @Test
    public void likedTracks() {
        User FAKE_USER = new User("test1", new LatLng(46.518510, 6.563199), 2000);

        FAKE_USER.like(0);
        assertTrue(FAKE_USER.alreadyLiked(0));

        FAKE_USER.unlike(0);
        assertFalse(FAKE_USER.alreadyLiked(0));
    }
}