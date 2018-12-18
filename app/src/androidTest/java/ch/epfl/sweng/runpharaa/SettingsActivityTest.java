package ch.epfl.sweng.runpharaa;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.settings.SettingsActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.onPreferenceRow;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    private Resources r;

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "1");
    }

    @Before
    public void init() {
        Context c = InstrumentationRegistry.getTargetContext();
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        resetSharedPreferences();
        Intents.init();
        r = InstrumentationRegistry.getTargetContext().getResources();
    }

    @Test
    public void deleteUserTest() {
        onView(withId(R.id.delete_account)).perform(click());
        onView(withText(R.string.cancel)).perform(click());

        onView(withId(R.id.delete_account))
                .perform(click());
        onView(withText(R.string.delete)).perform(click());

        sleep(3000);
        onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
        intended(hasComponent(LoginActivity.class.getName()));
    }
        
    public void clickOnHomeButton(){
        sleep(2000);
        //The description only works if the app is in english, there could be an issue if not
        onView(withContentDescription("Navigate up")).perform(click());
        sleep(2000);
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void updateRadius() {
        setValueToPref(R.string.pref_key_radius, "0.5");
        assertEquals(500, User.instance.getPreferredRadius());
    }

    @Test
    public void updateTimeInterval() {
        setValueToPref(R.string.pref_key_time_interval, "6");
    }

    @Test
    public void updateMinTimeInterval() {
        setValueToPref(R.string.pref_key_min_time_interval, "2");
    }

    @Test
    public void updateDistanceInterval() {
        setValueToPref(R.string.pref_key_min_distance_interval, "1");
    }

    // ---- HELPERS ----

    @Test
    public void resetsPreferences() {
        onPreferenceRow(
                withKey(r.getString(R.string.pref_key_reset_prefs)))
                .perform(click());
    }

    @Test
    public void clearCache() {
        onPreferenceRow(
                withKey(r.getString(R.string.pref_key_clear_cache)))
                .perform(click());
    }

    private void setValueToPref(int keyId, String value) {
        onPreferenceRow(
                withKey(r.getString(keyId)))
                .perform(scrollTo(), click());

        onView(allOf(withId(android.R.id.edit),
                isDisplayed()))
                .perform(replaceText(value));

        onView(allOf(withId(android.R.id.button1),
                isDisplayed()))
                .perform(click());
    }

    private void resetSharedPreferences() {
        Context c = InstrumentationRegistry.getTargetContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        PreferenceManager.setDefaultValues(c, R.xml.preferences, true);
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }
}