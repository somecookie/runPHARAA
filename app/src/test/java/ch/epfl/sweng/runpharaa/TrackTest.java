package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrackTest {

    LatLng coord1 = new LatLng(46.518577, 6.563165); //inm
    LatLng coord2 = new LatLng(46.519380, 6.580669); //centre sportif

    @Test
    public void constructorNullPathTest(){
        try {
            Track t = new Track("Test", null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void constructorPathLength1Test(){
        LatLng[] path = {new LatLng(1.221, 2.133)};
        try {
            Track t = new Track("Toto", 1, path);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void distance_isCorrect_for_small_distances(){
        //should be 1.342 km

        LatLng[] path = {coord1, coord2};

        Track tr = new Track(path);
        assert(Math.abs(tr.distance(coord2)-1342) < 5);
    }

    @Test
    public void distance_is_commutative(){
        LatLng[] path1 ={coord1, coord2};
        LatLng[] path2 ={coord2, coord1};

        Track tr1 = new Track("Track1", path1);
        Track tr2 = new Track("Track2", path2);

        assert(tr1.distance(coord2) == tr2.distance(coord1));
    }

    @Test
    public void allTracksTest(){
        ArrayList<Track> all = new ArrayList<>();
        all.add(new Track("Banane -> Centre Sportif",R.drawable.centre_sportif ,new LatLng[]{new LatLng(46.522735, 6.579772), new LatLng(46.519380, 6.580669)}));
        all.add(new Track("Innovation Parc -> BC",R.drawable.innovation_park,new LatLng[]{new LatLng(46.517563, 6.562350), new LatLng(46.518475, 6.561960)}));
        all.add(new Track("Rolex -> Swisstech",R.drawable.rolex, new LatLng[]{new LatLng(46.518447, 6.568238), new LatLng(46.523206, 6.564945)}));
        all.add(new Track("Sat -> INM",R.drawable.rolex, new LatLng[]{new LatLng(46.520566, 6.567820), new LatLng(46.518577, 6.563165)}));
        all.add(new Track("Ouchy -> Gare",R.drawable.ouchy, new LatLng[]{new LatLng(46.506279, 6.626111), new LatLng(46.517210, 6.630105)}));
        all.add(new Track("SF -> Cath -> Flon",R.drawable.saint_francois, new LatLng[]{new LatLng(46.519531, 6.633149), new LatLng(46.522638, 6.634971), new LatLng(46.521412, 6.627383)}));

        //Test done in function of the location, maybe override equals in tracks to test
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i).getLocation(), Track.allTracks().get(i).getLocation());
        }
    }

    @Test
    public void getStartingPointTest(){
        Track t = new Track("Banane -> Centre Sportif",R.drawable.centre_sportif ,new LatLng[]{new LatLng(46.522735, 6.579772),  new LatLng(46.519380, 6.580669)});

        assertEquals(new LatLng(46.522735, 6.579772), t.getStartingPoint());
    }

    @Test
    public void getLocationTest(){
        Track t = new Track("TestLocation",R.drawable.centre_sportif ,new LatLng[]{new LatLng(46.522735, 6.579772),  new LatLng(46.519380, 6.580669)});

        assertEquals("TestLocation", t.getLocation());
    }

    @Test
    public void getCardItemTest(){
        Track t = new Track("ici",R.drawable.centre_sportif ,new LatLng[]{new LatLng(46.522735, 6.579772),  new LatLng(46.519380, 6.580669)});

        assertEquals(new CardItem(R.drawable.centre_sportif, "ici", 0).getBackground(), t.getCardItem().getBackground());
        assertEquals(new CardItem(R.drawable.centre_sportif, "ici", 0).getName(), t.getCardItem().getName());
    }
}
