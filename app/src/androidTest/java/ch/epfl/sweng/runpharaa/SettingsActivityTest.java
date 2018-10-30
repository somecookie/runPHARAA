package ch.epfl.sweng.runpharaa;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    Resources r;

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(46.520566, 6.567820), "aa");
    }

    @Before
    public void init() {
        resetSharedPreferences();
        r = mActivityRule.getActivity().getResources();
    }

    @Test
    public void correctlyUpdatesPrefRadius() {
        writeTextToPreference("100", 1);
        assertTrue(User.instance.getPreferredRadius() == 100);
    }

    @Test
    public void correctlyUpdatesMinTimeInterval() {
        writeTextToPreference("2000", 4);
    }

    @Test
    public void correctlyUpdatesTimeInterval() {
        writeTextToPreference("6000", 3);
    }

    @Test
    public void correctlyUpdatesDistanceInterval() {
        writeTextToPreference("10", 5);
    }

    @Test
    public void correctlyResetsAllValues() {
        selectItemAtPos(7).perform(click());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity().getBaseContext());
        checkDefaultValueCorresponds(sp, R.string.pref_key_radius, R.string.pref_default_radius);
        checkDefaultValueCorresponds(sp, R.string.pref_key_min_distance_interval, R.string.pref_default_min_distance_interval);
        checkDefaultValueCorresponds(sp, R.string.pref_key_min_time_interval, R.string.pref_default_min_time_interval);
        checkDefaultValueCorresponds(sp, R.string.pref_key_time_interval, R.string.pref_default_time_interval);
    }

    private void checkDefaultValueCorresponds(SharedPreferences sp, int keyId, int defaultId) {
        assertTrue(sp.getString(r.getString(keyId), "").equals(r.getString(defaultId)));
    }

    private DataInteraction selectItemAtPos(int pos) {
        return onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list),
                        childAtPosition(
                                withId(android.R.id.list_container),
                                0)))
                .atPosition(pos);
    }

    // TODO: make this work, currently selecting 2 different views ??
    private void typeNewValueInPreference(int titleId, String key, String value) {
        onData(allOf(is(instanceOf(Preference.class)), withTitle(titleId), withKey(key)))
                .onChildView(withClassName(is(EditText.class.getName())))
                .perform(click()).perform(typeText(value)).perform(ViewActions.closeSoftKeyboard());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }

    public void writeTextToPreference(String newVal, int pos) {
        selectItemAtPos(pos).perform(click());
        sleep(500);
        ViewInteraction editText = onView(
                allOf(withId(android.R.id.edit),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                1)));
        editText.perform(scrollTo(), replaceText(newVal));
        sleep(500);
        ViewInteraction editText2 = onView(
                allOf(withText(newVal),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.ScrollView")),
                                                0)),
                                1),
                        isDisplayed()));
        editText2.perform(closeSoftKeyboard());
        sleep(500);
        ViewInteraction appCompatButton = onView(
                allOf(withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void resetSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity().getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}