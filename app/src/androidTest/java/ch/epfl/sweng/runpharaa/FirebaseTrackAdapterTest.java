package ch.epfl.sweng.runpharaa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackType;
import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class FirebaseTrackAdapterTest {

    private List<LatLngAdapter> path = new ArrayList<>(Arrays.asList(new LatLngAdapter(1.0,1.0), new LatLngAdapter(1.0, 0.0)));
    private Set<TrackType> types = new HashSet<>(Collections.singleton(TrackType.BEACH));
    private TrackProperties tp = new TrackProperties(5.0, 200, 20, 5, types);

    @Test(expected = NullPointerException.class)
    public void failOnNullName() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter(null, null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void failOnNullCreatorId() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void failOnNullCreatorName() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void failOnNullPath() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", "Bob", null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void failOnNullProperties() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", "Bob", null, path , null, null);
    }

    @Test(expected = NullPointerException.class)
    public void failOnNullComments() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", "Bob", null, path, tp, null);
    }

    @Test
    public void createsInstanceWithCorrectInputs() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", "Bob", null, path, tp, new ArrayList<>());
        assertNull(t.getImage());
        assertEquals(t.getCreatorName(), "Bob");
        t.setTrackUid("0");
        assertEquals(t.getTrackUid(), "0");
        t.setImageStorageUri("someuri.com");
        assertEquals(t.getImageStorageUri(), "someuri.com");
        t.setComments(new ArrayList<>());
        assertTrue(t.getComments().isEmpty());
    }

   @Test(expected = NullPointerException.class)
    public void failOnNullIsDeleted(){
        new FirebaseTrackAdapter(new Track("A", "b", "c", path, new ArrayList<>(), tp), null);
   }

    @Test
    public void createsInstanceWithCorrectInputs2() {
        FirebaseTrackAdapter t = new FirebaseTrackAdapter(new Track("A", "Bob", "c", path, new ArrayList<>(), tp), false);
        assertEquals(t.getIsDeleted(), false);
    }
}
