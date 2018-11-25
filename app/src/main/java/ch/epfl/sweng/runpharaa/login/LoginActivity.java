package ch.epfl.sweng.runpharaa.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Map;

import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.login.firebase.FirebaseAuthentication;
import ch.epfl.sweng.runpharaa.login.google.GoogleAuthentication;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private final static int GPS_PERMISSIONS_REQUEST_CODE = 69;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleAuthentication mGoogleAuth;
    private FirebaseAuthentication mAuth;
    private LatLng lastLocation = new LatLng(46.520566, 6.567820);
    private FirebaseUser currentUser;
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoadingAnimation();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        for (Map.Entry e : PreferenceManager.getDefaultSharedPreferences(this).getAll().entrySet())
            System.out.println(e.getKey() + " " + e.getValue());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAuth = GoogleAuthentication.getInstance(LoginActivity.this);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = mGoogleAuth.getClient(this, gso);

        mAuth = FirebaseAuthentication.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        if(!requestPermissions()) {
            currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_google:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getBaseContext(), "Google sign in failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }

    /**
     * Updates the user interface. Launch the app if the user is already signed in.
     *
     * @param currentUser
     */
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            setLoadingText(getText(R.string.loading_data).toString());
            Location l = GpsService.getInstance().getCurrentLocation();
            if(GpsService.getInstance().getCurrentLocation() != null) {
                lastLocation = new LatLng(l.getLatitude(), l.getLongitude());
            }
            float prefRadius = SettingsActivity.getFloat(PreferenceManager.getDefaultSharedPreferences(this), SettingsActivity.PREF_KEY_RADIUS, 2f);
            User.set(currentUser.getDisplayName(), prefRadius, currentUser.getPhotoUrl(), lastLocation, currentUser.getUid());
            UserDatabaseManagement.writeNewUser(User.instance, new Callback<User>() {
                @Override
                public void onSuccess(User value) {
                    User.instance.setFavoriteTracks(value.getFavoriteTracks());
                    User.instance.setCreatedTracks(value.getCreatedTracks());
                    User.instance.setLikedTracks(value.getLikedTracks());
                    User.instance.setFollowedUsers(value.getFollowedUsers());
                    launchApp();
                }

                @Override
                public void onError(Exception e) {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getBaseContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            setContentView(R.layout.activity_login);
            TextView tv = findViewById(R.id.textViewLogin);
            tv.setText("#RunPharaaWayFromTravis!");

            findViewById(R.id.sign_in_button_google).setOnClickListener(this);
            findViewById(R.id.sign_in_button_google).setVisibility(View.VISIBLE);
        }
    }

    private void setLoadingText(String text) {
        TextView textView = findViewById(R.id.loading_text);
        textView.setText(text);
    }

    private void startLoadingAnimation() {
        setContentView(R.layout.loading_screen);
        ImageView imageView = findViewById(R.id.anim_view);
        imageView.setBackgroundResource(R.drawable.animation);
        anim = ((AnimationDrawable)imageView.getBackground());
        anim.start();
    }

    /**
     * Prompts the user to select a Google account to sign in with
     */
    private void signIn() {
        Intent signInIntent = mGoogleAuth.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Launch the main app
     */
    private void launchApp() {
        Intent launchIntent = new Intent(getBaseContext(), MainActivity.class);
        setLoadingText(getText(R.string.almost_there).toString());
        new Thread() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void run() {
                try {
                    sleep(1000);
                    runOnUiThread(() -> {
                        setLoadingText(String.format(getString(R.string.welcome), User.instance.getName()));
                        findViewById(R.id.anim_view).setBackgroundResource(R.drawable.anim_standing);
                    });
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(launchIntent);
                    finish();
                }
            }
        }.start();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(GoogleAuthentication.isTest ? "token" : acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startLoadingAnimation();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(getBaseContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                } else {
                    requestPermissions();
                }
            }
        }
    }

    /**
     * Verifies if we need to ask for the GPS permissions
     *
     * @return true if we need to request permissions, false otherwise
     */
    private boolean requestPermissions() {
        if (Build.VERSION.SDK_INT > 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSIONS_REQUEST_CODE);
            return true;
        }
        startService(new Intent(getBaseContext(), GpsService.getInstance().getClass()));
        return false;
    }
}
