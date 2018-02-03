package com.example.ubuntu.seefood;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ubuntu on 3/2/18.
 */

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mLayout = findViewById(R.id.main_layout);

        if (hasPermission()) {
            // setFragment();
            Snackbar.make(mLayout,
                    "Camera permission is available.",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }

    // Implementation for "Close" Button in CameraActivity
    public void closeCameraActivity(View view){
        finish();
    }


    /******************** Code Related to Runtime Permissions **********************/
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Camera permission was granted.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Camera permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {

                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // Display a SnackBar with a button to request the missing permission.
                Snackbar.make(mLayout, "Camera access is required to display the camera preview.",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        requestPermissions(new String[] {PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
                    }
                }).show();
            }else {
                Snackbar.make(mLayout,
                        "Permission is not available. Requesting camera permission.",
                        Snackbar.LENGTH_SHORT).show();
                requestPermissions(new String[]{PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
            }
        }
    }


}
