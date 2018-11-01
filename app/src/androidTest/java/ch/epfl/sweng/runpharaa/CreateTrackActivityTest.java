package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.runpharaa.user.User;

import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class CreateTrackActivityTest {

    @Rule
    public final ActivityTestRule<CreateTrackActivity> mActivityRule = new ActivityTestRule<>(CreateTrackActivity.class, true, false);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
    }

    @Before
    public void initUsers() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
    }

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void staysOnCreateTrackActivity() throws TimeoutException {
        mActivityRule.launchActivity(null);
        /*Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent i = new Intent(targetContext, GpsService.class);
        mServiceRule.startService(i);
        sleep(1000);
        Intent intent = new Intent(targetContext, CreateTrackActivity.class);
        mActivityRule.launchActivity(intent);
        initUser();
        sleep(3000);
        onView(withId(R.id.start_create_button)).perform(click());*/
    }

}
