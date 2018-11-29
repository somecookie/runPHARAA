package ch.epfl.sweng.runpharaa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.FilterProperties;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

import static org.junit.Assert.assertEquals;

public class FilterPropertiesTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void addWithBadArgumentsTest(){
        FilterProperties f = new FilterProperties();
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.CITY);
        exception.expect(IllegalArgumentException.class);
        f.add(-1, 1, 1, types);
        exception.expect(IllegalArgumentException.class);
        f.add(1, -1, 1, types);
        exception.expect(IllegalArgumentException.class);
        f.add(1, 1, -1, types);
        exception.expect(NullPointerException.class);
        types.clear();
        f.add(1, -1, 1, types);
    }

    @Test
    public void chooseLuckyTrackTest(){
        FilterProperties filterProperties = new FilterProperties();
        Set<TrackType> tt = new HashSet<>();
        tt.add(TrackType.SEASIDE);
        TrackProperties tp1 = new TrackProperties(5000, 0, 30, 2, tt);
        Set<TrackType> tt2 = new HashSet<>();
        tt2.add(TrackType.SEASIDE);
        TrackProperties tp2 = new TrackProperties(5000, 0, 30, 2, tt2);
        List<CustLatLng> path = Arrays.asList(new CustLatLng(0.,0.), new CustLatLng(0.,0.));
        Track t1 = new Track("t1", "t1UDI", "t1Creator", path, new ArrayList<>(), tp1);
        Track t2 = new Track("t2", "t2UDI", "t2Creator", path, new ArrayList<>(), tp2);
        List<Track> favs = Arrays.asList(t1, t2);
        filterProperties.add(favs);
        Set<TrackType> tt3 = new HashSet<>();
        tt3.add(TrackType.FOREST);
        TrackProperties tp3 = new TrackProperties(5000, 0, 30, 2, tt3);
        Track tn1 = new Track("tn1", "tn1UDI", "tn1Creator", path, new ArrayList<>(), tp3);
        Set<TrackType> tt4 = new HashSet<>();
        tt4.add(TrackType.SEASIDE);
        TrackProperties tp4 = new TrackProperties(5000, 0, 30, 2, tt4);
        Track tn2 = new Track("tn2", "tn2UDI", "tn2Creator", path, new ArrayList<>(), tp4);
        List<Track> nearMe = Arrays.asList(tn1, tn2);
        Track lucky = filterProperties.chooseLuckyTrack(nearMe);
        assertEquals(lucky.getTrackUid(), tn2.getTrackUid());
    }
}
