package ch.epfl.sweng.runpharaa.login;

import android.Manifest;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void connectWithoutGoogleTest() {
        Intents.init();
        onView(withId(R.id.sign_in_button)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    // TODO: THIS TEST PROBABLY CAUSED THE ROOT FOCUSED ERROR
    /*@Test
    public void connectWithGoogleSuccess(){
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.sign_in_button_google)).perform(click());
    }*/
}
