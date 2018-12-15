package ch.epfl.sweng.runpharaa;

import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.StreakManager;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.FragmentMyTracks;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class UsersProfileActivityTest extends TestInitLocation {

    private static final int WAIT_TIME = 2000;
    @Rule
    public final ActivityTestRule<UsersProfileActivity> mActivityRule =
            new ActivityTestRule<>(UsersProfileActivity.class, false, false);

    @Before
    public void initEmptyUser() {
        User.instance = new User("Bob", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "1");
        Intents.init();
        Calendar fakeCalendar = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(fakeCalendar);
        User.setStreakManager(new StreakManager());
    }

    @Test
    public void showsCorrectInitialStreakValue() {
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        onView(withId(R.id.current_streak)).check(matches(withText("1")));
    }

    @Test
    public void showsCorrectStreakValueForMultipleDays() {
        Calendar myFakeCalendar = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(myFakeCalendar);
        StreakManager sm = new StreakManager();
        User.setStreakManager(sm);
        myFakeCalendar.set(2018, Calendar.DECEMBER, 25);
        sm.update();
        myFakeCalendar.set(2018, Calendar.DECEMBER, 26);
        sm.update();
        myFakeCalendar.set(2018, Calendar.DECEMBER, 27);
        sm.update();
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        onView(withId(R.id.current_streak)).check(matches(withText("4")));
    }

    @Test
    public void showsCreatedTracks() throws Throwable {
        User.instance.addToCreatedTracks("0");
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        runOnUiThread(() ->((FragmentMyTracks)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0)).onRefresh());
        sleep(WAIT_TIME);
        onView(AllOf.allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        sleep(WAIT_TIME);
        onView(withId(R.id.trackTitleID)).check(matches(withText("Cours forest !")));
    }

    @Test
    public void showsEmptyMessageWhenNoTracks() throws Throwable {
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        runOnUiThread(() ->((FragmentMyTracks)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0)).onRefresh());
        sleep(500);
        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(R.string.no_created_self)));
    }

    @Test
    public void testOpenSettings() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.settingsIcon)).perform(click());
        sleep(WAIT_TIME);
        // can't access intent if the view isn't initialized, so we wait until we see the preferences
        onView(isRoot()).check(matches(isDisplayed()));
        //intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void correctlyDisplaysName() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.user_name)).check(matches(withText(User.instance.getName())));
    }

    @Test
    public void correctlyDisplaysNumberOfCreatedTracks() {
        User.instance.addToCreatedTracks("0");
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        onView(withId(R.id.nbTracks)).check(matches(withText("1")));
    }

    @Test
    public void correctlyDisplaysNumberOfFavorites() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("1");
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        onView(withId(R.id.nbFav)).check(matches(withText("2")));
    }

    @Test
    public void handlesSameFavoriteAddedTwice() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("0");
        mActivityRule.launchActivity(null);
        sleep(WAIT_TIME);
        onView(withId(R.id.nbFav)).check(matches(withText("1")));
    }

    @Test
    public void logoutButtonLogOut() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.signOutIcon)).perform(click());
        sleep(WAIT_TIME);
        // can't access intent if the view isn't initialized, so we wait until we see the sign in button
        onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void canClickOnDifferentTrophiesCategories(){
        mActivityRule.launchActivity(null);
        onView(withId(R.id.viewPagerUser)).perform(swipeLeft());
        sleep(2000);
        clickOnDifferenttrophies();
    }

    @Test
    public void trophiesWithOneofEach(){
        User.instance.addToCreatedTracks("0");
        User.instance.like("track");
        User.instance.addToFavorites("fav");

        mActivityRule.launchActivity(null);
        onView(withId(R.id.viewPagerUser)).perform(swipeLeft());
        sleep(2000);

        clickOnDifferenttrophies();
    }

    @Test
    public void trophiesWithTwoofEach(){
        User.instance.addToCreatedTracks("0");
        User.instance.addToCreatedTracks("1");
        User.instance.like("track");
        User.instance.like("track2");
        User.instance.addToFavorites("fav");
        User.instance.addToFavorites("fav2");
        mActivityRule.launchActivity(null);

        onView(withId(R.id.viewPagerUser)).perform(swipeLeft());
        sleep(2000);


        clickOnDifferenttrophies();
    }

    @Test
    public void trophiesWithTenofEach(){
        for(int i = 0; i < 13; i++){
            User.instance.addToCreatedTracks(String.valueOf(i));
            User.instance.like(String.valueOf(i));
            User.instance.addToFavorites(String.valueOf(i));
        }
        mActivityRule.launchActivity(null);

        onView(withId(R.id.viewPagerUser)).perform(swipeLeft());
        sleep(2000);

        clickOnDifferenttrophies();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    private void clickOnDifferenttrophies() {
        onView(withId(R.id.trophies_create)).perform(click());
        sleep(WAIT_TIME);
        Espresso.pressBack();
        onView(withId(R.id.trophies_like)).perform(click());
        sleep(WAIT_TIME);
        Espresso.pressBack();
        onView(withId(R.id.trophies_favorite)).perform(click());
        sleep(WAIT_TIME);
        Espresso.pressBack();
    }
}
