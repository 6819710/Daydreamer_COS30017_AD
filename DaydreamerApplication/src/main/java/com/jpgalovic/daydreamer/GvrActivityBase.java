package com.jpgalovic.daydreamer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.jpgalovic.daydreamer.model.Util;

import javax.microedition.khronos.egl.EGLConfig;

import static com.jpgalovic.daydreamer.model.Util.checkGLError;

public class GvrActivityBase extends GvrActivity implements GvrView.StereoRenderer {
    private static final String TAG="ACTIVITY_BASE";

    // OpenGL Attributes.
    private int GLESObejctProgram;
    private int GLESViewProjectionAttribute;
    private int GLESObjectProjectionAttribute;
    private int GLESObjectUVAttribute;

    // Cameras, Views and Projection Mapping.
    private float[] camera;
    private float[] view;
    private float[] headView;
    private float[] headRotation;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Initialise Cameras, Views and Projection Mapping.
        // Note: Open gl uses float array as 4x4 Matrix.
        camera = new float[16];
        view = new float[16];
        headView = new float[16];
        headRotation = new  float[16];

        // Init GVR View
        initialiseGvrView();
    }

    /**
     * Initialises Gvr View.
     */
    private void initialiseGvrView() {
        setContentView(R.layout.common_ui);
        GvrView gvrView = findViewById(R.id.gvr_view);

        gvrView.setEGLConfigChooser(8,8,8,8,16,8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);

        // Enable Cardboard-trigger feedback.
        gvrView.enableCardboardTriggerEmulation();

        // Enable async re-projection to de-sync the app frame-rate from the display frame-rate,
        // this allows for smoother and more immersive interaction in the event that the
        // clock-rate becomes throttled.
        if (gvrView.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        setGvrView(gvrView);
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        //Build Camera Matrix
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);

        //Get headView.
        headTransform.getHeadView(headRotation, 0);

        //Update Audio Engine
        headTransform.getQuaternion(headRotation, 0);
        //TODO: audio_engine.setHearRotation(headRotation[0], headRotation[1], headRotation[2], headRotation[3]);
        //TODO: audio_engine.update();

        checkGLError(TAG, "onNewFrame");
    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Clear screen, colour may or may not matter depending on weather background is fully
        // obstructed by an object, however the buffer should still be cleared to improve performance.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Apply the transformations to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);
        float[] perspective = eye.getPerspective(getResources().getFloat(R.dimen.Z_FAR), getResources().getFloat(R.dimen.Z_NEAR));

        // Draw/Render State.
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        // Update State.
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");

        // Setup OpenGL Parameters
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GLESObejctProgram = Util.compileProgram(TAG, getResources().getStringArray(R.array.OBJECT_VERTEX_SHADER), getResources().getStringArray(R.array.OBJECT_FRAGMENT_SHADER));
        GLESObjectProjectionAttribute = GLES20.glGetAttribLocation(GLESObejctProgram, "a_Position");
        GLESObjectUVAttribute = GLES20.glGetAttribLocation(GLESObejctProgram, "a_UV");
        GLESViewProjectionAttribute = GLES20.glGetUniformLocation(GLESObejctProgram, "u_MVP");

        Util.checkGLError(TAG, "OnSurfaceCreated");

        // Load State Engine and Data.
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");
        super.onCardboardTrigger();
    }
}
