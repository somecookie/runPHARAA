package ch.epfl.sweng.runpharaa.login;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);
    

    @Test
    public void connectWithGoogle() {
        //TODO: test connection with google account
        /*Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.sign_in_button_google)).perform(click());*/
        assertTrue(true);
    }
}
