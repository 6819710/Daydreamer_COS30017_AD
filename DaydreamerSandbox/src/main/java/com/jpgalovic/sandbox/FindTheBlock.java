package com.jpgalovic.sandbox;

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

import com.jpgalovic.sandbox.model.TexturedMeshObject;
import com.jpgalovic.sandbox.model.util.HighScoreManager;
import com.jpgalovic.sandbox.model.util.Util;
import com.jpgalovic.sandbox.model.util.Values;
import com.jpgalovic.sandbox.model.game.object.SevenSegmentTimer;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;

public class FindTheBlock extends GvrActivity implements GvrView.StereoRenderer {
    private static final String TAG = "FindTheBlockActivity";

    private int objectProgram;

    private int objectPositionParam;
    private int objectUvParam;
    private int objectModelViewProjectionParam;

    private Random rand;

    // Object Data
    private SevenSegmentTimer sevenSegmentTimer;
    private TexturedMeshObject block;

    // Game Data
    private int score;

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

        score = 0;
        rand = new Random();
    }

    public void initializeGvrView() {
        setContentView(R.layout.common_ui);

        GvrView gvrView = findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        gvrView.setRenderer(this);
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
        sevenSegmentTimer.draw(perspective, view, objectProgram, objectModelViewProjectionParam);
        block.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);

    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        // Rotate Block about XYZ
        block.rotate(1,1,1);

        // If timer has run out check if score is new high score then load either the
        // "New High Score" or "High Scores" activity respectively.
        if(sevenSegmentTimer.zero()) {
            Intent intent = new Intent(FindTheBlock.this, HighScores.class);
            HighScoreManager manager = new HighScoreManager(this, "find_the_block_scores.csv");
            if(manager.isHighScore(score)) {
                intent = new Intent(FindTheBlock.this, NewHighScore.class);
                intent.putExtra("score", score);
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        // Handle Block Found. Increment Score then re-position block to be found again.
        if(block.isLookedAt(headView)) {
            score = score + rand.nextInt((20 - 10) + 1) + 10;
            newTarget();
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
        sevenSegmentTimer = new SevenSegmentTimer(this, objectPositionParam, objectUvParam, 0.0f,0.0f, -15.0f, 30);
        sevenSegmentTimer.start();

        block = new TexturedMeshObject(this, "Block", "obj/cube.obj", "obj/cube.png", "obj/cube_selected.png", objectPositionParam, objectUvParam,0.0f,0.0f, -10f);
        newTarget();
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    private void newTarget() {
        // calculate random yaw, pitch and distance
        float theta = (float) Math.toRadians((rand.nextFloat() - 0.5f) * 2.0f * Values.MAX_YAW);
        float phi = (float) Math.toRadians((rand.nextFloat() - 0.5f) * 2.0f * Values.MAX_PITCH);
        float radius = rand.nextFloat() * (Values.MAX_TARGET_DISTANCE - Values.MIN_TARGET_DISTANCE) + Values.MIN_TARGET_DISTANCE;

        // Calculate target position based on theta, phi and radius
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
        float z = (float) (radius * Math.cos(theta));

        /*
         * Get x, y, z from yaw, pitch, and dist
         * x = radius * cos (yaw) * cos(pitch)
         * y = radius * sin (yaw) * cos(pitch)
         * z = radius * sin (yaw)
         *
         * Get yaw, pitch, dist from (x,y,z)
         * radius = sqrt(x*x + y*y + z*z);
         * yaw = acos(z / radius);
         * pitch = atan2 (y, x);
         */

        // Set position of block
        block.setPosition(x, y, z);
    }
}
