package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class TrackPropertiesActivityTest extends TestInitLocation {

    @BeforeClass
    public static void initUser() {
        User.instance = Database.getUser();
        //User.set(Database.getUser());
        //InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("adb install ~/facebook-android-sdk-3.5/bin/FBAndroid-28.apk");
    }

    @Rule
    public ActivityTestRule<TrackPropertiesActivity> mActivityRule =
            new ActivityTestRule<>(TrackPropertiesActivity.class, true, false);

    @Before
    public void initUserAndTracks() {
        User.instance = Database.getUser();
        //User.set("FakeUser", 2000, Uri.parse(""), new LatLng(37.422, -122.084), "aa");
    }

    @Test
    public void shareOnFacebook() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.fb_share_button)).perform(click());
    }

    @Ignore
    @Test
    public void shareOnTwitter() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.twitter_share_button)).perform(click());
    }

    @Test
    public void correctValuesDisplayedForTrack1() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        withId(R.id.trackTitleID).matches(withText(t1.getName()));
        withId(R.id.trackLengthID).matches(withText("Length: " + Double.toString(t1.getProperties().getLength()) + "m"));
        withId(R.id.trackLikesID).matches(withText("Likes: " + t1.getProperties().getLikes()));
    }

    @Test
    public void testLike() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("1"));

    }

    @Test
    public void testFavourite() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("1"));
    }

    @Test
    public void pressingLikeUpdatesValue() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        int likesBefore = createTrack().getProperties().getLikes();
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("Likes: " + likesBefore + 1));
        sleep(500);
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("Likes: " + likesBefore));
    }

    @Test
    public void addingToFavoritesUpdatesValue() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        int favsBefore = createTrack().getProperties().getLikes();
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Likes: " + favsBefore + 1));
        sleep(500);
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Likes: " + favsBefore));
    }

    @Test
    public void testTrackPropertiesMap() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(5_000);
        onView(withId(R.id.maps_test_text2)).check(matches(withText("ready")));
    }

    @Test
    public void testButtonVisitProfile() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(5_000);
        onView(withId(R.id.trackCreatorID)).perform(click());
    }

    @Test
    public void testCommentTooLong(){
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(5_000);
        onView(withId(R.id.commentsID)).perform(click());

        sleep(2000);
        //onView(withId(R.id.comments_editText)).perform(click());

        onView(withId(R.id.comments_editText)).perform(replaceText("Mais, vous savez, moi je ne crois pas " +
                "qu'il y ait de bonne ou de mauvaise situation. " +
                "Moi, si je devais résumer ma vie aujourd'hui avec vous, " +
                "je dirais que c'est d'abord des rencontres, " +
                "Des gens qui m'ont tendu la main, " +
                "peut-être à un moment où je ne pouvais pas, où j'étais seul chez moi. " +
                "Et c'est assez curieux de se dire que les hasards, " +
                "les rencontres forgent une destinée... "));

        sleep(2000);

        onView(withId(R.id.post_button)).perform(click());

        Espresso.pressBack();

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.comment_too_long)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private Track createTrack() {
        Bitmap b = Util.createImage(200, 100, R.color.colorPrimary);
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        CustLatLng coord0 = new CustLatLng(37.422, -122.084); //inm
        CustLatLng coord1 = new CustLatLng(37.425, -122.082); //inm
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);

        Track track = new Track("0", "Bob","Cours forest !", Arrays.asList(coord0, coord1), p);


        return track;
    }

    private void launchWithExtras(Track t) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, TrackPropertiesActivity.class);
        intent.putExtra("TrackID", t.getTrackUid());
        mActivityRule.launchActivity(intent);
        sleep(1_000);
    }

}



