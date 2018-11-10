package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class FavoritesFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);
    private Track t;

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(46.518577, 6.563165), "aa");
    }

    @Before
    public void init() {
        Intents.init();
    }

    @Test
    public void testNoFavorites() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        mActivityRule.launchActivity(null);
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);
        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(targetContext.getResources().getString(R.string.no_favorite))));
    }

    @Test
    public void testFavoritesAppears() {
        /*LatLng points = new LatLng(46.518577, 6.563165);
        Location location = generateLocation(points);
        launchWithExtras(location);*/

        mActivityRule.launchActivity(null);
        onView(allOf(withId(R.id.refreshNearMe), isDisplayed())).perform(swipeDown());
        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());

        Espresso.pressBack();

        sleep(2000);

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);
        onView(allOf(withId(R.id.refreshNearMe), isDisplayed())).perform(swipeDown());
        sleep(5000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));

        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));
        onView(withId(R.id.buttonFavoriteID)).perform(click());
    }


    @After
    public void clean() {
        Intents.release();
    }

    /*
    private Location generateLocation(LatLng p) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(p.latitude);
        l.setLongitude(p.longitude);
        l.setAltitude(0);
        l.setAccuracy(1);
        l.setTime(System.currentTimeMillis());
        return l;
    }

    private void launchWithExtras(Location location) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, MainActivity.class);
        intent.putExtra("locations", location);
        mActivityRule.launchActivity(intent);
        sleep(5_000);
    }*/
}