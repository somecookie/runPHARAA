package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.runpharaa.map.FullMapActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;
import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FullMapActivityTest extends TestInitLocation {

    @Rule
    public ActivityTestRule<FullMapActivity> mActivityRule =
            new ActivityTestRule<>(FullMapActivity.class, true, false);

    @Test
    public void testTrackPropertiesMap() {
        LatLngAdapter coord0 = new LatLngAdapter(37.422, -122.084); //inm
        LatLngAdapter coord1 = new LatLngAdapter(37.425, -122.082); //inm
        List<LatLngAdapter> p = Arrays.asList(coord0, coord1);
        LatLng[] points = LatLngAdapter.CustLatLngToLatLng(p).toArray(new LatLng[p.size()]);
        launchWithExtras(points);
        sleep(5_000);
        onView(withId(R.id.maps_test_text3)).check(matches(withText("ready")));
    }

    private void launchWithExtras(LatLng[] points) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, FullMapActivity.class);
        intent.putExtra("points", points);
        mActivityRule.launchActivity(intent);
        sleep(1_000);
    }
}
