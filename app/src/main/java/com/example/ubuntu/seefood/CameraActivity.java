package com.example.ubuntu.seefood;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ubuntu on 3/2/18.
 */

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    public void closeCameraActivity(View view){
        finish();
    }
}
