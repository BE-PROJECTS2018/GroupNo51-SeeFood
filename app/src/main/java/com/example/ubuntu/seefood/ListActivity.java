package com.example.ubuntu.seefood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.seefood.detector.DetectorActivity;
import com.example.ubuntu.seefood.detector.TensorFlowYoloDetector;
import com.example.ubuntu.seefood.env.Logger;
import com.example.ubuntu.seefood.menu.AboutActivity;
import com.example.ubuntu.seefood.menu.SettingsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Remote Config keys
    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_CODE = 1;
    private final int RESULT_CODE_DETECTOR = 2;
    private final int RESULT_CODE_SETTINGS = 3;
    private Logger LOGGER = new Logger();
    private ArrayList<String> objects;
    private ObjectAdapter mAdapter;
    private ListView objectListView;
    private TextView mWelcomeTextView;
    private FloatingActionsMenu fab_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mWelcomeTextView = findViewById(R.id.no_objects_found);

        fab_menu = findViewById(R.id.left_labels);
        com.getbase.floatingactionbutton.FloatingActionButton detect_fab = findViewById(R.id.detect_fab);
        detect_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_menu.collapse();
                Intent intent = new Intent(ListActivity.this, DetectorActivity.class);
                intent.putExtra("FromActivity", "ListActivity");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton edit_fab = findViewById(R.id.edit_fab);
        edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_menu.collapse();
                addIngredient();
            }
        });

        Intent intent = getIntent();
        if (intent.getStringExtra("FromActivity").equals("DetectorActivity")) {
            displayDetectedObjectsSummary(intent);
        }

    }

    private void addIngredient() {
//        LayoutInflater layoutInflater = this.getLayoutInflater();
//        final  View inflator = layoutInflater.inflate(R.layout.dialog_add_ingredient,null);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(inflator);
//        final EditText et = inflator.findViewById(R.id.added_ingredient);
//
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String value = et.getText().toString();
//                Toast.makeText(getApplicationContext(),"Added: " + value, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // what ever you want to do with No option.
//            }
//        });
//        builder.show();

        final ArrayList selectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Add Ingredients")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(TensorFlowYoloDetector.LABELS_SEEFOOD, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (fab_menu.isExpanded())
            fab_menu.collapse();
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_DETECTOR && requestCode == REQUEST_CODE) {
            displayDetectedObjectsSummary(data);
        } else if (resultCode == RESULT_CODE_SETTINGS && requestCode == REQUEST_CODE) {
            Toast.makeText(getApplicationContext(), "Detector : "
                    + data.getStringExtra(SettingsActivity.DETECTOR_NAME), Toast.LENGTH_LONG).show();
        } else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(getApplicationContext(), "Welcome " + user.getDisplayName(),
                        Toast.LENGTH_LONG).show();
                invalidateOptionsMenu();
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Error: " + response,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(getApplicationContext(),"Request and result code don't match!",Toast.LENGTH_LONG).show();
            LOGGER.d("Request and result code don't match!");
        }
    }

    private void displayDetectedObjectsSummary(Intent data) {

        Bundle results = data.getBundleExtra("resultsBundle");
        int n = results.size();

        // Below code is written to find count of each class and its mean confidence score
        HashMap<String, Integer> classCount = new HashMap<>();
        HashMap<String, Double> classConfidence = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            // Expected format of temp = "Class:(name), Confidence:(score)"
            String temp = results.getString("Object" + i);
            String[] pair = temp.split(",");
            String[] className = pair[0].split(":");
            String[] confidenceScore = pair[1].split(":");
            String class_key = className[1];
            Double score_key = Double.parseDouble(confidenceScore[1]);

            // Updating count for each class in the list of detected objects
            if (classCount.containsKey(class_key)) {
                classCount.put(class_key, classCount.get(class_key) + 1);
            } else {
                classCount.put(class_key, 1);
            }
            // Adding up scores of each class which will be divided by classCount
            // so as to get mean confidenceScore of that class
            if (classConfidence.containsKey(class_key)) {
                classConfidence.put(class_key, classConfidence.get(class_key) + score_key);
            } else {
                classConfidence.put(class_key, score_key);
            }
        }

        objects = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("##.#");
        for (String class_key : classCount.keySet()) {
            int count = classCount.get(class_key);
            objects.add(class_key + "," + count + "," + df.format(classConfidence.get(class_key) / count));
        }

        // Code to populate ListView using above objects ArrayList
        mAdapter = new ObjectAdapter(this, objects);
        objectListView = findViewById(R.id.list);
        objectListView.setAdapter(mAdapter);

        if (mAdapter.getCount() > 0) {
            mWelcomeTextView.setVisibility(View.GONE);
        } else {
            mWelcomeTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem sign_in_item = menu.findItem(R.id.action_sign_in);
        MenuItem sign_out_item = menu.findItem(R.id.action_sign_out);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        LOGGER.d("onCreateOptionsMenu called!");
        if (user == null) {
            sign_out_item.setVisible(false);
        } else {
            sign_in_item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LOGGER.d("onOptionsItemSelected called!");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(settingsIntent, 1);
            return true;
        } else if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (id == R.id.action_sign_in) {
            if (isOffline()) {
                Toast.makeText(getApplicationContext(), "No network connection",
                        Toast.LENGTH_LONG).show();
            } else {
                // Choose authentication providers
//            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }
        } else if (id == R.id.action_sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Signed out successfully",
                                    Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isOffline() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return !(manager != null
                && manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isConnectedOrConnecting());
    }

}
