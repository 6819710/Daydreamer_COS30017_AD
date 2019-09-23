package com.jpgalovic.daydreamer;

import android.content.Intent;
import android.net.Uri;
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

import com.jpgalovic.daydreamer.R;
import com.jpgalovic.daydreamer.model.TexturedMeshObject;
import com.jpgalovic.daydreamer.model.Util;
import com.jpgalovic.daydreamer.model.Values;
import com.jpgalovic.daydreamer.model.game.object.SevenSegmentTimer;

import javax.microedition.khronos.egl.EGLConfig;

public class FindTheBlock extends GvrActivity implements GvrView.StereoRenderer {
    private static final String TAG = "FindTheBlockActivity";

    private static final String[] OBJECT_VERTEX_SHADER_CODE =
            new String[] {
                    "uniform mat4 u_MVP;",
                    "attribute vec4 a_Position;",
                    "attribute vec2 a_UV;",
                    "varying vec2 v_UV;",
                    "",
                    "void main() {",
                    "  v_UV = a_UV;",
                    "  gl_Position = u_MVP * a_Position;",
                    "}",
            };

    private static final String[] OBJECT_FRAGMENT_SHADER_CODE =
            new String[] {
                    "precision mediump float;",
                    "varying vec2 v_UV;",
                    "uniform sampler2D u_Texture;",
                    "",
                    "void main() {",
                    "  // The y coordinate of this sample's textures is reversed compared to",
                    "  // what OpenGL expects, so we invert the y coordinate.",
                    "  gl_FragColor = texture2D(u_Texture, vec2(v_UV.x, 1.0 - v_UV.y));",
                    "}",
            };

    private int objectProgram;

    private int objectPositionParam;
    private int objectUvParam;
    private int objectModelViewProjectionParam;

    // Object Data
    private SevenSegmentTimer sevenSegmentTimer;

    // Cameras, Views and Projection Mapping
    private float[] camera;
    private float[] view;
    private float[] headView;

    private float[] headRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGvrView();

        // Initialize Cameras, Views and Projection Mapping
        camera = new float[16];
        view = new float[16];

        headView = new float[16];
        headRotation = new float[4];
    }

    public void initializeGvrView() {
        setContentView(R.layout.common_ui);

        GvrView gvrView = findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(false);

        gvrView.

        // Enable Cardboard-trigger feedback.
        gvrView.enableCardboardTriggerEmulation();

        // Async reprojection decouples the app frame rate from the display frame rate,
        // this allows more immersive interaction event when the clock rate is throttled.
        if(gvrView.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(this,true);
        }

        setGvrView(gvrView);
    }

    /**
     * Prepares OpenGL ES before drawing a frame.
     *
     * @param headTransform
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        //Build the camera matrix and apply it to the ModelView
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(headView, 0);

        //TODO: UPDATE AUDIO ENGINE.

        Util.checkGLError("onNewFrame");
    }

    /**
     * Draws a frame for an eye
     * @param eye The eye to render. Includes all required transformations.
     */
    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Clear color may not matter if it is completly obscured by an object, however the color
        // buffer is still cleared because it can help imporve performance.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Apply the transformation to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);

        float[] perspective = eye.getPerspective(Values.Z_NEAR, Values.Z_FAR);

        // Build ModelView and ModelView Projection for each object.
        // This calculates the position to draw the object.
        sevenSegmentTimer.draw(perspective, view, objectProgram, objectModelViewProjectionParam);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        // If timer has run out return to menu TODO: Change this to follow design pattern defined in D.2
        if(sevenSegmentTimer.zero()) {
            finish();
        }
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        super.onCardboardTrigger();
    }

    /**
     * Creates the buffers needed to store information about the 3D enviroment.
     *
     * <p>Note: OpenGL doen't use Java arrays, but rather needs data formatted for it to understand.
     * Use of ByteBuffers is hence required.</p>
     *
     * @param eglConfig the EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(128.0f,128.0f,128.0f,128.0f);

        objectProgram = Util.compileProgram(OBJECT_VERTEX_SHADER_CODE, OBJECT_FRAGMENT_SHADER_CODE);

        objectPositionParam = GLES20.glGetAttribLocation(objectProgram, "a_Position");
        objectUvParam = GLES20.glGetAttribLocation(objectProgram, "a_UV");
        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objectProgram, "u_MVP");

        Util.checkGLError("onSurfaceCreated");

        // Load Objects
        sevenSegmentTimer = new SevenSegmentTimer(this, objectPositionParam, objectUvParam, 0.0f,0.0f, -15.0f, 123);
        sevenSegmentTimer.start();
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
