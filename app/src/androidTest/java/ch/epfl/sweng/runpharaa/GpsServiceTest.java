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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the RealGpsService Service to see if it launches / doesn't launch at the right time
 */
@RunWith(AndroidJUnit4.class)
public class GpsServiceTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    private Context c;

    @Before
    public void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "1");
        c = getTargetContext();
    }

    @Test
    public void instanceNotNullWhenLaunched() {
        assertNotEquals(null, GpsService.getInstance());
    }

    @Test
    public void launchesRealServiceOnMainActivity() {
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        sleep(1000);
        assertTrue(isMyServiceRunning(RealGpsService.class));
    }

    @Test
    public void launchesFakeServiceOnMainActivity() {
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        sleep(1000);
        assertTrue(isMyServiceRunning(FakeGpsService.class));
    }

    @Test
    public void getLocationFromGps() {
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        sleep(10_000);
    }

    @Test
    public void setNewLocationFailsOnGps() {
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        sleep(1000);
        Location old = GpsService.getInstance().getCurrentLocation();
        GpsService.getInstance().setNewLocation(c, Util.locationFromLatLng(new LatLng(0, 0)));
        assertEquals(old, GpsService.getInstance().getCurrentLocation());
    }

    @Test
    public void setNewLocationWorksOnFake() {
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        Location n = Util.locationFromLatLng(new LatLng(0, 0));
        GpsService.getInstance().setNewLocation(c, n);
        assertEquals(n, GpsService.getInstance().getCurrentLocation());
    }

    @Test
    public void setNewParamsOnMockFails() {
        GpsService.initFakeGps(FakeGpsService.SAT);
        c.startService(new Intent(c, GpsService.getInstance().getClass()));
        sleep(1000);
        GpsService.getInstance().setTimeInterval(0);
        GpsService.getInstance().setMinTimeInterval(0);
        GpsService.getInstance().setMinDistanceInterval(0);
    }

    @After
    public void endServices() {
        if (isMyServiceRunning(GpsService.getInstance().getClass()))
            c.stopService(new Intent(c, GpsService.getInstance().getClass()));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}