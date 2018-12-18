package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.core.AllOf.allOf;

public class OtherUserProfileActivityTest extends TestInitLocation {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public final ActivityTestRule<OtherUsersProfileActivity> mActivityRule2 =
            new ActivityTestRule<>(OtherUsersProfileActivity.class, false, false);

    @Before
    public void init(){
        Intents.init();
    }

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "1");
    }

    @Test
    public void clickOnHomeButton(){
        mActivityRule2.launchActivity(null);
        sleep(2000);
        //The description only works if the app is in english, there could be an issue if not
        onView(withContentDescription("Navigate up")).perform(click());
        sleep(2000);
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testFollowButtonAppears() throws Throwable {
        sleep(2000);
        // refresh
        runOnUiThread(() ->((FragmentNearMe)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0)).onRefresh());
        sleep(1000);

        onView(allOf(withId(R.id.cardListId), isDisplayed())).perform(
                actionOnItemAtPosition(0, click()));
        onView(withId(R.id.trackCreatorID)).perform(click());

        onView(withId(R.id.follow_button)).check(matches(withText("FOLLOW")));
        onView(withId(R.id.follow_button)).perform(click());
        sleep(1000);
        onView(withId(R.id.follow_button)).check(matches(withText("UNFOLLOW")));
        onView(withId(R.id.follow_button)).perform(click());
        onView(withId(R.id.follow_button)).check(matches(withText("FOLLOW")));
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }
}
