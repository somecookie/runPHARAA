package ch.epfl.sweng.runpharaa.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;


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
                    User.instance.getService().setMinDistanceInterval(minDistance);
                    String s = minDistance + " m";
                    p.setSummary(s);
                    break;
                }
                case PREF_KEY_TIME_INTERVAL: {
                    Preference p = findPreference(key);
                    int time = getInt(sharedPreferences, key, 5);
                    User.instance.getService().setTimeInterval(time);
                    String s = time + " s";
                    p.setSummary(s);
                    break;
                }
                case PREF_KEY_MIN_TIME_INTERVAL: {
                    Preference p = findPreference(key);
                    int minTime = getInt(sharedPreferences, key, 1);
                    User.instance.getService().setMinTimeInterval(minTime);
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