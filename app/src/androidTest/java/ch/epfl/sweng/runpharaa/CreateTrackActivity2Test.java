package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivity2Test {

    @Rule
    public ActivityTestRule<CreateTrackActivity2> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity2.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    private LatLng p1 = new LatLng(46.518577, 6.563165); //inm
    private LatLng p2 = new LatLng(46.522735, 6.579772); //Banane
    private LatLng p3 = new LatLng(46.519380, 6.580669); //centre sportif

    private Location l1 = generateLocation(p1);
    private Location l2 = generateLocation(p2);
    private Location l3 = generateLocation(p3);

    private LatLng[] points = {p1, p2, p3};
    private Location[] locations = {l1, l2, l3};
    private double totalDistance = l1.distanceTo(l2) + l2.distanceTo(l3);

    @Test
    public void correctValuesDisplayedForCustomTrack() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, CreateTrackActivity2.class);
        intent.putExtra("locations", locations);
        intent.putExtra("points", points);
        mActivityRule.launchActivity(intent);
        sleep(5_000);
        onView(withId(R.id.create_text_total_altitude)).check(matches(withText("Total altitude difference: 0.00 m")));
        onView(withId(R.id.create_text_total_distance)).check(matches(withText(String.format("Total distance: %.2f m", totalDistance))));
    }

    private Location generateLocation(LatLng p) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(p.latitude);
        l.setLongitude(p.longitude);
        l.setAltitude(0);
        l.setAccuracy(1);
        l.setTime(System.currentTimeMillis());
        return l;
    }

}