package ch.epfl.sweng.runpharaa.Firebase.Authentification.Google;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public interface GoogleAuthInterface {

    /**
     * Method the get the client if any
     * @param activity
     * @param googleSignInOptions
     * @return
     */
    GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions);

    /**
     * Getter to get the intent
     * @return Intent
     */
    Intent getSignInIntent();
}
