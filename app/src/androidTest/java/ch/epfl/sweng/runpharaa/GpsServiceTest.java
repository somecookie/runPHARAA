package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

/**
 * Tests the GpsService Service to see if it launches / doesn't launch at the right time
 */
@RunWith(AndroidJUnit4.class)
public class GpsServiceTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void doesNotLaunchGpsServiceInitially() {
        assertTrue(!isMyServiceRunning(GpsService.class));
    }

    @Test
    public void correctlyLaunchesServiceOnMapView() {
        turnsGpsServiceOnAndOff(R.id.mapIcon);
    }

    @Test
    public void correctlyLaunchesServiceOnCreateTrack1() {
        turnsGpsServiceOnAndOff(R.id.fab);
    }

    private void turnsGpsServiceOnAndOff(int id) {
        onView(withId(id)).perform(click());
        assertTrue(isMyServiceRunning(GpsService.class));
        pressBack();
        assertTrue(!isMyServiceRunning(GpsService.class));
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