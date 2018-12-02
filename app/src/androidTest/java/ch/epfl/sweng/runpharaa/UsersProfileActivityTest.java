package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.user.settings.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UsersProfileActivityTest extends TestInitLocation {

    private static final int WAIT_TIME = 2000;
    @Rule
    public final ActivityTestRule<UsersProfileActivity> mActivityRule =
            new ActivityTestRule<>(UsersProfileActivity.class, false, false);

    @Before
    public void initEmptyUser() {
        User.instance = new User("Bob", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "1");
    }

    @Test
    public void testOpenSettings() {
        onView(withId(R.id.settingsIcon)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void correctlyDisplaysName() {
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.user_name)).check(matches(withText(User.instance.getName())));
    }

    @Test
    public void correctlyDisplaysNumberOfCreatedTracks() {
        User.instance.addToCreatedTracks("0");
        mActivityRule.launchActivity(new Intent());
        sleep(WAIT_TIME);
        onView(withId(R.id.nbTracks)).check(matches(withText("1")));
    }

    @Test
    public void correctlyDisplaysNumberOfFavorites() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("1");
        mActivityRule.launchActivity(new Intent());
        sleep(WAIT_TIME);
        onView(withId(R.id.nbFav)).check(matches(withText("2")));
    }

    @Test
    public void handlesSameFavoriteAddedTwice() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("0");
        mActivityRule.launchActivity(new Intent());
        sleep(WAIT_TIME);
        onView(withId(R.id.nbFav)).check(matches(withText("1")));
    }

    //TODO: move this test to the correct place
/*    @Test
    public void logoutButtonLogOut() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        Intents.release();
        onView(withId(R.id.sign_out_button)).perform(click());
        *//*
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.loggedOut)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));*//*
    }*/

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

    @Ignore
    @Test
    public void trophiesWithTenofEach(){
        mActivityRule.launchActivity(null);
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

    private void clickOnDifferenttrophies(){
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
