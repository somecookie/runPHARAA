package ch.epfl.sweng.runpharaa.util;

import android.Manifest;
import android.support.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sweng.runpharaa.utils.Config;


public class TestInitLocation extends Authorizations{

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void setTestModeOn() {
        Config.isTest = true;
        MapUtils.initCameraUpdateFactory();
        MapUtils.initBitmapDescriptorFactory();
        Config.setFakeMap(MapUtils.FAKE_MAP);
    }

    @AfterClass
    public static void setTestModeOff() {
        Config.isTest = false;
    }
}
