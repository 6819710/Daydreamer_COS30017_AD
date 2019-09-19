package com.jpgalovic.daydreamer;

import android.os.Bundle;
import android.util.Log;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * A VR Navigation Activity
 *
 * <p>This activity presents the user a scene consisting of a room that contains a desk with an
 * old school style computer and floating objects that represent the various mini games and
 * demonstrations that are available to the user.</p>
 *
 * <p>Upon selecting the computer screen or one of the floating objects, the user is taken to the
 * relevant activity, respectively the source repository using the devices default web browser or
 * the given mini game or demonstration.</p>
 */
public class Navigation extends GvrActivity implements GvrView.StereoRenderer {
    private static final String TAG = "NavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGvrView();
    }

    public void initializeGvrView() {
        setContentView(R.layout.common_ui);

        GvrView gvrView = findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8,8,8,8,16,8);

        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);

        // Enable Cardboard-trigger feedback.
        gvrView.enableCardboardTriggerEmulation();

        // Async reprojection decouples the app frame rate from the display frame rate,
        // this allows more immersive interaction event when the clock rate is throttled.
        if(gvrView.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(this,true);
        }

        setGvrView(gvrView);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    @Override
    public void onDrawEye(Eye eye) {

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }


    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        //TODO: Handle Object Detection and Activity Transitions.

        super.onCardboardTrigger();
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }
}
