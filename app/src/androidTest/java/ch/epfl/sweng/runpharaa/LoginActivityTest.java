package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.Initializer.TestInitNoLocation;
import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void connectWithoutGoogleTest() {
        Intents.init();
        onView(withId(R.id.sign_in_button)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    /*
    @Test
    public void connectWithGoogleSuccess(){
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.sign_in_button_google)).perform(click());
        sleep(10000);
        Espresso.pressBack();
    }*/
}
