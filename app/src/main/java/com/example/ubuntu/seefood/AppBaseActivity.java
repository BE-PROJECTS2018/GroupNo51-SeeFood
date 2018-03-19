package com.example.ubuntu.seefood;

/*
* This is a simple and easy approach to reuse the same
* navigation drawer on your other activities. Just create
* a base layout that conains a DrawerLayout, the
* navigation drawer and a FrameLayout to hold your
* content view. All you have to do is to extend your
* activities from this class to set that navigation
* drawer. Happy hacking :)
* P.S: You don't need to declare this Activity in the
* AndroidManifest.xml. This is just a base class.
*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.seefood.env.Logger;
import com.example.ubuntu.seefood.menu.AboutActivity;
import com.example.ubuntu.seefood.menu.PreferencesActivity;
import com.example.ubuntu.seefood.menu.SettingsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AppBaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_CODE = 0;
    private final int RESULT_CODE_SETTINGS = 3;
    public FirebaseAuth mAuth;
    protected DrawerLayout mDrawerLayout;
    protected TextView nav_header_textview;
    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources. Easy
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;
    private Logger LOGGER = new Logger();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The base layout that contains your navigation drawer.
        super.setContentView(R.layout.app_base_layout);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        view_stub = findViewById(R.id.content_frame);
        navigation_view = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerMenu = navigation_view.getMenu();
        for (int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        mAuth = FirebaseAuth.getInstance();
        nav_header_textview = navigation_view.getHeaderView(0).findViewById(R.id.nav_header_title);

        // and so on...
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Hide the "Sign out" option
            drawerMenu.getItem(1).setVisible(false);
            drawerMenu.getItem(0).setVisible(true);
            nav_header_textview.setText("Please Sign In");
        } else {
            // Hide the "Sign in" option
            drawerMenu.getItem(0).setVisible(false);
            drawerMenu.getItem(1).setVisible(true);
            nav_header_textview.setText(user.getDisplayName());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
         * so that, we can make other activity implementations looks like normal activity subclasses.
         */
    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Checking results from settings activity
        if (resultCode == RESULT_CODE_SETTINGS && requestCode == REQUEST_CODE) {
            Toast.makeText(getApplicationContext(), "Detector : "
                    + data.getStringExtra(SettingsActivity.DETECTOR_NAME), Toast.LENGTH_LONG).show();
        }
        // Checking results from Firebase SignIn activity
        else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(), "Welcome " + user.getDisplayName(),
                        Toast.LENGTH_LONG).show();
                completePendingTasksOnSignIn();
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Error: " + response,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        LOGGER.d("onMenuItemClick called!");
        Bundle bundle;
        switch (menuItem.getItemId()) {
            case R.id.action_sign_in:

                // Use this template at various places in your app to record user clicks
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_sign_in");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sign in button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                if (isOffline()) {
                    Toast.makeText(getApplicationContext(), "No network connection",
                            Toast.LENGTH_LONG).show();
                } else {
                    mDrawerLayout.closeDrawers();
                    // Choose authentication providers
//            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }
                break;
            case R.id.action_sign_out:
                // Use this template at various places in your app to record user clicks
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_sign_out");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sign out button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                mDrawerLayout.closeDrawers();
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Signed out successfully",
                                        Toast.LENGTH_LONG).show();
                                completePendingTasksOnSignOut();
                            }
                        });
                break;
            case R.id.action_recipe_prefs:
                // Use this template at various places in your app to record user clicks
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_recipe_prefs");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Preferences button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(), "Please Sign In!", Toast.LENGTH_LONG).show();
                } else {
                    mDrawerLayout.closeDrawers();
                    Intent prefsIntent = new Intent(this, PreferencesActivity.class);
                    startActivity(prefsIntent);
                }
                break;
            case R.id.action_settings:
                // Use this template at various places in your app to record user clicks
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_settings");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Settings button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                mDrawerLayout.closeDrawers();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_CODE);
                break;
            case R.id.action_about:
                // Use this template at various places in your app to record user clicks
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_about");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "About button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                mDrawerLayout.closeDrawers();
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return false;
    }

    private boolean isOffline() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return !(manager != null
                && manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isConnectedOrConnecting());
    }

    protected void completePendingTasksOnSignIn() {
        nav_header_textview.setText(mAuth.getCurrentUser().getDisplayName());
        drawerMenu.getItem(0).setVisible(false);
        drawerMenu.getItem(1).setVisible(true);
        setupUserDatabase();
    }

    private void setupUserDatabase() {
        // Accessing Cloud Firestore instance
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        final String userName = mAuth.getCurrentUser().getDisplayName();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final String userPhnNumber = mAuth.getCurrentUser().getPhoneNumber();
        final DocumentReference docRef = firestoreDb.collection("users").document(userEmail);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        LOGGER.d("User " + userName + " already exists, synchronizing!");
                        synchronizeUserPreferences(docRef);
                    } else {
                        LOGGER.d("Setting up userPreferences for the first time!");

                        String userPhoto;
                        try {
                            userPhoto = mAuth.getCurrentUser().getPhotoUrl().toString();
                        } catch (Exception e) {
                            LOGGER.d("No userPhoto found! " + e);
                            userPhoto = null;
                        }

                        UserPreferences prefs = setupUserPreferences(userName, userEmail, userPhoto, userPhnNumber);
                        docRef.set(prefs)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        LOGGER.d("User " + userName + "'s database initialised!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                LOGGER.d("Error intialising " + userName + "'s database!");
                            }
                        });

                    }
                }
            }
        });
    }

    public void synchronizeUserPreferences(DocumentReference docRef) {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                LOGGER.d("Running synchronizeUserPreferences()!");
                UserPreferences prefs = documentSnapshot.toObject(UserPreferences.class);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

                Map<String, Boolean> selected = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : ((HashMap<String, Boolean>) prefs.getAllergies()).entrySet())
                    if (entry.getValue()) selected.put(entry.getKey(), true);
                editor.putStringSet(getString(R.string.allergies_preference_key), selected.keySet());

                selected = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : ((HashMap<String, Boolean>) prefs.getCourses()).entrySet())
                    if (entry.getValue()) selected.put(entry.getKey(), true);
                editor.putStringSet(getString(R.string.courses_preferences_key), selected.keySet());

                selected = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : ((HashMap<String, Boolean>) prefs.getCuisines()).entrySet())
                    if (entry.getValue()) selected.put(entry.getKey(), true);
                editor.putStringSet(getString(R.string.cuisines_preferences_key), selected.keySet());

                selected = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : ((HashMap<String, Boolean>) prefs.getDiet()).entrySet())
                    if (entry.getValue()) selected.put(entry.getKey(), true);
                editor.putStringSet(getString(R.string.diet_preferences_key), selected.keySet());

                selected = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : ((HashMap<String, Boolean>) prefs.getFlavors()).entrySet())
                    if (entry.getValue()) selected.put(entry.getKey(), true);
                editor.putStringSet(getString(R.string.flavor_preferences_key), selected.keySet());

                editor.putString(getString(R.string.max_prep_time_key), prefs.getMaxPrepTimeInSeconds());

                editor.apply();
                LOGGER.d("Completed synchronizeUserPreferences()!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LOGGER.d("Could not synchronizeUserPreferences()!");
            }
        });

    }

    protected UserPreferences setupUserPreferences(String userName, String userEmail, String userPhoto, String userPhnNumber) {
        String arr[] = getResources().getStringArray(R.array.allergy_values);
        Map<String, Boolean> allergies = new HashMap<>();
        for (String s : arr) {
            allergies.put(s, false);
        }
        arr = getResources().getStringArray(R.array.courses_values);
        Map<String, Boolean> courses = new HashMap<>();
        for (String s : arr) {
            courses.put(s, false);
        }
        arr = getResources().getStringArray(R.array.cuisines_values);
        Map<String, Boolean> cuisines = new HashMap<>();
        for (String s : arr) {
            cuisines.put(s, false);
        }
        arr = getResources().getStringArray(R.array.diet_values);
        Map<String, Boolean> diet = new HashMap<>();
        for (String s : arr) {
            diet.put(s, false);
        }
        arr = getResources().getStringArray(R.array.flavor_values);
        Map<String, Boolean> flavors = new HashMap<>();
        for (String s : arr) {
            flavors.put(s, false);
        }
        return new UserPreferences(userName, userEmail, userPhoto, userPhnNumber, allergies, courses,
                cuisines, diet, flavors, "0");
    }

    protected void completePendingTasksOnSignOut() {
        nav_header_textview.setText("Pleade Sign In");
        drawerMenu.getItem(0).setVisible(true);
        drawerMenu.getItem(1).setVisible(false);
    }
}
