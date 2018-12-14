package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.util.TestInitNoLocation;

import static org.junit.Assert.assertEquals;

public class TrackDatabaseManagementTest extends TestInitNoLocation {

    @Rule
    public ActivityTestRule<CreateTrackActivity2> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity2.class, true, false);

    @Before
    public void initUser() {
        User.instance = new User("Bob", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "1");
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
    }

    //-----------------TESTS-------------------------


    @Test
    public void pushTrackToDatabaseWithoutError(){
        List<CustLatLng> path = new ArrayList<>(Arrays.asList(new CustLatLng(1.0,1.0), new CustLatLng(1.0, 0.0)));
        Set<TrackType> types = new HashSet<>(Collections.singleton(TrackType.BEACH));
        TrackProperties tp = new TrackProperties(5.0, 200, 20, 5, types);

        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);

        FirebaseTrackAdapter t = new FirebaseTrackAdapter("A", "1", "Bob", bitmap, path, tp, new ArrayList<>());

        TrackDatabaseManagement.writeNewTrack(t);

        assertEquals(User.instance.getCreatedTracks().size(), 1);

    }
}
