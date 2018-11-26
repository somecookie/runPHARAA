package ch.epfl.sweng.runpharaa.login.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

public class FakeGoogleSignInActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent();
        i.putExtra("googleSignInStatus", Status.RESULT_SUCCESS);
        i.putExtra("googleSignInAccount", GoogleSignInAccount.createDefault());
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
