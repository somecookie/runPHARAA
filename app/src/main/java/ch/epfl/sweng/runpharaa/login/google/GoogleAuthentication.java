package ch.epfl.sweng.runpharaa.login.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ch.epfl.sweng.runpharaa.utils.Config;

public abstract class GoogleAuthentication {

    private static GoogleAuthentication instance;

    public static GoogleAuthentication getInstance(Context c) {
        if (instance == null) {
            instance = Config.isTest ? new GoogleAuthenticationMock(c) : new GoogleAuthenticationReal();
        }
        return instance;
    }

    /**
     * Method the get the client if any
     *
     * @param activity an Activity
     * @param googleSignInOptions a GoogleSignInOptions
     * @return the GoogleSignInClient
     */
    public abstract GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions);

    /**
     * Getter to get the intent
     *
     * @return Intent
     */
    public abstract Intent getSignInIntent();
}
