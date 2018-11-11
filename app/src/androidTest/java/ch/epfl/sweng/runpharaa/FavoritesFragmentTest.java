package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.fail;

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
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");

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