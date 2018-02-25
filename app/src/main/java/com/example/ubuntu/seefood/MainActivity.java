package com.example.ubuntu.seefood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ubuntu.seefood.env.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Logger LOGGER = new Logger();
    private final int REQUEST_CODE=1;
    private final int RESULT_CODE_DETECTOR=2;
    private final int RESULT_CODE_SETTINGS=3;
    private ArrayList<String> objects;
    private ObjectAdapter mAdapter;
    private ListView objectListView;
    private TextView hello_msg;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        // Example of a call to a native method
        // TextView tv = (TextView) findViewById(R.id.no_objects_found);
        // This line prints "Hello from SeeFood" from native-lib.cpp
        // tv.setText(stringFromJNI());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CODE_DETECTOR && requestCode==REQUEST_CODE){
            displayDetectedObjectsSummary(data);
        }else if(resultCode==RESULT_CODE_SETTINGS && requestCode==REQUEST_CODE){
            if(data.getStringExtra(SettingsActivity.DETECTOR_NAME).equals(getString(R.string.settings_detector_ms_coco_value))) {
                Toast.makeText(getApplicationContext(), "Detector : MS COCO", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Detector : SeeFood", Toast.LENGTH_LONG).show();
            }
        } else{
            //Toast.makeText(getApplicationContext(),"Request and result code don't match!",Toast.LENGTH_LONG).show();
            LOGGER.d("Request and result code don't match!");
        }
    }

    private void displayDetectedObjectsSummary(Intent data){
        //Toast.makeText(getApplicationContext(),"Request and result code match!",Toast.LENGTH_LONG).show();
        Bundle results = data.getBundleExtra("resultsBundle");
        int n = results.size();

        // Below code is written to find count of each class and its mean confidence score
        HashMap<String, Integer>  classCount = new HashMap<>();
        HashMap<String, Double> classConfidence = new HashMap<>();
        for(int i=1;i<=n;i++) {
            // temp = "Class:(name), Confidence:(score)"
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
            if(classConfidence.containsKey(class_key)){
                classConfidence.put(class_key, classConfidence.get(class_key)+score_key);
            }else{
                classConfidence.put(class_key, score_key);
            }
        }

//        String[] arr = new String[classCount.size()];
//        int counter=0;
//        DecimalFormat df = new DecimalFormat("##.##");
//        for(String class_key: classCount.keySet()) {
//            int count = classCount.get(class_key);
//            arr[counter++] = "Object:             " + class_key + "\n" + "Frames:           " + count +
//                    "\n" + "Confidence:    " +
//                    df.format(classConfidence.get(class_key)/count) +"%";
//        }

        objects = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("##.#");
        for(String class_key: classCount.keySet()) {
            int count = classCount.get(class_key);
            objects.add(class_key + "," + count + "," + df.format(classConfidence.get(class_key)/count));
        }

        // Code to populate ListView using above objects ArrayList
        mAdapter = new ObjectAdapter(this, objects);
        objectListView = (ListView) findViewById(R.id.list);
        objectListView.setAdapter(mAdapter);

        hello_msg = (TextView) findViewById(R.id.no_objects_found);
        if(mAdapter.getCount()>0){
            hello_msg.setVisibility(View.GONE);
        }else{
            hello_msg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(settingsIntent, 1);
            return true;
        } else if(id == R.id.action_about){
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
