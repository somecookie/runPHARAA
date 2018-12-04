package ch.epfl.sweng.runpharaa.Initializer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ch.epfl.sweng.runpharaa.utils.Config;

public class TestInitNoLocation extends Authorizations{

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
