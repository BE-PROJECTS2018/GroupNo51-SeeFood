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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE=1;
    private final int RESULT_CODE=2;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.no_objects_found);
        // This line prints "Hello from SeeFood" from native-lib.cpp
        // tv.setText(stringFromJNI());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_CODE && requestCode==REQUEST_CODE){
            displayDetectedObjectsSummary(data);
        }else{
            Toast.makeText(getApplicationContext(),"Request and result code don't match!",Toast.LENGTH_LONG).show();
        }
    }

    private void displayDetectedObjectsSummary(Intent data){
        //Toast.makeText(getApplicationContext(),"Request and result code match!",Toast.LENGTH_LONG).show();
        Bundle results = data.getBundleExtra("resultsBundle");
        int n = results.size();
//        Log.d("MainActivity.java","displayDetectedObjects.n: "+n);

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

//            Log.d("MainActivity.java","displayDetectedObjects.temp: "+temp);
//            Log.d("MainActivity.java","displayDetectedObjects.pair: "+pair[0]+", "+ pair[1]);
//            Log.d("MainActivity.java","displayDetectedObjects.className: "+className[0]+", "+className[1]);
//            Log.d("MainActivity.java","displayDetectedObjects.confidenceScore: "+confidenceScore[0]+", "+confidenceScore[1]);
//            Log.d("MainActivity.java","displayDetectedObjects.class_key: "+class_key);
//            Log.d("MainActivity.java","displayDetectedObjects.score_key: "+score_key);


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

        String[] arr = new String[classCount.size()];
        int counter=0;
        DecimalFormat df = new DecimalFormat("##.##");
        for(String class_key: classCount.keySet()) {
            int count = classCount.get(class_key);
            arr[counter++] = "Object:             " + class_key + "\n" + "Frames:           " + count +
                    "\n" + "Confidence:    " +
                    df.format(classConfidence.get(class_key)/count) +"%";
        }

        // Code to populate ListView using above String[] arr
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        TextView hello_msg = (TextView) findViewById(R.id.no_objects_found);
        if(adapter.getCount()>0){
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
