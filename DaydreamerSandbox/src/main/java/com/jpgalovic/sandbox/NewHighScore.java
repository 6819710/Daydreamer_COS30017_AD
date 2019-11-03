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
import com.jpgalovic.sandbox.model.game.object.LetterSelector;
import com.jpgalovic.sandbox.model.util.HighScoreManager;
import com.jpgalovic.sandbox.model.util.Util;
import com.jpgalovic.sandbox.model.util.Values;

import javax.microedition.khronos.egl.EGLConfig;

public class NewHighScore extends GvrActivity implements GvrView.StereoRenderer {
    private static final String TAG = "NewHighScoreActivity";

    private int objectProgram;

    private int objectPositionParam;
    private int objectUvParam;
    private int objectModelViewProjectionParam;

    // Object Data
    private LetterSelector objectLetterSelector1;
    private LetterSelector objectLetterSelector2;
    private LetterSelector objectLetterSelector3;
    private TexturedMeshObject objectSave;

    int score;

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

        // Load Data
        Bundle extra = getIntent().getExtras();
        score = extra.getInt("score");
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
        objectLetterSelector1.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectLetterSelector2.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectLetterSelector3.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectSave.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        objectLetterSelector1.isLookedAt(headView);
        objectLetterSelector2.isLookedAt(headView);
        objectLetterSelector3.isLookedAt(headView);

        if(objectSave.isLookedAt(headView)) {
            // Save High Score
            HighScoreManager highScore = new HighScoreManager(this, "find_the_block_scores.csv");
            String name = objectLetterSelector1.getLetter() + objectLetterSelector2.getLetter() + objectLetterSelector3.getLetter();
            highScore.setNewHighScore(name, score);

            // Load High Scores
            Intent intent = new Intent(NewHighScore.this, HighScores.class);
            startActivity(intent);
            finish();
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
        objectLetterSelector1 = new LetterSelector(this, objectPositionParam, objectUvParam, -1.3f, 0.0f, -4.0f);
        objectLetterSelector2 = new LetterSelector(this, objectPositionParam, objectUvParam, 0.0f, 0.0f, -4.0f);
        objectLetterSelector3 = new LetterSelector(this, objectPositionParam, objectUvParam, 1.3f, 0.0f, -4.0f);
        objectSave = new TexturedMeshObject(this, "Save", "obj/save.obj", "obj/save.png", "obj/save_selected.png", objectPositionParam, objectUvParam, 0.0f, -1.8f, -4.0f);
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
