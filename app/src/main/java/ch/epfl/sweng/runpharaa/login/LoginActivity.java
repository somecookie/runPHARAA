package ch.epfl.sweng.runpharaa.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.HashSet;

import ch.epfl.sweng.runpharaa.Firebase.Authentification.FirebaseAuth;
import ch.epfl.sweng.runpharaa.Firebase.Authentification.FirebaseAuthInterface;
import ch.epfl.sweng.runpharaa.Firebase.Authentification.Google.GoogleAuth;
import ch.epfl.sweng.runpharaa.Firebase.Authentification.Google.GoogleAuthInterface;
import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.User;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.utils.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private final static int GPS_PERMISSIONS_REQUEST_CODE = 1;


    //private static final int RC_SIGN_IN = 1;
    //Needed public to mock access
    private GoogleAuthInterface mGoogleAuth;
    public static GoogleSignInClient mGoogleSignInClient;
    //Shared instance of the FirebaseAuth
    private FirebaseAuthInterface mAuth;
    private LatLng lastLocation = new LatLng(46.520566, 6.567820);
    private Location l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestPermissions();

        //add listener to the buttons
        findViewById(R.id.sign_in_button_google).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //TODO: see if we need to request additional scopes to access Google APIs
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAuth = GoogleAuth.getInstance();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = mGoogleAuth.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                // TODO: setting a fake user for now, change it later
                User.set("Fake User", 2000, Uri.parse(""), new HashSet<Integer>(), new HashSet<Integer>(), lastLocation, false, "Fake User");
                launchApp();
                break;
            case R.id.sign_in_button_google:
                signIn();
                break;
            default:
                throw new AssertionError("This button does not exist.");
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
                Log.w(TAG, "Google sign in failed", e);
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
            if (l != null) {
                lastLocation = new LatLng(l.getLatitude(), l.getLongitude());
            }
            Toast.makeText(getBaseContext(), getResources().getString(R.string.welcome) + " " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            User.set(currentUser.getDisplayName(), 2000, currentUser.getPhotoUrl(), new HashSet<Integer>(), new HashSet<Integer>(), lastLocation, false, currentUser.getUid());
            launchApp();
        } else {
            findViewById(R.id.email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button_google).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
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
        startActivity(launchIntent);
        finish();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User.set(user.getDisplayName(), 2000, user.getPhotoUrl(), new HashSet<Integer>(), new HashSet<Integer>(), lastLocation, false, user.getUid());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSIONS_REQUEST_CODE: {
                Log.i("cunt", grantResults.length+" hh");
                if (grantResults.length != 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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

        l = Util.getCurrLocation(this);
        if (l != null) {
            lastLocation = new LatLng(l.getLatitude(), l.getLongitude());
        }

        return false;
    }

}
