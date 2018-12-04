package ch.epfl.sweng.runpharaa.utils;

import com.google.android.gms.maps.GoogleMap;

public abstract class Config {
    public static boolean isTest = false;

    private static GoogleMap fakeMap;

    public static void setFakeMap(GoogleMap map) {
        Config.fakeMap = map;
    }

    public static GoogleMap getFakeMap() {
        return fakeMap;
    }
}
