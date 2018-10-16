package ch.epfl.sweng.runpharaa.Firebase.Authentification;

import ch.epfl.sweng.runpharaa.login.LoginActivity;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAuthReal implements GoogleAuthInterface {

    public final GoogleAuthReal GoogleAuth = new GoogleAuthReal();

    @Override
    public GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions) {
        return getClient(activity, googleSignInOptions);
    }

    @Override
    public Intent getSignInIntent() {
        return  Auth.GoogleSignInApi.getSignInIntent(LoginActivity.mGoogleSignInClient.asGoogleApiClient());
    }
}
