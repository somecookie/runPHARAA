package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.common.internal.Constants;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivityTest {
    @Rule
    public final ActivityTestRule<CreateTrackActivity> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void grantPermissions() {
        InstrumentationRegistry
                .getInstrumentation()
                .getUiAutomation()
                .executeShellCommand(String.format("appops set %s android:mock_location allow", "ch.epfl.sweng.runpharaa"));
                //.executeShellCommand(String.format("appops set %s android:mock_location allow", mActivityRule.getActivity().getPackageName()));
    }

    @Test
    public void createTrackWorksWithTwoFakePoints() {
        System.out.print(mActivityRule.getActivity().getPackageName());
        locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Let map load
        sleep(2000);
        setMock(46.518577, 6.563165, 1);
        sleep(500);
        onView(withId(R.id.start_create_button)).perform(click());
        setMock(46.522735, 6.579772, 1);
        sleep(500);
        onView(withId(R.id.start_create_button)).perform(click());
        onView(withId(R.id.create_text_total_altitude)).check(matches(withText("Total altitude difference: 0.00 m")));
        onView(withId(R.id.create_text_name)).perform(typeText("Random name")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_track_button)).perform(click());
    }

    private LocationManager locMgr;

    private void setMock(double latitude, double longitude, float accuracy) {
        locMgr.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);
        newLocation.setAltitude(0);
        newLocation.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        locMgr.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        locMgr.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null, System.currentTimeMillis());

        locMgr.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
    }
}