package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class FavoritesFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, null, new ArrayList<Track>(), new ArrayList<Track>(), new LatLng(21.23, 12.112), false, "aa");
        User.instance.addToFavorites(0);
        User.instance.addToFavorites(1);
    }


    @Test
    public void testFavoritesAppears() {
        Intents.init();
        onView(withId(R.id.viewPagerId)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(click());
        intended(hasComponent(TrackPropertiesActivity.class.getName()));
        Intents.release();
    }

}
