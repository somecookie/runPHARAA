package ch.epfl.sweng.runpharaa;

import android.net.Uri;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends TestInitLocation {

    private static final int WAIT_TIME = 1000;
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(46.518577, 6.563165), "aa");
    }

    private static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    private static ViewAction setGone() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return is(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(View.GONE);
            }

            @Override
            public String getDescription() {
                return "Hide View";
            }
        };
    }

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testChangeFragment() {
        onView(withId(R.id.viewPagerId)).perform(swipeDown());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(3000);
    }

    @Test
    public void testOpenMaps() {
        onView(withId(R.id.mapIcon)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));

    }

    @Test
    public void testOpenProfile() {
        onView(withId(R.id.profileIcon)).perform(click());
        intended(hasComponent(UsersProfileActivity.class.getName()));
    }

    @Test
    public void testOpenSettings() {
        onView(withId(R.id.settingsIcon)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void testOpenFilters() {
        onView(withId(R.id.filterIcon)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSetUpFilters() {
        onView(withId(R.id.filterIcon)).perform(click());
        onView(withId(R.id.timeBar)).perform(setProgress(100));
        onView(withId(R.id.difficulty_bar)).perform(setProgress(1));
        selectAllTypes(true);
        assertTrue(MainActivity.typesAreFiltered);
        assertTrue(MainActivity.timeIsFiltered);
        assertTrue(MainActivity.difficultyIsFiltered);
        assertEquals(MainActivity.typesFilter.size(), TrackType.values().length);
        assertEquals(MainActivity.timeFilter, 100);
        assertEquals(MainActivity.difficultyFilter, 1);
    }

    @Test
    public void testSetUpNoFilters() {
        onView(withId(R.id.filterIcon)).perform(click());
        onView(withId(R.id.timeBar)).perform(setProgress(100));
        onView(withId(R.id.difficulty_bar)).perform(setProgress(1));
        selectAllTypes(false);
        assertFalse(MainActivity.typesAreFiltered);
        assertFalse(MainActivity.timeIsFiltered);
        assertFalse(MainActivity.difficultyIsFiltered);
    }

    /*@Test
    public void testOpenCreateTrackActivity() {
        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(CreateTrackActivity.class.getName()));
    }*/

    @Test
    public void testMaxTimeFilter() {
        onView(withId(R.id.filterIcon)).perform(click());
        onView(withId(R.id.timeBar)).perform(setProgress(120));
        onView(withId(R.id.maxTime)).check(matches(withText(R.string.no_time_limit)));
        onView(withId(R.id.timeBar)).perform(setProgress(114));
        onView(withId(R.id.maxTime)).check(matches(withText(mActivityRule.getActivity().getResources().getString(R.string.max_time_of) + " " + 114 + " minutes.")));
        onView(withId(R.id.timeBar)).perform(setProgress(115));
        onView(withId(R.id.maxTime)).check(matches(withText(R.string.no_time_limit)));

        onView(withId(R.id.scroll)).perform(setGone());
        sleep(WAIT_TIME);

        onView(allOf(withId(android.R.id.button1),
                isDisplayed()))
                .perform(click());
        assertFalse(MainActivity.timeIsFiltered);
    }

    @Test
    public void testFilterMethod() {
        //Set up
        MainActivity.timeIsFiltered = true;
        MainActivity.difficultyIsFiltered = true;
        MainActivity.typesAreFiltered = true;
        MainActivity.timeFilter = 60;
        MainActivity.difficultyFilter = 3;
        MainActivity.typesFilter.add(TrackType.FOREST);
        List<String> types = new ArrayList<>();
        types.add(TrackType.FOREST.toString());
        CustLatLng coord0 = new CustLatLng(37.422, -122.084);
        CustLatLng coord1 = new CustLatLng(37.425, -122.082);
        int length = 100;
        int heigthDiff = 10;
        FirebaseTrackAdapter track = new FirebaseTrackAdapter("Cours forest !", "0", "BobUID", "Bob", Arrays.asList(coord0, coord1), "imageUri",
                types, length, heigthDiff, 1, 1, 1, 1, 0, 0);
        Track t = new Track(track);
        assertTrue(MainActivity.passFilters(t));
        MainActivity.typesFilter.remove(TrackType.FOREST);
        MainActivity.typesFilter.add(TrackType.CITY);
        assertFalse(MainActivity.passFilters(t));
    }

    private void selectAllTypes(boolean pressOk) {
        for (int i = 0; i < TrackType.values().length; ++i) {
            onView(withText(mActivityRule.getActivity().getResources().getStringArray(R.array.track_types)[i]))
                    .perform(click())
                    .perform(click())
                    .perform(click());
        }

        onView(withId(R.id.scroll)).inRoot(isDialog()).perform(setGone());

        sleep(WAIT_TIME);
        onView(allOf(withId(pressOk ? android.R.id.button1 : android.R.id.button2),
                isDisplayed()))
                .perform(click());
        sleep(WAIT_TIME);
    }
}