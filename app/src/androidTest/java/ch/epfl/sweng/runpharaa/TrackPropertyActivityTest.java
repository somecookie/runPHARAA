package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TrackPropertyActivityTest {

    @Rule
    public final ActivityTestRule<TrackPropertiesActivity> mActivityRule =
            new ActivityTestRule<>(TrackPropertiesActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testLike() {
        onView(withId(R.id.buttonLikeID)).perform(click());
    }

    @Test
    public void testFavourite() {
        onView(withId(R.id.buttonFavoriteID)).perform(click());
    }

}
