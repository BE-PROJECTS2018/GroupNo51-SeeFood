package com.example.ubuntu.seefood.menu;

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

import com.example.ubuntu.seefood.R;
import com.example.ubuntu.seefood.env.Logger;

/**
 * Created by ubuntu on 24/2/18.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String DETECTOR_NAME = "detector_name";
    private static final Logger LOGGER = new Logger();
    // This result code is updated only when onPreferenceChange() method is called
    private static int RESULT_CODE = 0;
    private static String stringValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String yolo_model_filename = sharedPrefs.getString(getString(R.string.settings_detector_key),
                getString(R.string.settings_detector_default));
        if(yolo_model_filename.equals(getString(R.string.settings_detector_ms_coco_value))) {
            intent.putExtra(DETECTOR_NAME, getString(R.string.settings_detector_ms_coco_label));
        }else{
            intent.putExtra(DETECTOR_NAME, getString(R.string.settings_detector_seefood_label));
        }
        setResult(RESULT_CODE, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public static class DetectorPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            Preference detector = findPreference(getString(R.string.settings_detector_key));
            bindPreferenceSummaryToValue(detector);

            Preference threshold = findPreference(getString(R.string.settings_detector_threshold_key));
            bindPreferenceSummaryToValue(threshold);
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
