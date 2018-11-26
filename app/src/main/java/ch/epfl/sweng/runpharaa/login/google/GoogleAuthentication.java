package ch.epfl.sweng.runpharaa.login.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public abstract class GoogleAuthentication {

    public static boolean isTest = false;

    private static GoogleAuthentication instance;

    public static GoogleAuthentication getInstance(Context c) {
        if (instance == null) {
            if (isTest)
                instance = new GoogleAuthenticationMock(c);
            else
                instance = new GoogleAuthenticationReal();
        }
        return instance;
    }

    /**
     * Method the get the client if any
     * @param activity
     * @param googleSignInOptions
     * @return
     */
    public abstract GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions);

    /**
     * Getter to get the intent
     * @return Intent
     */
    public abstract Intent getSignInIntent();
}
