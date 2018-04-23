package com.example.ubuntu.seefood.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ubuntu.seefood.R;
import com.example.ubuntu.seefood.env.Logger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ubuntu on 11/3/18.
 */

public class PreferencesActivity extends AppCompatActivity {

    private static Boolean allergy_preferences_changed = false;
    private static Boolean courses_preferences_changed = false;
    private static Boolean cuisines_preferences_changed = false;
    private static Boolean diet_preferences_changed = false;
    private static Boolean flavor_preferences_changed = false;
    private static Boolean max_prep_time_changed = false;
    private Boolean anythingChanged = false;

    private Logger LOGGER = new Logger();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, Object> updates = new HashMap<>();
        anythingChanged = allergy_preferences_changed | courses_preferences_changed | cuisines_preferences_changed
                | diet_preferences_changed | flavor_preferences_changed | max_prep_time_changed;
        if (allergy_preferences_changed) {

            // Initialising HasMap with default values
            for (String s : getResources().getStringArray(R.array.allergy_values)) {
                updates.put("allergies." + s, false);
            }

            // Now updating HashMap with the selected values from MultiListPreference
            Set<String> selections = sharedPrefs.getStringSet(getString(R.string.allergies_preference_key), null);
            for (String s : selections) {
                updates.put("allergies." + s, true);
                LOGGER.d("allergies." + s);
            }

            allergy_preferences_changed = false;
        }
        if (courses_preferences_changed) {

            for (String s : getResources().getStringArray(R.array.courses_values)) {
                updates.put("courses." + s, false);
            }

            Set<String> selections = sharedPrefs.getStringSet(getString(R.string.courses_preferences_key), null);
            for (String s : selections) {
                updates.put("courses." + s, true);
                LOGGER.d("courses." + s);
            }

            courses_preferences_changed = false;
        }
        if (cuisines_preferences_changed) {

            for (String s : getResources().getStringArray(R.array.cuisines_values)) {
                updates.put("cuisines." + s, false);
            }

            Set<String> selections = sharedPrefs.getStringSet(getString(R.string.cuisines_preferences_key), null);
            for (String s : selections) {
                updates.put("cuisines." + s, true);
                LOGGER.d("cuisines." + s);
            }
            cuisines_preferences_changed = false;
        }
        if (diet_preferences_changed) {

            for (String s : getResources().getStringArray(R.array.diet_values)) {
                updates.put("diet." + s, false);
            }

            Set<String> selections = sharedPrefs.getStringSet(getString(R.string.diet_preferences_key), null);
            for (String s : selections) {
                updates.put("diet." + s, true);
                LOGGER.d("diet." + s);
            }
            diet_preferences_changed = false;
        }
        if (flavor_preferences_changed) {

            for (String s : getResources().getStringArray(R.array.flavor_values)) {
                updates.put("flavors." + s, false);
            }

            Set<String> selections = sharedPrefs.getStringSet(getString(R.string.flavor_preferences_key), null);
            for (String s : selections) {
                updates.put("flavors." + s, true);
                LOGGER.d("flavors." + s);
            }
            flavor_preferences_changed = false;
        }
        if (max_prep_time_changed) {
            String selection = sharedPrefs.getString(getString(R.string.max_prep_time_key), null);
            updates.put("maxPrepTimeInSeconds", selection);
            LOGGER.d("maxPrepTimeInSeconds " + selection);
            max_prep_time_changed = false;
        }

        if (anythingChanged) {
            // Accessing a Cloud Firestore instance
            FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userEmail = mAuth.getCurrentUser().getEmail();
            DocumentReference docRef = firestoreDb.collection("users").document(userEmail);
            docRef.update(updates);
            Toast.makeText(getApplicationContext(), "Preferences saved", Toast.LENGTH_LONG).show();
            anythingChanged = false;
        }

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

        return super.onOptionsItemSelected(item);
    }

    public static class RecipePreferenceFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        private final Logger LOGGER = new Logger();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                addPreferencesFromResource(R.xml.preferences);
            } catch (Exception e) {
                LOGGER.d("addPreferencesFromResource() exception: " + e);
                throw e;
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            LOGGER.d("Preference changed for " + key);
            if (key.equals(getString(R.string.allergies_preference_key))) {
                allergy_preferences_changed = true;
            } else if (key.equals(getString(R.string.courses_preferences_key))) {
                courses_preferences_changed = true;
            } else if (key.equals(getString(R.string.cuisines_preferences_key))) {
                cuisines_preferences_changed = true;
            } else if (key.equals(getString(R.string.diet_preferences_key))) {
                diet_preferences_changed = true;
            } else if (key.equals(getString(R.string.flavor_preferences_key))) {
                flavor_preferences_changed = true;
            } else if (key.equals(getString(R.string.max_prep_time_key))) {
                max_prep_time_changed = true;
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
