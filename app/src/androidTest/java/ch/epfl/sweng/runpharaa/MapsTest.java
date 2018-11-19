package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class MapsTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @BeforeClass
    public static void initUser() {
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");
    }


    @Test
    public void testIfMapLoads() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.mapIcon)).perform(click());
        sleep(5_000);
        onView(withId(R.id.maps_test_text)).check(matches(withText("ready")));
    }


    @Test
    public void clickOnMarkerWorks() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.mapIcon)).perform(click());
        sleep(5_000);

        UiDevice device = UiDevice.getInstance(getInstrumentation());

        UiObject marker = device.findObject(new UiSelector().descriptionContains("Cours forest !"));

        if(marker == null){
            fail("Null marker");
        }


        try{
            Log.i("WESHHHHH", marker.getText());

        } catch (UiObjectNotFoundException e){
            fail("cant get text");
        }

        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Failed to click on marker 1");
        }

        /*
        try {
            int XMarker = marker.getBounds().centerX() + 50;
            int YMarker = marker.getBounds().centerY() + 50;
            device.click(XMarker,YMarker);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Cannot get bound of marker");
        }*/

        int x = 0;
        try {
            x = marker.getBounds().centerX();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Failed to get X of marker");
        }
        int y = 0;
        try {
            y = marker.getBounds().centerY();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("failed to get Y of marker");
        }
        device.click(x, y-100);
        sleep(500);
        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));
    }

    @Test
    public void changeMapLocation(){
        mActivityRule.launchActivity(null);
        onView(withId(R.id.mapIcon)).perform(click());
        sleep(5000);

        onView(withId(R.id.map)).perform(swipeDown());
        sleep(5000);
        onView(withId(R.id.map)).perform(longClick());
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("selected Position"));
        try{
            marker.click();
            int x = marker.getBounds().centerX();
            int y = marker.getBounds().centerY();
            device.click(x, y-50);
            sleep(500);
            marker = device.findObject(new UiSelector().descriptionContains("Cours forest !"));
            assertTrue(marker != null);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            fail("Couldn't find marker");
        }
    }



    @AfterClass
    public static void cleanUp() {
        CustLatLng coord0 = new CustLatLng(46.518577, 6.563165); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track t = new Track("0", "Bob", "Cours forest !", Arrays.asList(coord0, coord1, coord2), p);

        ArrayList<Track> all = new ArrayList<>();
        all.add(t);

        Track.allTracks = all;
    }
}
