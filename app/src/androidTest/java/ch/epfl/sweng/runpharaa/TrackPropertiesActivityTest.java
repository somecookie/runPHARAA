package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TrackPropertiesActivityTest {

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000,  Uri.parse(""), new LatLng(37.422, -122.084), "aa");
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void initUserAndTracks() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");
    }

    @Test
    public void correctValuesDisplayedForTrack1() {
        Track t1 = createTrack();
        mActivityRule.launchActivity(null);
        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        withId(R.id.trackTitleID).matches(withText(t1.getName()));
        withId(R.id.trackLengthID).matches(withText("Length: " + Double.toString(t1.getProperties().getLength()) + "m"));
        withId(R.id.trackLikesID).matches(withText("Likes: " + t1.getProperties().getLikes()));
    }

    @Test
    public void testLike() {
        mActivityRule.launchActivity(null);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("1"));

    }

    @Test
    public void testFavourite() {
        mActivityRule.launchActivity(null);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("1"));
    }

    public void pressingLikeUpdatesValue() {
        mActivityRule.launchActivity(null);
        int likesBefore = createTrack().getProperties().getLikes();
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("Likes: " + likesBefore + 1));
        sleep(500);
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("Likes: " + likesBefore));
    }

    @Test
    public void addingToFavoritesUpdatesValue() {
        mActivityRule.launchActivity(null);
        int favsBefore = createTrack().getProperties().getLikes();
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Likes: " + favsBefore + 1));
        sleep(500);
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Likes: " + favsBefore));
    }

    @Test
    public void addingToFavoritesUpdatesUser() {
        mActivityRule.launchActivity(null);
        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        sleep(5000);
        assertTrue(User.instance.getFavoriteTracks().contains("0"));
    }

    private void launchWithTrackId(String id) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, TrackPropertiesActivity.class);
        intent.putExtra("TrackID", id);
        mActivityRule.launchActivity(intent);
    }

    private Track createTrack() {
        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        CustLatLng coord0 = new CustLatLng(37.422, -122.084); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);
        Track track = new Track("0", "Bob", "Cours forest !", Arrays.asList(coord0, coord1, coord2), p);

        return track;

    }

}



