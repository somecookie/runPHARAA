package ch.epfl.sweng.runpharaa.user.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;


public class SettingsActivity extends AppCompatPreferenceActivity {
    public static final String PREF_KEY_RADIUS = "pref_radius";
    public static final String PREF_KEY_TIME_INTERVAL = "time_interval";
    public static final String PREF_KEY_MIN_TIME_INTERVAL = "min_time_interval";
    public static final String PREF_KEY_MIN_DISTANCE = "min_distance_interval";
    public static final String PREF_KEY_RESET_PREFS = "reset_prefs";
    public static final String PREF_KEY_CLEAR_CACHE = "clear_cache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_account :
                AlertDialog alertDialog = deleteUserConfirmation();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog deleteUserConfirmation()
    {
        AlertDialog deleteUserDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.delete)
                .setMessage(R.string.want_to_delete_your_account)
                .setIcon(R.drawable.ic_delete)

                .setPositiveButton(R.string.delete, (dialog, whichButton) -> {
                    dialog.dismiss();
                    UserDatabaseManagement.deleteUser(User.instance);
                    signOut();
                })

                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();
        return deleteUserDialogBox;

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
                        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        finish();
                    }
                });
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            // Initialise all values for the first time
            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
            for(String key : sp.getAll().keySet())
                onSharedPreferenceChanged(sp, key);
            // Add reset preferences "button"
            findPreference(PREF_KEY_RESET_PREFS).setOnPreferenceClickListener(resetPrefsListener);
            // Add clear cache "button"
            findPreference(PREF_KEY_CLEAR_CACHE).setOnPreferenceClickListener(clearCacheListener);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch(key) {
                case PREF_KEY_RADIUS: {
                    Preference p = findPreference(key);
                    float prefRadius = getFloat(sharedPreferences, key, 2f);
                    User.instance.setPreferredRadius(prefRadius);
                    String s = prefRadius + " km";
                    p.setSummary(s);
                    break;
                }
                case PREF_KEY_MIN_DISTANCE: {
                    Preference p = findPreference(key);
                    int minDistance = getInt(sharedPreferences, key, 5);
                    GpsService.getInstance().setMinDistanceInterval(minDistance);
                    String s = minDistance + " m";
                    p.setSummary(s);
                    break;
                }
                case PREF_KEY_TIME_INTERVAL: {
                    Preference p = findPreference(key);
                    int time = getInt(sharedPreferences, key, 5);
                    GpsService.getInstance().setTimeInterval(time);
                    String s = time + " s";
                    p.setSummary(s);
                    break;
                }
                case PREF_KEY_MIN_TIME_INTERVAL: {
                    Preference p = findPreference(key);
                    int minTime = getInt(sharedPreferences, key, 1);
                    GpsService.getInstance().setMinTimeInterval(minTime);
                    String s = minTime + " s";
                    p.setSummary(s);
                    break;
                }
            }
        }
    }

    /**
     * Helper function to use instead of sharedPreference.getFloat(key, default) because EditTextPreferences use Strings for values
     * @param sp the shared preferences
     * @param key the key of the preference
     * @param defaultValue the default value
     * @return the float contained in the preference
     */
    public static float getFloat(SharedPreferences sp, String key, float defaultValue) {
        String val = sp.getString(key, defaultValue+"");
        return Float.parseFloat(val);
    }

    /**
     * Helper function to use instead of sharedPreference.getInt(key, default) because EditTextPreferences use Strings for values
     * @param sp the shared preferences
     * @param key the key of the preference
     * @param defaultValue the default value
     * @return the integer contained in the preference
     */
    public static int getInt(SharedPreferences sp, String key, int defaultValue) {
        String val = sp.getString(key, defaultValue+"");
        return Integer.parseInt(val);
    }

    private static Preference.OnPreferenceClickListener resetPrefsListener = preference ->  {
            // Clear the preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            // Reset the default values
            PreferenceManager.setDefaultValues(preference.getContext(), R.xml.preferences, true);
            Toast.makeText(preference.getContext(), "Preferences were reset !", Toast.LENGTH_SHORT).show();
            return true;
        };

    private static Preference.OnPreferenceClickListener clearCacheListener = preference -> {
            ImageLoader.getLoader(preference.getContext()).clearCache();
            Toast.makeText(preference.getContext(), "Cache cleared !", Toast.LENGTH_SHORT).show();
            return true;
        };
}