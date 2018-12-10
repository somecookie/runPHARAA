package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.SeekBar;

import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.util.TestInitLocation;
import ch.epfl.sweng.runpharaa.utils.Util;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.runpharaa.util.ViewUtils.setProgress;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivity2Test extends TestInitLocation {

    private static final int WAIT_TIME = 1000;

    @Rule
    public ActivityTestRule<CreateTrackActivity2> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity2.class, true, false);

    // ------------- COORDS --------------
    private LatLng inm = new LatLng(46.518577, 6.563165); //inm
    private LatLng banane = new LatLng(46.522735, 6.579772); //Banane
    private LatLng cs = new LatLng(46.519380, 6.580669); //centre sportif
    private LatLng eiffel = new LatLng(48.858664, 2.294424);
    private LatLng placeTrocadero = new LatLng(48.863048, 2.287890);
    private LatLng buckingham = new LatLng(51.501478, -0.141702);
    private LatLng localPub = new LatLng(51.499248, -0.136834);
    private LatLng marina = new LatLng(1.283536, 103.860319);
    private LatLng esplaTheatre = new LatLng(1.288845, 103.855491);

    // ------------- TESTS ---------------

    @Before
    public void initUser() {
        User.instance = new User("FakeUser", 2000, Uri.parse(""), new LatLng(21.23, 12.112), "FakeUser");
        GpsService.initFakeGps(FakeGpsService.GOOGLE);
    }

    @Test
    public void correctValuesDisplayedForInmBananeCs() {
        LatLng[] points = {inm, banane, cs};
        Location[] locations = generateLocations(points);
        double[] skrr = Util.computeDistanceAndElevationChange(locations);
        launchWithExtras(locations, points);
        onView(withId(R.id.create_text_total_altitude)).check(matches(withText(String.format("Total altitude difference: %.2f m", skrr[1]))));
        onView(withId(R.id.create_text_total_distance)).check(matches(withText(String.format("Total distance: %.2f m", skrr[0]))));
    }

    @Test
    public void createTrackWorksWithTwoFakePoints() {
        LatLng[] points = {eiffel, placeTrocadero};
        Location[] locations = generateLocations(points);
        launchWithExtras(locations, points);
        sleep(2000);
        onView(withId(R.id.set_properties)).perform(click());
        onView(withId(R.id.time)).perform(typeText("10.00"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
                .perform(closeSoftKeyboard());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.create_text_name)).perform(typeText("Name")).perform(closeSoftKeyboard());
        sleep(WAIT_TIME);
        selectFirstType(true);
        sleep(WAIT_TIME * 2);
        //TODO: MAKE THIS WORK
        //onView(withId(R.id.create_track_button)).perform(click());
    }

    @Test
    public void creatingTrackWithoutSettingPropertiesFails() {
        LatLng[] points = {buckingham, localPub};
        Location[] locations = generateLocations(points);
        launchWithExtras(locations, points);
        onView(withId(R.id.create_text_name)).perform(typeText("Buckingham to pub")).perform(closeSoftKeyboard());
        sleep(WAIT_TIME);
        onView(withId(R.id.create_track_button)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.properties_not_set)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void creatingTrackWithoutSettingTypesFails() {
        LatLng[] points = {marina, esplaTheatre};
        Location[] locations = generateLocations(points);
        launchWithExtras(locations, points);
        onView(withId(R.id.create_text_name)).perform(typeText("Marina Bay to theatre")).perform(closeSoftKeyboard());
        onView(withId(R.id.set_properties)).perform(click());
        sleep(WAIT_TIME);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        sleep(WAIT_TIME);
        selectAllTypes(false);
        onView(withId(R.id.create_track_button)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.types_not_set)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void slideBarCorrectlyUpdatesDifficulty() {
        LatLng[] points = {inm, banane};
        Location[] locations = generateLocations(points);
        launchWithExtras(locations, points);
        onView(withId(R.id.set_properties)).perform(click());
        onView(withClassName(Matchers.equalTo(SeekBar.class.getName()))).perform(setProgress(1));
        onView(withId(R.id.diff_text)).check(matches(withText(mActivityRule.getActivity().getResources().getString(R.string.difficulty_is) + "1")));
        sleep(WAIT_TIME);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.default_time)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        sleep(WAIT_TIME);
        selectFirstType(true);
        onView(withId(R.id.create_track_button)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.need_name)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void creatingTrackWithoutSettingNameFails() {
        LatLng[] points = {inm, banane};
        Location[] locations = generateLocations(points);
        launchWithExtras(locations, points);
        onView(withId(R.id.set_properties)).perform(click());
        sleep(WAIT_TIME);
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.OK)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        sleep(WAIT_TIME);
        selectAllTypes(true);
        onView(withId(R.id.create_track_button)).perform(click());
        onView(withText(mActivityRule.getActivity().getResources().getString(R.string.need_name)))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private void selectFirstType(boolean pressOk) {
        onView(withId(R.id.types)).perform(click());
        onData(is(instanceOf(String.class))).inAdapterView(allOf(withClassName(equalTo("com.android.internal.app.AlertController$RecycleListView")), isDisplayed()))
                .atPosition(0)
                .perform(click());
        sleep(WAIT_TIME);
        onView(withText(pressOk ? mActivityRule.getActivity().getResources().getString(R.string.OK) : mActivityRule.getActivity().getResources().getString(R.string.dismiss)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        sleep(WAIT_TIME);
    }

    private void selectAllTypes(boolean pressOk) {
        onView(withId(R.id.types)).perform(click());
        for (int i = 0; i < TrackType.values().length; ++i) {
            onData(is(instanceOf(String.class))).inAdapterView(allOf(withClassName(equalTo("com.android.internal.app.AlertController$RecycleListView")), isDisplayed()))
                    .atPosition(i)
                    .perform(click())
                    .perform(click())
                    .perform(click());
        }
        sleep(WAIT_TIME);
        onView(withText(pressOk ? mActivityRule.getActivity().getResources().getString(R.string.OK) : mActivityRule.getActivity().getResources().getString(R.string.dismiss)))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        sleep(WAIT_TIME);
    }

    private Location generateLocation(LatLng p) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(p.latitude);
        l.setLongitude(p.longitude);
        l.setAltitude(0);
        l.setAccuracy(1);
        l.setTime(System.currentTimeMillis());
        return l;
    }

    private Location[] generateLocations(LatLng[] points) {
        Location[] locations = new Location[points.length];
        for (int i = 0; i < locations.length; ++i)
            locations[i] = generateLocation(points[i]);
        return locations;
    }

    private void launchWithExtras(Location[] locations, LatLng[] points) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, CreateTrackActivity2.class);
        intent.putExtra("locations", locations);
        intent.putExtra("points", points);
        mActivityRule.launchActivity(intent);
        sleep(WAIT_TIME);
    }

}
