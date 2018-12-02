package ch.epfl.sweng.runpharaa;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;

import static android.os.SystemClock.sleep;

public class A extends TestInitLocation {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void basicTest(){
        sleep(1000);
    }
}
