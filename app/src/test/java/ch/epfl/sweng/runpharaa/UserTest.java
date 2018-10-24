package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

import static ch.epfl.sweng.runpharaa.tracks.TrackType.FOREST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void basicConstructorGetAttributesTest() {
        User FAKE_USER = new User("test1", 30, null, new ArrayList<String>(), new ArrayList<String>(), new LatLng(46.518510, 6.563199), false,  "2000");

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
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        ArrayList<Track> all = new ArrayList<>();

        all.add(new Track("0","0", null,"Innovation Parc -> BC",Arrays.asList(new CustLatLng(46.517563, 6.562350), new CustLatLng(46.518475, 6.561960)),p));
        all.add(new Track("1","0",null,"Rolex -> Swisstech",Arrays.asList(new CustLatLng(46.518447, 6.568238), new CustLatLng(46.523206, 6.564945)),p));
        all.add(new Track("2","0",null,"Sat -> INM",Arrays.asList(new CustLatLng(46.520566, 6.567820), new CustLatLng(46.518577, 6.563165)),p));
        all.add(new Track("3","0", null,"Banane -> Centre Sportif",Arrays.asList(new CustLatLng(46.522735, 6.579772), new CustLatLng(46.519380, 6.580669)),p));

        //Test done in function of the location, maybe override equals in tracks to test
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i).getName(), FAKE_USER.tracksNearMe().get(i).getName());
        }
    }

    @Test
    public void likedTracks() {
        User FAKE_USER = new User("test1", new LatLng(46.518510, 6.563199), 2000);

        FAKE_USER.like("0");
        assertTrue(FAKE_USER.alreadyLiked("0"));

        FAKE_USER.unlike("0");
        assertFalse(FAKE_USER.alreadyLiked("0"));
    }
}