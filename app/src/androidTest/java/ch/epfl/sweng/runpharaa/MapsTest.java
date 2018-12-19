package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;
import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MapsTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @BeforeClass
    public static void initUser() {
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "1");
    }

    @Test
    public void testIfMapLoads() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.mapIcon)).perform(click());
        sleep(2000);
        onView(withId(R.id.maps_test_text)).check(matches(withText("ready")));
    }

    @AfterClass
    public static void cleanUp() {
        LatLngAdapter coord0 = new LatLngAdapter(46.518577, 6.563165); //inm
        LatLngAdapter coord1 = new LatLngAdapter(46.522735, 6.579772); //Banane
        LatLngAdapter coord2 = new LatLngAdapter(46.519380, 6.580669); //centre sportif
        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track t = new Track("0", "Bob", "Cours forest !", Arrays.asList(coord0, coord1, coord2),new ArrayList<>(), p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        Track.allTracks = all;
    }
}
