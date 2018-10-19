package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class CreateTrackActivityTest {
    @Rule
    public final ActivityTestRule<CreateTrackActivity> mActivityRule =
            new ActivityTestRule<>(CreateTrackActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    @BeforeClass
    public static void initUser(){
        User.instance = new User("FakeUser", 2000, null, new HashSet<Integer>(), new HashSet<Integer>(), new LatLng(21.23, 12.112), false, "aa");
    }

    @Before
    public void grantPermissions() {
        InstrumentationRegistry
                .getInstrumentation()
                .getUiAutomation()
                //.executeShellCommand(String.format("appops set %s android:mock_location allow", "ch.epfl.sweng.runpharaa"));
                .executeShellCommand(String.format("appops set %s android:mock_location allow", mActivityRule.getActivity().getPackageName()));
    }

    //------------- METHOD TO MOCK LOCATION, GRANT PERMISSION AND SEEKBAR ----------------

    private int setMockLocationSettings() {
        int value = 1;
        try {
            value = Settings.Secure.getInt(mActivityRule.getActivity().getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION);
            Settings.Secure.putInt(mActivityRule.getActivity().getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void restoreMockLocationSettings(int restore_value) {
        try {
            Settings.Secure.putInt(mActivityRule.getActivity().getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION, restore_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private LocationManager locMgr;

    private void setMock(double latitude, double longitude, float accuracy) {
        locMgr.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);
        newLocation.setAltitude(0);
        newLocation.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        locMgr.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        locMgr.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null, System.currentTimeMillis());

        locMgr.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
    }

    private static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    //----------------------------TESTS-------------------------------


    @Test
    public void createTrackWorksWithTwoFakePoints() {
        int value = setMockLocationSettings();//toggle ALLOW_MOCK_LOCATION on
        try {
        System.out.print(mActivityRule.getActivity().getPackageName());
        locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Let map load
        sleep(2000);
        setMock(46.518577, 6.563165, 1);
        sleep(500);
        onView(withId(R.id.start_create_button)).perform(click());
        sleep(500);
        setMock(46.522735, 6.579772, 1);
        sleep(500);
        onView(withId(R.id.start_create_button)).perform(click());
        onView(withId(R.id.create_text_total_altitude)).check(matches(withText("Total altitude difference: 0.00 m")));
        onView(withId(R.id.create_text_name)).perform(typeText("Random name")).perform(closeSoftKeyboard());
        onView(withId(R.id.set_properties)).perform(click());
        onView(withId(R.id.time)).perform(typeText("10.00"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))
                .perform(closeSoftKeyboard());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        sleep(1000);
        onView(withId(R.id.types)).perform(click());
        onData(is(instanceOf(String.class))).inAdapterView(allOf(withClassName(equalTo("com.android.internal.app.AlertController$RecycleListView")), isDisplayed()))
                .atPosition(0)
                .perform(click());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.create_track_button)).perform(click());

        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }




    @Test
    public void needAtLeastTwoPointsToCreateTracks(){
        int value = setMockLocationSettings();//toggle ALLOW_MOCK_LOCATION on
        try {
            System.out.print(mActivityRule.getActivity().getPackageName());
            locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
            // Let map load
            sleep(2000);
            //Setting location to the Burj Kahlifa
            setMock(25.197401, 55.274377, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            onView(withText("You need at least 2 points to create a track !"))
                    .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }


    @Test
    public void tryCreatingTrackWithoutSettingProperties(){
        int value = setMockLocationSettings();//toggle ALLOW_MOCK_LOCATION on
        try {
            System.out.print(mActivityRule.getActivity().getPackageName());
            locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
            // Let map load
            sleep(2000);
            //Setting location to Buckingham Palace
            setMock(51.501478, -0.141702, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            sleep(500);
            //To local pub
            setMock(51.499248, -0.136834, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            onView(withId(R.id.create_text_name)).perform(typeText("From Buckingham palace to local pub")).perform(closeSoftKeyboard());
            //We need to wait long enough for the "Ending GPS signal toast to disappear"
            sleep(2000);
            onView(withId(R.id.create_track_button)).perform(click());
            onView(withText("The properties must be set up"))
                    .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }


    @Test
    public void tryCreatingTrackWithoutSettingTypes(){
    int value = setMockLocationSettings();//toggle ALLOW_MOCK_LOCATION on
        try {
            System.out.print(mActivityRule.getActivity().getPackageName());
            locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
            // Let map load
            sleep(2000);
            //Marina Bay Sands
            setMock(1.283536, 103.860319, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            sleep(500);
            //To Esplanade theater
            setMock(1.288845, 103.855491, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            onView(withId(R.id.create_text_name)).perform(typeText("Small walk near Marina Bay")).perform(closeSoftKeyboard());
            onView(withId(R.id.set_properties)).perform(click());
            sleep(1000);
            onView(withText("OK"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());
            sleep(3000);
            onView(withId(R.id.create_track_button)).perform(click());
            onView(withText("The types must be set up"))
                    .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }

    @Test
    public void tryCreatingTrackWithoutSettingName() {
        int value = setMockLocationSettings();//toggle ALLOW_MOCK_LOCATION on
        try {
            System.out.print(mActivityRule.getActivity().getPackageName());
            locMgr = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
            // Let map load
            sleep(2000);
            //Eiffel Tower
            setMock(48.858664, 2.294424, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            sleep(500);
            //To place du trocadero
            setMock(48.863048, 2.287890, 1);
            sleep(500);
            onView(withId(R.id.start_create_button)).perform(click());
            onView(withId(R.id.create_text_total_altitude)).check(matches(withText("Total altitude difference: 0.00 m")));
            onView(withId(R.id.set_properties)).perform(click());
            onView(withClassName(Matchers.equalTo(SeekBar.class.getName()))).perform(setProgress(1));
            onView(withId(R.id.diff_text)).check(matches(withText("Difficulty is 1")));
            sleep(1000);
            onView(withText("OK"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());
            sleep(3000);
            onView(withText("Default run time was chosen"))
                    .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
            sleep(3000);
            onView(withId(R.id.types)).perform(click());
            onData(is(instanceOf(String.class))).inAdapterView(allOf(withClassName(equalTo("com.android.internal.app.AlertController$RecycleListView")), isDisplayed()))
                    .atPosition(0)
                    .perform(click());
            onView(withText("OK"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());
            onView(withId(R.id.create_track_button)).perform(click());
            onView(withText("A track needs a name!"))
                    .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            restoreMockLocationSettings(value);//toggle ALLOW_MOCK_LOCATION off
        }
    }



}