package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class TrackTest {

    LatLng coord1 = new LatLng(46.518577, 6.563165); //inm
    LatLng coord2 = new LatLng(46.519380, 6.580669); //centre sportif

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

        Track tr1 = new Track(path1);
        Track tr2 = new Track(path2);

        assert(tr1.distance(coord2) == tr2.distance(coord1));
    }
}
