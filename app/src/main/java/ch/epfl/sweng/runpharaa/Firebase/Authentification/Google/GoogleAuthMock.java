package ch.epfl.sweng.runpharaa.Firebase.Authentification.Google;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAuthMock implements GoogleAuthInterface {

    @Override
    public GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions) {
        return null;
    }

    @Override
    public Intent getSignInIntent() {
        return new Intent();
    }
}
