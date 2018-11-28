package ch.epfl.sweng.runpharaa.Initializer;

import android.Manifest;
import android.support.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sweng.runpharaa.utils.Config;


public class TestInitLocation {

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setTestModeOn() {
        Config.isTest = true;
    }

    @AfterClass
    public static void setTestModeOff() {
        Config.isTest = false;
    }
}
