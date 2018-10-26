package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;

import ch.epfl.sweng.runpharaa.Users.Profile.UsersProfileActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UsersProfileActivityTest {

    @Rule
    public final ActivityTestRule<UsersProfileActivity> mActivityRule =
            new ActivityTestRule<>(UsersProfileActivity.class, true, false);

    @BeforeClass
    public static void initEmptyUser() {
        Uri path = Uri.parse("android.resource://ch.epfl.sweng.runpharaa/" + R.drawable.default_photo);
        User.instance = new User("FakeUser", 2000, null, new ArrayList<String>(), new ArrayList<String>(), new LatLng(21.23, 12.112), false, "aa");
    }

    @Test
    public void correctlyDisplaysName() {
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.user_name)).check(matches(withText("FakeUser")));
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
}
