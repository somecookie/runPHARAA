package ch.epfl.sweng.runpharaa.login;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.CreateTrackActivity;
import ch.epfl.sweng.runpharaa.R;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);



    @Test
    public void connectWithoutGoogleTest(){
        //To update once this is implemented
        onView(withId(R.id.sign_in_button)).perform(click());
    }

    @Test
    public void connectWithGoogleTest(){
        onView(withId(R.id.signInBut)).perform(click());
    }
}
