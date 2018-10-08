package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivity2Test {

    @Rule
    public final ActivityTestRule<CreateTrackActivity2> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity2.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void addNameAndConfirmTest(){
        //onView(withId(R.id.create_text_name)).perform(typeText("Test track")).perform(closeSoftKeyboard());
        //onView(withId(R.id.create_track_button)).perform(click());
    }
}
