package ch.epfl.sweng.runpharaa.firebase.authentification.google;

import android.content.Context;

public class GoogleAuth {

    public static boolean isTest = false;

    private static GoogleAuthInterface instance;

    public static GoogleAuthInterface getInstance(Context c) {
        if (instance == null) {
            if (isTest)
                instance = new GoogleAuthMock(c);
            else
                instance = new GoogleAuthReal();
        }
        return instance;
    }
}
