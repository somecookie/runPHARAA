package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TrackPropertiesActivityTest {

    @Rule
    public ActivityTestRule<TrackPropertiesActivity> mActivityRule =
            new ActivityTestRule<>(TrackPropertiesActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void correctValuesDisplayedForTrack1() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, TrackPropertiesActivity.class);
        Track t1 = Track.allTracks().get(1);
        intent.putExtra("TrackID", 1);
        mActivityRule.launchActivity(intent);
        withId(R.id.trackTitleID).matches(withText(t1.getLocation()));
        withId(R.id.trackLengthID).matches(withText("Length: " + Double.toString(t1.getTrack_length()) + "m"));
        withId(R.id.trackLikesID).matches(withText("Likes: " + t1.getLikes()));
    }


}
