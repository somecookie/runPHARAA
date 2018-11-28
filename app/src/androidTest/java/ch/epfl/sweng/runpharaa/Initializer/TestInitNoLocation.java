package ch.epfl.sweng.runpharaa.Initializer;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.epfl.sweng.runpharaa.utils.Config;

public class TestInitNoLocation {

    @BeforeClass
    public static void setTestModeOn() {
        Config.isTest = true;
    }

    @AfterClass
    public static void setTestModeOff() {
        Config.isTest = false;
    }
}
