package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UsersProfileActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<UsersProfileActivity> mActivityRule =
            new ActivityTestRule<>(UsersProfileActivity.class, true, false);

    @Before
    public void initEmptyUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "FakeUser");
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
        sleep(500);
        onView(withId(R.id.nbTracks)).check(matches(withText("1")));
    }
    
    @Test
    public void correctlyDisplaysNumberOfFavorites() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("1");
        mActivityRule.launchActivity(new Intent());
        sleep(500);
        onView(withId(R.id.nbFav)).check(matches(withText("2")));
    }

    @Test
    public void handlesSameFavoriteAddedTwice() {
        User.instance.addToFavorites("0");
        User.instance.addToFavorites("0");
        mActivityRule.launchActivity(new Intent());
        sleep(500);
        onView(withId(R.id.nbFav)).check(matches(withText("1")));
    }

    @Test
    public void logoutButtonLogOut() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        Intents.release();
        onView(withId(R.id.sign_out_button)).perform(click());
        /*
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.loggedOut)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));*/
    }
}
