package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrackTest {

    Track tracko = new Track();

    CustLatLng coord1 = new CustLatLng(46.518577, 6.563165); //inm
    CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif

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
        List<CustLatLng> path = Arrays.asList(new CustLatLng(1.221, 2.133));
        try {
            Track t = new Track("0","Toto", 1, path);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void distance_isCorrect_for_small_distances(){
        //should be 1.342 km

        List<CustLatLng> path = Arrays.asList(coord1, coord2);

        Track tr = new Track(path);
        assert(Math.abs(tr.distance(new LatLng(coord2.getLatitude(), coord2.getLongitude()))-1342) < 5);
    }

    @Test
    public void distance_is_commutative(){
        List<CustLatLng> path1 = Arrays.asList(coord1, coord2);
        List<CustLatLng> path2 = Arrays.asList(coord2, coord1);

        Track tr1 = new Track("Track1", path1);
        Track tr2 = new Track("Track2", path2);

        assert(tr1.distance(new LatLng(coord2.getLatitude(), coord2.getLongitude())) == tr2.distance(new LatLng(coord1.getLatitude(), coord1.getLongitude())));
    }

    @Test
    public void allTracksTest(){
        ArrayList<Track> all = new ArrayList<>();
        all.add(new Track("0","Banane -> Centre Sportif",R.drawable.centre_sportif ,Arrays.asList(new CustLatLng(46.522735, 6.579772), new CustLatLng(46.519380, 6.580669))));
        all.add(new Track("1","Innovation Parc -> BC",R.drawable.innovation_park,Arrays.asList(new CustLatLng(46.517563, 6.562350), new CustLatLng(46.518475, 6.561960))));
        all.add(new Track("2","Rolex -> Swisstech",R.drawable.rolex, Arrays.asList(new CustLatLng(46.518447, 6.568238), new CustLatLng(46.523206, 6.564945))));
        all.add(new Track("3","Sat -> INM",R.drawable.rolex, Arrays.asList(new CustLatLng(46.520566, 6.567820), new CustLatLng(46.518577, 6.563165))));
        all.add(new Track("4","Ouchy -> Gare",R.drawable.ouchy, Arrays.asList(new CustLatLng(46.506279, 6.626111), new CustLatLng(46.517210, 6.630105))));
        all.add(new Track("5","SF -> Cath -> Flon",R.drawable.saint_francois, Arrays.asList(new CustLatLng(46.519531, 6.633149), new CustLatLng(46.522638, 6.634971), new CustLatLng(46.521412, 6.627383))));

        //Test done in function of the location, maybe override equals in tracks to test
        for(int i = 0; i < all.size(); i++){
            assertEquals(all.get(i).getLocation(), Track.allTracks().get(i).getLocation());
        }
    }

    @Test
    public void getStartingPointTest(){
        Track t = new Track("0","Banane -> Centre Sportif",R.drawable.centre_sportif ,Arrays.asList(new CustLatLng(46.522735, 6.579772),  new CustLatLng(46.519380, 6.580669)));

        assertEquals(new LatLng(46.522735, 6.579772), t.getStartingPoint());
    }

    @Test
    public void getLocationTest(){
        Track t = new Track("0","TestLocation",R.drawable.centre_sportif ,Arrays.asList(new CustLatLng(46.522735, 6.579772),  new CustLatLng(46.519380, 6.580669)));

        assertEquals("TestLocation", t.getLocation());
    }

    @Test
    public void getCardItemTest(){
        Track t = new Track("0","ici",R.drawable.centre_sportif ,Arrays.asList(new CustLatLng(46.522735, 6.579772),  new CustLatLng(46.519380, 6.580669)));

        assertEquals(new CardItem(R.drawable.centre_sportif, "ici", "0").getBackground(), t.getCardItem().getBackground());
        assertEquals(new CardItem(R.drawable.centre_sportif, "ici", "0").getName(), t.getCardItem().getName());
        assertEquals(new CardItem(R.drawable.centre_sportif, "ici", "0").getParentTrackID(), t.getCardItem().getParentTrackID());
    }

    @Test
    public void testConstructor(){
        Track t = new Track("0", R.drawable.centre_sportif,"TestLocation", Arrays.asList(new CustLatLng(46.522735, 6.579772),  new CustLatLng(46.519380, 6.580669)),
                            0, 0, 0, 0);

        assertEquals("0", t.getCreatorUid());
        assertEquals("TestLocation", t.getLocation());
        assertEquals(R.drawable.centre_sportif, t.getImage());
        assertEquals(0, t.getTrackLength(), 0);
        assertEquals(0, t.getAverageTimeLength(), 0);
        assertEquals(0, t.getLikes());
        assertEquals(0, t.getFavourites());
    }
}
