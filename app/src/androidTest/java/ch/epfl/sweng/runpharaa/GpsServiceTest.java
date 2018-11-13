package ch.epfl.sweng.runpharaa;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the RealGpsService Service to see if it launches / doesn't launch at the right time
 */
@RunWith(AndroidJUnit4.class)
public class GpsServiceTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
    }

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

    @Test
    public void getLocationFromGps() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
        mActivityRule.getActivity().startService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
        sleep(3000);
        assertTrue(User.instance.getService().getCurrentLocation() != null);
    }

    @Test
    public void setNewLocationFailsOnGps() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa");
        mActivityRule.getActivity().startService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
        sleep(1000);
        Location old = User.instance.getService().getCurrentLocation();
        User.instance.getService().setNewLocation(mActivityRule.getActivity().getBaseContext(), Util.locationFromLatLng(new LatLng(0,0)));
        assertEquals(old, User.instance.getService().getCurrentLocation());
    }

    @Test
    public void setNewLocationParamsOnMockFails() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "aa", FakeGpsService.SAT);
        mActivityRule.getActivity().startService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
        sleep(1000);
        User.instance.getService().setTimeInterval(0);
        User.instance.getService().setMinTimeInterval(0);
        User.instance.getService().setMinDistanceInterval(0);
    }

    @After
    public void endServices() {
        if (isMyServiceRunning(User.instance.getService().getClass()))
            mActivityRule.getActivity().stopService(new Intent(mActivityRule.getActivity().getBaseContext(), User.instance.getService().getClass()));
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