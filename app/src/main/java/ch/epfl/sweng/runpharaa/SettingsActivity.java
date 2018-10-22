package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.runpharaa.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                signOut();
                break;
            default:
                throw new AssertionError("This button does not exists.");
        }
    }

    private void signOut() {

        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.loggedOut), Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }
}
