package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import static android.support.test.espresso.action.ViewActions.swipeDown;
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
import static org.junit.Assert.fail;

public class FavoritesFragmentTest {
    private Track t;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");

    }


    @Test
    public void testNoFavorites(){
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        if(targetContext == null){
            fail("Target context null");
        }

        if(mActivityRule == null){
            fail("activity null");
        }
        mActivityRule.launchActivity(null);

        if(onView(withId(R.id.viewPagerId)) == null){
            fail("ID null");
        }

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);

        if(onView(allOf(withId(R.id.emptyMessage), isDisplayed())) == null){
            fail("truc null");
        }

        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(targetContext.getResources().getString(R.string.no_favorite))));
    }


    /*
    @Test
    public void testFavoritesAppears() {
        mActivityRule.launchActivity(null);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());

        Espresso.pressBack();

        sleep(2000);

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                swipeDown());

        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));

        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));

        onView(withId(R.id.buttonFavoriteID)).perform(click());

    }*/

}