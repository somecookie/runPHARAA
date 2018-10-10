package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests the CreateTrack activity (first part of creation)
 */
@RunWith(AndroidJUnit4.class)
public class CreateTrackActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void buttonAppearsCorrectly() {
        onView(withId(R.id.fab)).perform(click());
        // Let map load
        sleep(5000);
        createButtonTextMatches("START");
    }

    /*@Test
    public void buttonWorksCorrectly() {
        onView(withId(R.id.fab)).perform(click());
        // Let map load
        sleep(5000);
        onView(withId(R.id.start_create_button)).perform(scrollTo()).perform(click());
        createButtonTextMatches("STOP");
        onView(withId(R.id.start_create_button)).perform(scrollTo()).perform(click());
        onView(withText("You need at least 2 points to create a track !")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }*/

    private void createButtonTextMatches(String text) {
        onView(withId(R.id.start_create_button)).check(matches(withText(text)));
    }

    /*@Test
    public void handleClickStartCorrectly(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.start_create_button)).perform(click());
        //sleep(5000);
        //onView(withId(R.id.start_create_button)).perform(click());
    }*/

}