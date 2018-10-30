package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.Firebase.Database;
import ch.epfl.sweng.runpharaa.database.DatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

public class FavoritesFragmentTest {
    private Track t;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void init() {
        Intents.init();
    }

    @Before
    public void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");

    }


    @Test
    public void testFavoritesAppears() {
        createTrack();
        //User.instance.addToFavorites("0");
        //User.instance.addToFavorites("1");
        DatabaseManagement.writeNewTrack(t);
        sleep(5000);
        onView(withId(R.id.viewPagerId)).perform(swipeUp());
        sleep(5000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());

        sleep(5000);

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(5_000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        intended(hasComponent(TrackPropertiesActivity.class.getName()));
    }

    @Test
    public void testNoFavorites(){
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);
        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(targetContext.getResources().getString(R.string.no_favorite))));
    }

    @After
    public void clean() {
        Intents.release();
    }

    private void createTrack() {
        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        CustLatLng coord0 = new CustLatLng(46.518577, 6.563165); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track track = new Track("0", "Bob", b, "Cours forest !", Arrays.asList(coord0, coord1, coord2), p);

        t = track;

    }
}