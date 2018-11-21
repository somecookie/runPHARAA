package ch.epfl.sweng.runpharaa.firebase.authentification.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;

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
