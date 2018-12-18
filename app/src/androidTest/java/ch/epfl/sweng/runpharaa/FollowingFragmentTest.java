package ch.epfl.sweng.runpharaa;

import android.content.ComponentName;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.core.AllOf.allOf;

public class FollowingFragmentTest extends TestInitLocation {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    //@Rule
    //public IntentsTestRule<MainActivity> mActivityTestRule =
    //        new IntentsTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "2");
    }

    @Before
    public void init(){
        Intents.init();
    }

    @Test
    public void testFollowingSystem() throws Throwable {
       // mActivityRule.launchActivity(null);

        sleep(2000);
        // refresh
        runOnUiThread(() ->((FragmentNearMe)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0)).onRefresh());
        sleep(1000);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));

        onView(withId(R.id.trackCreatorID)).perform(click());

        onView(withId(R.id.follow_button)).perform(click());

        Espresso.pressBack();


        Espresso.pressBack();

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());

        sleep(2000);

        // refresh
        runOnUiThread(() ->((FragmentFollowing)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(1)).onRefresh());


        sleep(1000);
        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        sleep(1000);

        intended(hasComponent(OtherUsersProfileActivity.class.getName()), times(2));
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }
}
