package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class MapsTest {
    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

    @BeforeClass
    public static void initUser() {
        User.instance = new User("FakeUser", 2000, null, new HashSet<Integer>(), new HashSet<Integer>(), new LatLng(46.520566, 6.567820), false, "aa");
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void test() {
        SystemClock.sleep(5_000);
        onView(withId(R.id.maps_test_text)).check(matches(withText("ready")));
    }

}
