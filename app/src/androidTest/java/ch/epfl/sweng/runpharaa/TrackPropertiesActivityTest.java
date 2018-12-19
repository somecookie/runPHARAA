package ch.epfl.sweng.runpharaa;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.comment.Comment;
import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.StreakManager;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TrackPropertiesActivityTest extends TestInitLocation {

    @Rule
    public ActivityTestRule<TrackPropertiesActivity> mActivityRule =
            new ActivityTestRule<>(TrackPropertiesActivity.class, false, false);
    ;
    private ViewAction clickOnName = new ViewAction() {
        @Override
        public Matcher<View> getConstraints() {
            return null;
        }

        @Override
        public String getDescription() {
            return "Click on inside button";
        }

        @Override
        public void perform(UiController uiController, View view) {
            AppCompatTextView name = view.findViewById(R.id.comment_name);
            name.performClick();
        }
    };


    @Before
    public void init() {
        User.instance = Database.getUser();
        User.instance.setStreakManager(new StreakManager(1, 1, 1));
        Intents.init();
    }

    @Test
    public void clickOnHomeButton(){
        Track t1 = createTrack();
        launchWithExtras(t1);
        //The description only works if the app is in english, there could be an issue if not
        onView(withContentDescription("Navigate up")).perform(click());
        sleep(2000);
        intended(hasComponent(MainActivity.class.getName()));
    }


    @Test
    public void giveFeedBack() {
        Track t1 = createTrack();
        launchWithExtras(t1);

        User.instance = new User("Alice", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "2");

        sleep(2000);
        onView(withId(R.id.feedbackButton)).perform(click());

        onView(withId(R.id.time)).perform(typeText("10.00"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
                .perform(closeSoftKeyboard());

        t1.getProperties().addNewDuration(10.00);

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        sleep(2000);
    }

    @Test
    public void cannotGiveFeedbackTwice() {
        Track t1 = createTrack();
        User.instance.addNewFeedBack(t1.getTrackUid());
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.feedbackButton)).perform(click());

        onView(withId(R.id.time)).perform(typeText("10.00"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
                .perform(closeSoftKeyboard());

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.feedback_exists)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void cannotGiveFeedbackWithoutSettingTime() {
        Track t1 = createTrack();
        User.instance.addNewFeedBack(t1.getTrackUid());
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.feedbackButton)).perform(click());

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.prop_miss)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shareOnFacebook() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.fb_share_button)).perform(click());
    }

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
    public void deletedTrackTest(){
        Track t1 = createTrack();
        String s = User.instance.getUid();
        launchWithExtras(t1);
        onView(withId(R.id.deleteButton))
                .perform(click());
        onView(withText(R.string.cancel)).perform(click());

        onView(withId(R.id.deleteButton))
                .perform(click());
        onView(withText(R.string.delete)).perform(click());

        assertTrue(mActivityRule.getActivity().isFinishing());

        User.instance.setUid(s);
    }

    @Test
    public void testLike() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        onView(withId(R.id.buttonLikeID)).perform(click());
        withId(R.id.trackLikesID).matches(withText("1"));
        sleep(3000);

        onView(withId(R.id.buttonLikeID)).perform(click());
        sleep(3000);
    }

    @Test
    public void testFavourite() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("1"));
        sleep(4000);
    }

    @Test
    public void pressingLikeUpdatesValue() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        User.instance.setNotificationKey("OtherNotificationKey");
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
        User.instance.setNotificationKey("OtherNotificationKey");
        int favsBefore = createTrack().getProperties().getLikes();
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Fav: " + favsBefore + 1));
        sleep(500);
        onView(withId(R.id.buttonFavoriteID)).perform(click());
        withId(R.id.trackFavouritesID).matches(withText("Fav: " + favsBefore));
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
    public void testCommentTooLong() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(5_000);
        onView(withId(R.id.commentsID)).perform(click());

        sleep(2000);

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

    @Test
    public void testEmptyComment() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.commentsID)).perform(click());

        sleep(2000);

        onView(withId(R.id.comments_editText)).perform(replaceText(""));

        sleep(2000);

        onView(withId(R.id.post_button)).perform(click());

        Espresso.pressBack();

        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.comment_too_short)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAddingCorrectComment() {
        postComment();
        withId(R.id.commentsID).matches(withText("1"));
    }

    @Test
    public void goToYourProfileFromComment() {
        postComment();
        onView(withId(R.id.commentsID)).perform(click());
        sleep(2000);
        onView(allOf(withId(R.id.comment_rv), isDisplayed())).perform(
                actionOnItemAtPosition(0, clickOnName));
        sleep(2000);
        intended(hasComponent(UsersProfileActivity.class.getName()));
    }

    @Test
    public void goToOtherProfileFromComment() {
        postComment();
        onView(withId(R.id.commentsID)).perform(click());
        sleep(2000);
        User.instance = new User("Alice", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "2");
        User.instance.setStreakManager(new StreakManager(1, 1, 1));
        sleep(2000);
        onView(allOf(withId(R.id.comment_rv), isDisplayed())).perform(
                actionOnItemAtPosition(0, clickOnName));
        sleep(2000);
        intended(hasComponent(OtherUsersProfileActivity.class.getName()));
    }

    private void postComment() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        onView(withId(R.id.commentsID)).perform(click());
        sleep(2000);
        onView(withId(R.id.comments_editText)).perform(replaceText("Hey, very nice track, love it!"));
        sleep(2000);
        Comment com = new Comment(User.instance.getUid(), "Hey, very nice track, love it!", new Date());
        t1.addComment(com);
        onView(withId(R.id.post_button)).perform(click());
        sleep(2000);
    }

    @Test
    public void testOpenAndCloseReport() {
        Track t1 = createTrack();
        launchWithExtras(t1);
        sleep(2000);
        // display the popup
        onView(withId(R.id.reportID)).perform(click());
        sleep(2000);
        // dismiss the popup by clicking on it
        onView(withContentDescription(R.string.report_description))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        // check that the activity is visible by checking that its button and map are displayed
        onView(withId(R.id.buttonLikeID)).check(matches(isDisplayed()));
    }

    private Track createTrack() {
        Set<TrackType> types = new HashSet<>();
        types.add(TrackType.FOREST);
        CustLatLng coord0 = new CustLatLng(37.422, -122.084); //inm
        CustLatLng coord1 = new CustLatLng(37.425, -122.082); //inm
        TrackProperties p = new TrackProperties(100, 10, 1, 1, types);

        return new Track("0", "Bob", "Cours forest !", Arrays.asList(coord0, coord1), new ArrayList<>(), p);
    }

    private void launchWithExtras(Track t) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, TrackPropertiesActivity.class);
        intent.putExtra("TrackID", t.getTrackUid());
        mActivityRule.launchActivity(intent);
        sleep(1_000);
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

}



