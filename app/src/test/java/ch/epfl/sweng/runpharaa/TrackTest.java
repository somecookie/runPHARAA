package ch.epfl.sweng.runpharaa;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

public class TrackTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private Bitmap b;
    private Set<TrackType> types;
    private TrackProperties p;
    private CustLatLng coord0;
    private CustLatLng coord1;
    private List<CustLatLng> path;
    private Track legalTestTrack;

    @Before
    public void init() {
        b = Bitmap.createBitmap(200, 100, Bitmap.Config.ARGB_8888);
        types = new HashSet<>();
        types.add(TrackType.FOREST);
        p = new TrackProperties(100, 10, 1, 1, types);
        path = new ArrayList<>();

        coord0 = new CustLatLng(46.518577, 6.563165);
        coord1 = new CustLatLng(46.522735, 6.579772);
        path.add(coord0);
        path.add(coord1);

        legalTestTrack = new Track("7864", "0", "test", path , p);

    }

    @Test
    public void PathWithLessThan2PointsThrowsException() {

        List<CustLatLng> path = new ArrayList<>();
        path.add(coord0);

        exception.expect(IllegalArgumentException.class);
        new Track("8498", "0","test", path, p);

    }

    @Test
    public void startingPositionIsCorrect(){
        assert legalTestTrack.getStartingPoint().equals(coord0);
    }
}