package com.example.ubuntu.seefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ubuntu.seefood.env.Logger;

/**
 * Created by ubuntu on 24/2/18.
 */

public class SettingsActivity extends AppCompatActivity {

    private static int RESULT_CODE = 0;
    private static final Logger LOGGER = new Logger();
    private static String stringValue;
    public static final String DETECTOR_NAME = "detector_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(DETECTOR_NAME, stringValue);
        setResult(RESULT_CODE, intent);
        super.onBackPressed();
    }

    public static class DetectorPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_xml);

            Preference detector = findPreference(getString(R.string.settings_detector_key));
            bindPreferenceSummaryToValue(detector);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            stringValue = value.toString();
            LOGGER.d("Preference changed to "+stringValue);
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            RESULT_CODE = 3;
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }

}
