package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.core.AllOf;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.core.AllOf.allOf;

public class FollowingFragmentTests extends TestInitLocation {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initUser() {
        User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "1");
    }

    @Test
    public void testNoFollowing() throws Throwable {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        sleep(2000);

        //refresh
        runOnUiThread(() ->((FragmentFollowing)mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(1)).onRefresh());

        sleep(1000);

        onView(allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(targetContext.getResources().getString(R.string.no_follow))));
    }

    @Test
    public void databaseErrorFavorite() throws Throwable {

        onView(withId(R.id.viewPagerId)).perform(swipeLeft());

        Database.setIsCancelled(true);

        runOnUiThread(() ->((FragmentFollowing)this.mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(1)).onRefresh());

        onView(AllOf.allOf(withId(R.id.emptyMessage), isDisplayed())).check(matches(withText(R.string.no_follow)));

        //For further tests
        Database.setIsCancelled(false);

    }
}
