package ch.epfl.sweng.runpharaa;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SettingsActivityTest {
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testLogOut(){
        onView(withId(R.id.sign_out_button)).perform(click());
    }
}
