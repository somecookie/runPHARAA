package ch.epfl.sweng.runpharaa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

import static org.junit.Assert.assertNotNull;

public class TrackTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void teststandardDatabaseConstructor(){
        assertNotNull(new Track());
    }

    @Test
    public void testExeptionOnFireBaseConstructor(){
        List<CustLatLng> path = new ArrayList<>();
        path.add(new CustLatLng(1.0, 1.0));
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.BEACH);
        TrackProperties tp = new TrackProperties(5.0, 200, 20, 5, types);
        exception.expect(IllegalArgumentException.class);
        new Track("testUID", "testCreator", "test", path, new ArrayList<>(), tp);
    }

    @Test
    public void testExeptionOnTrackAdapterConstructor(){
        List<CustLatLng> path = new ArrayList<>();
        path.add(new CustLatLng(1.0, 1.0));
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.BEACH);
        TrackProperties tp = new TrackProperties(5.0, 200, 20, 5, types);
        FirebaseTrackAdapter track = new FirebaseTrackAdapter("testUID", "testCreator", "test", null, path, tp, new ArrayList<>());
        track.setTrackUid("UID");
        exception.expect(IllegalArgumentException.class);
        new Track(track);
    }
}
