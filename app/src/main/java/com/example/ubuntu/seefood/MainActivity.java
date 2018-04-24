package com.example.ubuntu.seefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.seefood.detector.DetectorActivity;
import com.example.ubuntu.seefood.env.Logger;
import com.example.ubuntu.seefood.recipes.RecipesActivity;
import com.example.ubuntu.seefood.recipes.StarredRecipe;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ubuntu on 7/3/18.
 */

public class MainActivity extends AppBaseActivity {

    private static final String LOADING_PHRASE_CONFIG_KEY = "loading_phrase";
    private static String WELCOME_MESSAGE_KEY = "welcome_message";
    private final String TAG = "MainActivity";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    //Recyclerview of mainactivity
    RecyclerView recyclerView;
    MainRecyclerViewAdapter myAdapter;
    ArrayList<String> list;
    private Logger LOGGER = new Logger();
    private TextView mWelcomeTextView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private DrawerLayout mDrawerLayout;
    // Firestore testing
    private TextView mFirestoreText;
    private Button mSaveButton;
    private EditText mEnterText;
    //ListenerRegistration registration;
    private String userName, userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mWelcomeTextView = findViewById(R.id.main_welcome_msg);

        // Facebook SDK's app activation helper
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FloatingActionButton detection_fab = findViewById(R.id.main_detect_fab);
        detection_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use this template at various places in your app to record user clicks
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "main_detect_fab");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity FAB button");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
                intent.putExtra("FromActivity", "MainActivity");
                startActivity(intent);
            }
        });

//        // Use this template at various places in your app to record user clicks
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "button_name");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user_clicks");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        // Testing firebase crashlytics implementation
//        Button crashButton = findViewById(R.id.crash_button);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });

//         getRemoteConfigInstance();

//        mFirestoreText = findViewById(R.id.firestore_text);
//        mEnterText = findViewById(R.id.enter_text);
//        mSaveButton = findViewById(R.id.save_button);
//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCloudFirestoreInstance();
//            }
//        });

        //RecyclerView of main activity


//        ArrayList<StarredRecipe> list = new ArrayList<>();
//        SharedPreferences preferences = getSharedPreferences("Starred", MODE_PRIVATE);
//        for(Map.Entry entry:preferences.getAll().entrySet()){
//            Gson gson = new Gson();
//            String json = entry.getValue().toString();
//            StarredRecipe temp = gson.fromJson(json, StarredRecipe.class);
//            list.add(temp);
//        }
//
//        recyclerView = findViewById(R.id.main_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        myAdapter = new MainRecyclerViewAdapter(list, this);
//        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<StarredRecipe> list = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("Starred", MODE_PRIVATE);
        for(Map.Entry entry:preferences.getAll().entrySet()){
            Gson gson = new Gson();
            String json = entry.getValue().toString();
            StarredRecipe temp = gson.fromJson(json, StarredRecipe.class);
            list.add(temp);
        }
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView textView = (TextView)findViewById(R.id.starMsg);
        if(list.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            recyclerView.invalidate();
        }else {
            textView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            myAdapter = new MainRecyclerViewAdapter(list, this);
            recyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mAuth.getCurrentUser()!=null){
//            setupSnapshotListener();
//        }
    }

//    private void getRemoteConfigInstance(){
//        // Get Remote Config instance.
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//
//        // Create a Remote Config Setting to enable developer mode, which you can use to increase
//        // the number of fetches available per hour during development. See Best Practices in the
//        // README for more information.
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                .build();
//        mFirebaseRemoteConfig.setConfigSettings(configSettings);
//
//        // Set default Remote Config parameter values. An app uses the in-app default values, and
//        // when you need to adjust those defaults, you set an updated value for only the values you
//        // want to change in the Firebase console. See Best Practices in the README for more
//        // information.
//        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
//
//        fetchWelcome();
//    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
//    private void fetchWelcome() {
//
//        mWelcomeTextView.setText(mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));
//        long cacheExpiration;
//        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
//        // retrieve values from the service.
//        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
//            cacheExpiration = 0;
//            WELCOME_MESSAGE_KEY = "welcome_message_dev";
//        } else {
//            cacheExpiration = 3600;
//            WELCOME_MESSAGE_KEY = "welcome_message";
//        }
//
//        // [START fetch_config_with_callback]
//        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
//        // will use fetch data from the Remote Config service, rather than cached parameter values,
//        // if cached parameter values are more than cacheExpiration seconds old.
//        // See Best Practices in the README for more information.
//        mFirebaseRemoteConfig.fetch(cacheExpiration)
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();
//
//                            // After config data is successfully fetched, it must be activated before newly fetched
//                            // values are returned.
//                            mFirebaseRemoteConfig.activateFetched();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Fetch Failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        displayWelcomeMessage();
//                    }
//                });
//        // [END fetch_config_with_callback]
//    }

//    public static class UserData{
//        String name;
//        String email;
//        Map<String, String> preferences;
//
//        public UserData() {}
//
//        public UserData(String name, String email, Map<String, String> prefs){
//            this.name = name;
//            this.email = email;
//            this.preferences = prefs;
//        }
//
//        public String getName(){
//            return name;
//        }
//        public String getEmail(){
//            return email;
//        }
//        public Object getPreferences(){
//            return preferences;
//        }
//    }

//    private void getCloudFirestoreInstance(){
//        if(mAuth.getCurrentUser()==null){
//            Toast.makeText(getApplicationContext(),"Please Sign In!",Toast.LENGTH_LONG).show();
//            return;
//        }
//        // Create a new user with a first and last name
//        Map<String, String> userQuote = new HashMap<>();
//        String userText = mEnterText.getText().toString();
//        userQuote.put("userText",userText);
//
//        UserData userData = new UserData(userName, userEmail, userQuote);
//
//        // Donot create documents by username as they can be same
//        docRef.set(userData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        LOGGER.d("Document saved!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        LOGGER.d("Error saving document! "+e);
//                    }
//                });
//    }
    @Override
    protected void completePendingTasksOnSignOut() {
        super.completePendingTasksOnSignOut();
    }

    @Override
    protected void completePendingTasksOnSignIn() {
        super.completePendingTasksOnSignIn();
//        setupSnapshotListener();
    }

//    private void setupSnapshotListener() {
//        userName = mAuth.getCurrentUser().getDisplayName();
//        userEmail = mAuth.getCurrentUser().getEmail();
//        // Donot create documents by username as they can be same
//        docRef = firestoreDb.collection("users").document(userName);
//        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if(e!=null){
//                    LOGGER.d("addSnapshotListener Exception!"+e);
//                    return;
//                }
//                if(documentSnapshot.exists()){
//                    String quoteName = documentSnapshot.getString("name");
//                    String quoteText = documentSnapshot.getString("preferences.userText");
//                    String source = documentSnapshot.getMetadata().isFromCache() ? "local cache" : "server";
//                    mFirestoreText.setText("\"" + quoteText + "\" -- " + quoteName + " (From " + source + ")");
//                }
//            }
//        });
//    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private void displayWelcomeMessage() {
        // [START get_config_values]
        String welcomeMessage = mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);
        // [END get_config_values]
        mWelcomeTextView.setText(welcomeMessage);
    }

    public void onClickSave(View view) {
        Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
        startActivity(intent);
    }
    // [END display_welcome_message]
}
