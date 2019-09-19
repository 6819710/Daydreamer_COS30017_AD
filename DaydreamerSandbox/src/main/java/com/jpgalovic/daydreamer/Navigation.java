package com.jpgalovic.daydreamer;

import android.opengl.GLES20;
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

    private int objectModelViewProjectionParam;

    // Object Data
    private TexturedMesh objectCRT;

    private float[] modelViewProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGvrView();

        modelViewProjection = new float[16];
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

        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objectProgram, "u_MVP");

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

    public void drawCRT() {
        GLES20.glUseProgram(objectProgram);
        GLES20.glUniformMatrix4fv(objectModelViewProjectionParam, 1, false, modelViewProjection, 0);

        // objectCRTTex.bind();
        objectCRT.draw();
        Util.checkGLError("drawCRT");
    }
}
