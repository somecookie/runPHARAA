package ch.epfl.sweng.runpharaa;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);
    Resources r;

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");
    }

    private static Matcher<View> withResName(final String resName) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with res-name: " + resName);
            }

            @Override
            public boolean matchesSafely(View view) {
                int identifier = view.getResources().getIdentifier(resName, "id", "android");
                return !TextUtils.isEmpty(resName) && (view.getId() == identifier);
            }
        };
    }

    private static DataInteraction onPreferenceRow(Matcher<? extends Object> datamatcher) {

        DataInteraction interaction = onData(datamatcher);

        return interaction
                .inAdapterView(allOf(
                        withId(android.R.id.list),
                        not(withParent(withResName("headers")))));
    }

    @Before
    public void init() {
        resetSharedPreferences();
        r = InstrumentationRegistry.getTargetContext().getResources();
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
                /*withTitle(R.string.pref_title_radius)))*/
                .perform(click());
    }

    @Test
    public void clearCache() {
        onPreferenceRow(
                withKey(r.getString(R.string.pref_key_clear_cache)))
                /*withTitle(R.string.pref_title_radius)))*/
                .perform(click());
    }

    private void setValueToPref(int keyId, String value) {
        onPreferenceRow(
                withKey(r.getString(keyId)))
                /*withTitle(R.string.pref_title_radius)))*/
                .perform(click());

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
}