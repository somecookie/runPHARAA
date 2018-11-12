package ch.epfl.sweng.runpharaa;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * Tests the RealGpsService Service to see if it launches / doesn't launch at the right time
 */
@RunWith(AndroidJUnit4.class)
public class GpsServiceTest extends TestInitLocation {

    @BeforeClass
    public static void initUser(){
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
    }

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void launchesRealServiceOnMainActivity() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
        mActivityRule.getActivity().startService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
        sleep(1000);
        assertTrue(isMyServiceRunning(RealGpsService.class));
    }

    @Test
    public void launchesFakeServiceOnMainActivity() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa", FakeGpsService.SAT);
        mActivityRule.getActivity().startService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
        sleep(1000);
        assertTrue(isMyServiceRunning(FakeGpsService.class));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}