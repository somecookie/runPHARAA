package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
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

public class FavoritesFragmentTest extends TestInitLocation {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "1");

    }

    private Track t;

    @Test
    public void testNoFavorites(){
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                swipeDown());

        sleep(2000);

        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(targetContext.getResources().getString(R.string.no_favorite))));
    }

    @Ignore
    @Test
    public void testFavoritesAppears() {
        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                swipeDown());
        sleep(5000);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.buttonFavoriteID)).perform(click());

        Espresso.pressBack();

        sleep(2000);

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        List<String> fav = new ArrayList<>();
        fav.add("0");
        User.instance.setFavoriteTracks(fav);
        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                swipeDown());

        sleep(2000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));

        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));

        User.instance.setFavoriteTracks(new ArrayList<>());

        onView(withId(R.id.buttonFavoriteID)).perform(click());

    }
}