package com.example.ubuntu.seefood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ubuntu.seefood.detector.DetectorActivity;
import com.example.ubuntu.seefood.detector.TensorFlowYoloDetector;
import com.example.ubuntu.seefood.env.Logger;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppBaseActivity {

    private static final String TAG = "ListActivity";
    // Remote Config keys
    private final int REQUEST_CODE = 1;
    private final int RESULT_CODE_DETECTOR = 2;
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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE_DETECTOR) {
            displayDetectedObjectsSummary(data);
        } else {
            LOGGER.d("Request and result code might match in AppBaseActivity!");
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

    protected void completePendingTasksOnSignIn() {
        super.completePendingTasksOnSignIn();
    }

    protected void completePendingTasksOnSignOut() {

    }
}
