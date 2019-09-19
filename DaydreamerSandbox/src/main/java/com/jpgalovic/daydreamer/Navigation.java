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

import java.io.IOException;

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

    private static final float TARGET_MESH_COUNT = 3;

    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 10.0f;

    private static final float FLOOR_HEIGHT = -2.0f;

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
    private EntityObject objectCRT;
    private EntityObject objectTable;

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

        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);

        // Build ModelView and ModelView Projection for each object.
        // This calculates the position to draw the object.
        objectCRT.draw(perspective, view, objectProgram, objectModelViewProjectionParam);
        objectTable.draw(perspective, view, objectProgram, objectModelViewProjectionParam);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        objectCRT.rotate(0.0f, 1.0f, 0.0f);
    }


    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        //TODO: Handle Object Detection and Activity Transitions.

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
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);

        objectProgram = Util.compileProgram(OBJECT_VERTEX_SHADER_CODE, OBJECT_FRAGMENT_SHADER_CODE);

        objectPositionParam = GLES20.glGetAttribLocation(objectProgram, "a_Position");
        objectUvParam = GLES20.glGetAttribLocation(objectProgram, "a_UV");
        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objectProgram, "u_MVP");

        Util.checkGLError("onSurfaceCreated");

        // Load Objects
        objectCRT = new EntityObject(this, "CRT", "obj/crt_monitor.obj", "obj/crt_monitor_texture.png", objectPositionParam, objectUvParam,0.0f,-1.28f, -4.5f);
        objectTable = new EntityObject(this, "Table", "obj/table.obj", "obj/table_texture.png", objectPositionParam, objectUvParam, 0.0f, -3.5f, -4.0f);
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
