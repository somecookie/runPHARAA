package ch.epfl.sweng.runpharaa;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.user.StreakManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.setGone;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.setProgress;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.swipeToFragmentSearch;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.testToastDisplay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
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
        User.instance = Database.getUser();
        Calendar fakeCalendar = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(fakeCalendar);
        User.setStreakManager(new StreakManager());
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
        swipeToFragmentSearch();
        sleep(2000);
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
    public void testOpenFilters() {
        onView(withId(R.id.filterIcon)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOpenAndCloseHelp() {
        // display the popup
        onView(withId(R.id.helpIcon)).perform(click());
        // dismiss the popup by clicking on it
        onView(withContentDescription(R.string.popup_description))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        // check that the main activity is visible by checking that its button and tabs are displayed
        onView(withId(R.id.profileIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.tabLayoutId)).check(matches(isDisplayed()));
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

    @Test
    public void testUnsuccessfulUserSearch(){
        swipeToFragmentSearch();
        sleep(1000);
        searchFor("travis scott", true);
        sleep(500);
        String expected = String.format(mActivityRule.getActivity().getResources().getString(R.string.no_user_found), "travis scott");
        onView(withText(expected))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulUserSearch(){
        swipeToFragmentSearch();
        sleep(1000);
        searchFor("Bob", true);
        sleep(4000);
        onView(withId(R.id.user_name)).check(matches(withText("Bob")));
    }

    @Test
    public void testSuccessfulTrackSearch(){
        swipeToFragmentSearch();
        // click on the toggleButton
        onView(withId(R.id.toggle_button)).perform(click());
        sleep(1000);
        searchFor("Cours forest !", true);
        sleep(4000);
        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));
    }

    @Test
    public void testUnsuccessfulTrackSearch(){
        swipeToFragmentSearch();
        // click on the toggleButton
        onView(withId(R.id.toggle_button)).perform(click());
        sleep(1000);
        searchFor("GradleBuildRunning", true);
        sleep(1000);
        String expected = String.format(mActivityRule.getActivity().getResources().getString(R.string.no_track_found), "GradleBuildRunning");
        onView(withText(expected))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessfulLuckyButtonOnNoTrackNearMe(){
        swipeToFragmentSearch();

        sleep(1000);

        onView(withId(R.id.luckyIcon)).perform(click());
        testToastDisplay(mActivityRule, mActivityRule.getActivity().getResources().getString(R.string.no_tracks));
    }

    @Test
    public void testSuccessfulLuckyButtonOnNoLikesFavs(){
        swipeToFragmentSearch();

        sleep(1000);

        //User near track
        User.instance.setLocation(new LatLng(37.422, -122.084));
        onView(withId(R.id.luckyIcon)).perform(click());
        testToastDisplay(mActivityRule, mActivityRule.getActivity().getResources().getString(R.string.no_favorites_and_likes));
        intended(hasComponent(TrackPropertiesActivity.class.getName()));
    }


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
        int heightDiff = 10;
        FirebaseTrackAdapter track = new FirebaseTrackAdapter("Cours forest !", "0", "BobUID", "Bob", Arrays.asList(coord0, coord1), "imageUri",
                types, length, heightDiff, 2, 1, 40, 1, 0, 0,new ArrayList<>() );
        Track t = new Track(track);

        assertTrue(MainActivity.passFilters(t));
        MainActivity.typesFilter.remove(TrackType.FOREST);
        MainActivity.typesFilter.add(TrackType.CITY);
        assertFalse(MainActivity.passFilters(t));

        MainActivity.difficultyIsFiltered = false;
        assertFalse(MainActivity.passFilters(t));

        MainActivity.typesAreFiltered = false;
        assertTrue(MainActivity.passFilters(t));

        MainActivity.difficultyIsFiltered = true;
        assertTrue(MainActivity.passFilters(t));

        MainActivity.timeIsFiltered = false;
        assertTrue(MainActivity.passFilters(t));

        MainActivity.difficultyIsFiltered = true;
        MainActivity.typesAreFiltered = true;
        assertFalse(MainActivity.passFilters(t));

        MainActivity.typesAreFiltered = false;
        assertTrue(MainActivity.passFilters(t));

        MainActivity.difficultyIsFiltered = false;
        MainActivity.typesAreFiltered = true;
        assertFalse(MainActivity.passFilters(t));
    }

    @Test
    public void databaseErrorNearMe() throws Throwable {
        Database.setIsCancelled(true);

        runOnUiThread(() ->((FragmentNearMe)this.mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0)).onRefresh());

        //Tried to get the string but it did not work, put a hardcoded string for now
        onView(AllOf.allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(R.string.no_tracks)));

        Database.setIsCancelled(false);
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

    private void searchFor(String text, boolean submit) {
        try {
            FragmentSearch fragment = (FragmentSearch) mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(3);
            MenuItem item = fragment.getMenu().findItem(R.id.searchIcon);
            SearchView sv = (SearchView) item.getActionView();
            runOnUiThread(() -> sv.setQuery(text, submit));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
