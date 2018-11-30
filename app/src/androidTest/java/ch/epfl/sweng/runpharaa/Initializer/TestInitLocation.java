package ch.epfl.sweng.runpharaa.Initializer;

import android.Manifest;
import android.support.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.firebase.Storage;
import ch.epfl.sweng.runpharaa.login.firebase.FirebaseAuthentication;
import ch.epfl.sweng.runpharaa.login.google.GoogleAuthentication;


public class TestInitLocation {

    @BeforeClass
    public static void setTestModeOn(){
        Database.isTest = true;
        Storage.isTest = true;
        GoogleAuthentication.isTest = true;
        FirebaseAuthentication.isTest = true;
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @AfterClass
    public static void setTestModeOff(){
        Database.isTest = false;
        Database.isTest = false;
        GoogleAuthentication.isTest = false;
        FirebaseAuthentication.isTest = false;
    }
}
