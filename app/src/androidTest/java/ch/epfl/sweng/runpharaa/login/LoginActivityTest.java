package ch.epfl.sweng.runpharaa.login;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.User;
import ch.epfl.sweng.runpharaa.tracks.Track;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);


    @Before
    public void initUser(){
        User.instance = new User("FakeUser", 2000, null, new ArrayList<Track>(), new ArrayList<Track>(), new LatLng(21.23, 12.112), false, "aa");
    }

    @Test
    public void connectWithoutGoogleTest(){
        //To update once this is implemented
        onView(withId(R.id.sign_in_button)).perform(click());
    }

    /*@Test
    public void connectWithGoogleTest(){
        onView(withId(R.id.signInBut)).perform(click());
    }*/
}
