package com.jpgalovic.daydreamer;

import android.content.Intent;
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

import com.jpgalovic.daydreamer.model.game.object.LetterSelector;
import com.jpgalovic.daydreamer.model.util.FileManager;
import com.jpgalovic.daydreamer.model.TexturedMeshObject;
import com.jpgalovic.daydreamer.model.util.HighScoreManager;
import com.jpgalovic.daydreamer.model.util.Util;
import com.jpgalovic.daydreamer.model.util.Values;

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

    private int objectProgram;

    private int objectPositionParam;
    private int objectUvParam;
    private int objectModelViewProjectionParam;

    // Object Data
    private TexturedMeshObject objectCRT;
    private TexturedMeshObject objectTable;
    private TexturedMeshObject objectFindTheBlock;

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

        // Copy high score data if freshly installed.
        FileManager.copyAssets(this);
    }

    public void initializeGvrView() {
        setContentView(R.layout.common_ui);

        GvrView gvrView = findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        gvrView.setRenderer(this);
        //gvrView.setTransitionViewEnabled(true);
        gvrView.setTransitionViewEnabled(false);

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

        // Draw each object.
        objectCRT.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectTable.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectFindTheBlock.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        objectCRT.rotate(0.0f, 0.5f, 0.0f);
    }


    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        if(objectCRT.isLookedAt(headView)) {
            Log.i(TAG, "CRTMonitor");

            // Load High Scores Activity.
            Intent intent = new Intent(Navigation.this, HighScores.class);
            startActivity(intent);
        }
        
        if(objectFindTheBlock.isLookedAt(headView)) {
            Log.i(TAG, "FindTheBlock");

            // Load Find the Block Activity.
            Intent intent = new Intent(Navigation.this, FindTheBlock.class);
            startActivity(intent);
        }

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

        objectProgram = Util.compileProgram(Values.OBJECT_VERTEX_SHADER, Values.OBJECT_FRAGMENT_SHADER);

        objectPositionParam = GLES20.glGetAttribLocation(objectProgram, "a_Position");
        objectUvParam = GLES20.glGetAttribLocation(objectProgram, "a_UV");
        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objectProgram, "u_MVP");

        Util.checkGLError("onSurfaceCreated");

        // Load Objects
        objectCRT = new TexturedMeshObject(this, "CRT", "obj/crt_monitor.obj", "obj/crt_monitor_texture.png", objectPositionParam, objectUvParam,0.0f,0.0f, -4.5f);
        objectTable = new TexturedMeshObject(this, "Table", "obj/table.obj", "obj/table_texture.png", objectPositionParam, objectUvParam, 0.0f, -3.5f, -4.0f);
        objectFindTheBlock = new TexturedMeshObject(this, "FindTheBlockTitle", "obj/find_the_block_title.obj", "obj/find_the_block_title.png", "obj/find_the_block_title_selected.png", objectPositionParam, objectUvParam, 3.4641f,  0.0f, -2.0f);
        objectFindTheBlock.rotate(0.0f, -30.0f, 0.0f);

        /**
         *   X          Z       Angle
         *   3.4641     -2      -30
         *   -3.4641    -2      30
         *   2
         */
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
