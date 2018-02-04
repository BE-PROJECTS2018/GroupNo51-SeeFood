package com.example.ubuntu.seefood;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import com.example.ubuntu.seefood.env.Logger;
import com.example.ubuntu.seefood.env.ImageUtils;

import java.nio.ByteBuffer;


/**
 * Created by ubuntu on 3/2/18.
 */

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity implements ImageReader.OnImageAvailableListener {

    private static final Logger LOGGER = new Logger();
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private View mLayout;
    private boolean useCamera2API;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    protected int previewWidth = 0;
    protected int previewHeight = 0;
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mLayout = findViewById(R.id.main_layout);

        if (hasPermission()) {
            setFragment();
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
                setFragment();
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

    /****************** Code to display camera preview in FrameLayout **********************/
    protected void setFragment() {
        String cameraId = chooseCamera();

        Fragment fragment;
        if (useCamera2API) {
            CameraConnectionFragment camera2Fragment =
                    CameraConnectionFragment.newInstance(
                            new CameraConnectionFragment.ConnectionCallback() {
                                @Override
                                public void onPreviewSizeChosen(final Size size, final int rotation) {
                                    previewHeight = size.getHeight();
                                    previewWidth = size.getWidth();
//                                    CameraActivity.this.onPreviewSizeChosen(size, rotation);
                                }
                            },
                            this,
                            getLayoutId(),
                            getDesiredPreviewFrameSize());

            camera2Fragment.setCamera(cameraId);
            fragment = camera2Fragment;
        }
        else {
//            fragment =
//                    new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
            fragment=null;
            Log.d("CameraActivity.java","setFragment(): fragment not initialised!");
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String chooseCamera() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                        || isHardwareLevelSupported(characteristics,
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
                LOGGER.i("Camera API lv2?: %s", useCamera2API);
                return cameraId;
            }
        } catch (CameraAccessException e) {
            LOGGER.e(e, "Not allowed to access camera");
        }

        return null;
    }

    // Returns true if the device supports the required hardware level, or better.
    private boolean isHardwareLevelSupported(
            CameraCharacteristics characteristics, int requiredLevel) {
        int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        // deviceLevel is not LEGACY, can use numerical sort
        return requiredLevel <= deviceLevel;
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        //We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = imageReader.acquireNextImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            Trace.beginSection("imageAvailable");
            final Image.Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.convertYUV420ToARGB8888(
                                    yuvBytes[0],
                                    yuvBytes[1],
                                    yuvBytes[2],
                                    previewWidth,
                                    previewHeight,
                                    yRowStride,
                                    uvRowStride,
                                    uvPixelStride,
                                    rgbBytes);
                        }
                    };

            postInferenceCallback =
                    new Runnable() {
                        @Override
                        public void run() {
                            image.close();
                            isProcessingFrame = false;
                        }
                    };

            //processImage();
        } catch (final Exception e) {
            LOGGER.e(e, "Exception!");
            Trace.endSection();
            return;
        }
        Trace.endSection();
    }

    protected void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    /****************** Methods to be implemented by DetectorActivity.java ********************/
    //protected abstract void processImage();
    //protected abstract void onPreviewSizeChosen(final Size size, final int rotation);
    protected int getLayoutId(){
        return R.layout.camera_connection_fragment_tracking;
    }
    protected Size getDesiredPreviewFrameSize(){
        return DESIRED_PREVIEW_SIZE;
    }

}
