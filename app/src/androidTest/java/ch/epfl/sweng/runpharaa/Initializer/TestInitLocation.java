package ch.epfl.sweng.runpharaa.Initializer;

import android.Manifest;
import android.support.test.espresso.Espresso;
import android.support.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sweng.runpharaa.Firebase.Database;
import ch.epfl.sweng.runpharaa.Firebase.Storage;

public class TestInitLocation {

    @BeforeClass
    public static void setTestModeOn(){
        Database.isTest = true;
        Storage.isTest = true;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @AfterClass
    public static void setTestModeOff(){
        Database.isTest = false;
        Database.isTest = false;
    }
}
