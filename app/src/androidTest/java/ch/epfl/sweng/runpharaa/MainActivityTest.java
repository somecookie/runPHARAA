package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, null, new HashSet<Integer>(), new HashSet<Integer>(), new LatLng(21.23, 12.112), false, "aa");
    }

    @Test
    public void testChangeFragment() {
        onView(withId(R.id.viewPagerId)).perform(swipeDown());
        SystemClock.sleep(1000);
        onView(withId(R.id.viewPagerId)).perform(swipeLeft()).perform(swipeLeft());
    }

    @Test
    public void testOpenMaps() {
        Intents.init();
        onView(withId(R.id.mapIcon)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testOpenSettings() {
        Intents.init();
        onView(withId(R.id.settingsIcon)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testOpenCreateTrackActivity() {
        Intents.init();
        onView(withId(R.id.fab)).perform(click());
        intended(hasComponent(CreateTrackActivity.class.getName()));
        Intents.release();
    }
}
