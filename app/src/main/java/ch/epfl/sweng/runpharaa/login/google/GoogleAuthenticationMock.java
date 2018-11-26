package ch.epfl.sweng.runpharaa.login.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleAuthenticationMock extends GoogleAuthentication {

    private Context context;

    GoogleAuthenticationMock(Context c) {
        context = c;
    }

    @Override
    public GoogleSignInClient getClient(Activity activity, GoogleSignInOptions googleSignInOptions) {
        return null;
    }

    @Override
    public Intent getSignInIntent() {
        return new Intent(context, FakeGoogleSignInActivity.class);
    }

}
