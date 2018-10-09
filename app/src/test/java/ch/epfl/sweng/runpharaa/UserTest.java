package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void basicConstructorGetAttributesTest() {

        User FAKE_USER = new User("test1", 30, null, new ArrayList<Track>(), new ArrayList<Track>(), new LatLng(46.518510, 6.563199), false,  2000);

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
        all.add(new Track(0,"Innovation Parc -> BC",R.drawable.innovation_park,new LatLng[]{new LatLng(46.517563, 6.562350), new LatLng(46.518475, 6.561960)}));
        all.add(new Track(1,"Rolex -> Swisstech",R.drawable.rolex, new LatLng[]{new LatLng(46.518447, 6.568238), new LatLng(46.523206, 6.564945)}));
        all.add(new Track(2,"Sat -> INM",R.drawable.rolex, new LatLng[]{new LatLng(46.520566, 6.567820), new LatLng(46.518577, 6.563165)}));
        all.add(new Track(3,"Banane -> Centre Sportif",R.drawable.centre_sportif ,new LatLng[]{new LatLng(46.522735, 6.579772), new LatLng(46.519380, 6.580669)}));

        //Test done in function of the location, maybe override equals in tracks to test
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i).getLocation(), FAKE_USER.tracksNearMe().get(i).getLocation());
        }
    }
}