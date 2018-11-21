package ch.epfl.sweng.runpharaa.Initializer;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.firebase.Storage;

public class TestInitNoLocation {

    @BeforeClass
    public static void setTestModeOn() {
        Database.isTest = true;
        Storage.isTest = true;
    }

    @AfterClass
    public static void setTestModeOff() {
        Database.isTest = false;
        Database.isTest = false;
    }
}
