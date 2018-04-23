package com.example.ubuntu.seefood.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ubuntu.seefood.R;
import com.example.ubuntu.seefood.detector.TensorFlowYoloDetector;

/**
 * Created by ubuntu on 25/2/18.
 */

public class AboutActivity extends AppCompatActivity {

    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TensorFlowYoloDetector.LABELS);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(mAdapter);

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
}
