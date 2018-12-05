package ch.epfl.sweng.runpharaa.login.google;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAuthenticationReal extends GoogleAuthentication {

    private GoogleSignInClient gs;

    @Override
    public GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions) {
        gs = GoogleSignIn.getClient(activity, googleSignInOptions);
        return gs;
    }

    @Override
    public Intent getSignInIntent() {
        return  gs.getSignInIntent();
    }
}
