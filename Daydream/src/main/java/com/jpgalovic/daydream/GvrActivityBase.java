package com.jpgalovic.daydream;

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

import javax.microedition.khronos.egl.EGLConfig;

import com.jpgalovic.daydream.model.Navigation;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.TexturedMeshObject;
import com.jpgalovic.daydream.model.util.Util;
import com.jpgalovic.daydream.model.util.Values;

public class GvrActivityBase extends GvrActivity implements GvrView.StereoRenderer{
    private static final String TAG="ACTIVITY_BASE";

    // OpenGL Parameters
    private int objectProgram;

    private int objectPositionParam;
    private int objectUvParam;
    private int objectModelViewProjectionParam;

    // Cameras, Views and Projection Mapping
    private float[] camera;
    private float[] view;

    private float[] headView;
    private float[] headRotation;

    // State Engine Components
    State state;

    Navigation navigation;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Initialise Cameras, Views and Projection Mapping
        // Note: vectors are in form of float array as required by OpenGL
        camera = new float[16];
        view = new float[16];

        headView = new float[16];
        headRotation = new float[16];

        // Construct State Engine Components.
        navigation = new Navigation();

        // Initialise GVR View.
        initialiseGvrView();
    }

    /**
     * Initialises Google VR View.
     */
    private void initialiseGvrView() {
        // Initialise the GvrView.
        setContentView(R.layout.common_ui);
        GvrView gvrView = findViewById(R.id.gvr_view);

        gvrView.setEGLConfigChooser(8,8,8,8,16,8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(false);
        
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

    /**
     * Handles onNewFrame
     * @param headTransform contains transformations since last frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Build camera matrix.
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);

        // Get headView.
        headTransform.getHeadView(headView, 0);

        // Update Audio Engine
        Util.checkGLError("onNewFrame");
    }

    /**
     * Draws a frame for a given eye.
     * @param eye the eye to render, includes all transformations.
     */
    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Clear screen, colour may or may not matter depending on weather background is fully
        // obstructed by an object, however the buffer should still be cleared to improve performance.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Apply the transformations to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);
        float[] perspective = eye.getPerspective(Values.Z_NEAR, Values.Z_FAR);

        // Draw each object.
        state.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    /**
     * Handles onFinishFrame
     */
    @Override
    public void onFinishFrame(Viewport viewport) {
        state = state.update();
    }

    /**
     * Creates buffers needed to store information about the 3D environment.
     * Note: OpenGL doesn't use Java arrays, the data needs to be correctly formatted.
     * @param eglConfig the EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");

        // Set OpenGL Parameters
        GLES20.glClearColor(128.0f, 128.0f, 128.0f, 128.0f);

        objectProgram = Util.compileProgram(Values.OBJECT_VERTEX_SHADER, Values.OBJECT_FRAGMENT_SHADER);

        objectPositionParam = GLES20.glGetAttribLocation(objectProgram, "a_Position");
        objectUvParam = GLES20.glGetAttribLocation(objectProgram, "a_UV");
        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objectProgram, "u_MVP");

        Util.checkGLError("onSurfaceCreated");

        // Load Objects
        navigation.init(this, objectPositionParam, objectUvParam);

        state = navigation;
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG,"onSurfaceChanged");
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }
}
