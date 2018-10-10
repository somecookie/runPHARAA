package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivity2Test {

    private LatLng[] points = new LatLng[3];

    private LatLng coord3 = new LatLng(46.518475, 6.561960); //BC
    private LatLng coord4 = new LatLng(46.517563, 6.562350); //Innovation parc
    private LatLng coord5 = new LatLng(46.518447, 6.568238); //Rolex

    private LocationManager locMgr;


    @Rule
    public final ActivityTestRule<CreateTrackActivity> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);


    /*@Test
    public void addNameAndConfirmTest(){
        locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        onView(withId(R.id.start_create_button)).perform(click());
        setMock(90, 90, 15);
        sleep(10_000);
        setMock(0, 0, 15);
        sleep(10_000);
        onView(withId(R.id.start_create_button)).perform(click());
        sleep(5_000);

        //onView(withId(R.id.create_text_name)).perform(typeText("Track1")).perform(closeSoftKeyboard());
        //onView(withId(R.id.create_track_button)).perform(click());

    }*/

    private void setMock(double latitude, double longitude, float accuracy) {
        locMgr.addTestProvider (LocationManager.GPS_PROVIDER,
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
        newLocation.setAccuracy(500);
        newLocation.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        locMgr.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        locMgr.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null,System.currentTimeMillis());

        locMgr.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);}
}
