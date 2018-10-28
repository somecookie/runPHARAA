package ch.epfl.sweng.runpharaa.user;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import ch.epfl.sweng.runpharaa.GpsService;
import ch.epfl.sweng.runpharaa.R;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Bind preferences to show values in description
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_radius)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_time_interval)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_min_time_interval)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_min_distance_interval)));

            // Add reset preferences "button"
            findPreference(getString(R.string.pref_key_reset_prefs)).setOnPreferenceClickListener(resetPrefsListener());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof EditTextPreference) {
                switch (preference.getKey()) {
                    case "pref_radius": {
                        User.instance.setPreferredRadius(Integer.parseInt(stringValue));
                        preference.setSummary(stringValue + " m");
                        break;
                    }
                    case "min_distance_interval": {
                        GpsService.setMinDistanceInterval(Integer.parseInt(stringValue));
                        preference.setSummary(stringValue + " m");
                        break;
                    }
                    case "min_time_interval": {
                        GpsService.setMinTimeInterval(Integer.parseInt(stringValue));
                        preference.setSummary(stringValue + " ms");
                        break;
                    }
                    case "time_interval": {
                        GpsService.setTimeInterval(Integer.parseInt(stringValue));
                        preference.setSummary(stringValue + " ms");
                        break;
                    }
                }
            }
            return true;
        }
    };

    private static Preference.OnPreferenceClickListener resetPrefsListener() {
        return new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                Resources r = preference.getContext().getResources();
                SharedPreferences.Editor editor = preferences.edit();
                // Reset shared preferences
                editor.putString(r.getString(R.string.pref_key_radius), r.getString(R.string.pref_default_radius));
                editor.putString(r.getString(R.string.pref_key_min_time_interval), r.getString(R.string.pref_default_min_time_interval));
                editor.putString(r.getString(R.string.pref_key_time_interval), r.getString(R.string.pref_default_time_interval));
                editor.putString(r.getString(R.string.pref_key_min_distance_interval), r.getString(R.string.pref_default_min_distance_interval));
                editor.commit();
                // Reset values in user and gps service
                User.instance.setPreferredRadius(Integer.parseInt(r.getString(R.string.pref_default_radius)));
                GpsService.setMinTimeInterval(Integer.parseInt(r.getString(R.string.pref_default_min_time_interval)));
                GpsService.setTimeInterval(Integer.parseInt(r.getString(R.string.pref_default_time_interval)));
                GpsService.setMinDistanceInterval(Integer.parseInt(r.getString(R.string.pref_default_min_distance_interval)));
                Toast.makeText(preference.getContext(), "Preferences were reset !", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
    }
}