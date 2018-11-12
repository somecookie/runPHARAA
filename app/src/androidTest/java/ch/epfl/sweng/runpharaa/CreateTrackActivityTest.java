package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;
import android.widget.Toast;

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

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.user.User;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class CreateTrackActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2, Uri.parse(""), new LatLng(21.23, 12.112), "aa", FakeGpsService.SAT);
    }

    @Test
    public void createTrackWithTwoPoints() {
        Context c = mActivityRule.getActivity().getBaseContext();
        mActivityRule.getActivity().startService(new Intent(c, User.instance.getService().getClass()));
        onView(withId(R.id.fab)).perform(click());
        sleep(3000);
        onView(withId(R.id.start_create_button)).perform(click());
        onView(withId(R.id.start_create_button)).perform(click());
        User.instance.getService().setNewLocation(c, Util.locationFromLatLng(new LatLng(46.506279, 6.626111))); // Ouchy
        sleep(3000);
        onView(withId(R.id.start_create_button)).perform(click());
    }

}
