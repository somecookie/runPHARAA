package ch.epfl.sweng.runpharaa;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;

public class A extends TestInitLocation {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUser(){
        User.instance = Database.getUser();
    }

    @Test
    public void basicTest(){
        sleep(1000);
    }
}
