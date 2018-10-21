package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.utils.Util;

public class TrackTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private Bitmap b;
    private Set<TrackType> types;
    private TrackProperties p;
    private LatLng coord0;
    private LatLng coord1;
    private LatLng[] path;
    private Track legalTestTrack;

    @Before
    public void init() {
        b = Util.createImage(200, 100, R.color.colorPrimary);
        types = new HashSet<>();
        types.add(TrackType.FOREST);
        p = new TrackProperties(100, 10, 1, 1, types);

        coord0 = new LatLng(46.518577, 6.563165);
        coord1 = new LatLng(46.522735, 6.579772);

        path = new LatLng[]{coord0, coord1};

        legalTestTrack = new Track("7864", "Bob", b, "test", path , p);
    }

    @Test
    public void PathWithLessThan2PointsThrowsException() {

        LatLng[] path = {coord0};

        exception.expect(IllegalArgumentException.class);
        new Track("8498", "Bob", b, "test", path, p);

    }

    @Test
    public void startingPositionIsCorrect(){
        assert legalTestTrack.getStartingPoint().equals(coord0);
    }
}
