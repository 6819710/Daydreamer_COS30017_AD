package com.jpgalovic.daydreamer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.jpgalovic.daydreamer.Texture;
import com.jpgalovic.daydreamer.TexturedMesh;
import com.jpgalovic.daydreamer.Util;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Core Entity Object Model
 *
 * <p>Encapsulates all model data, including texture and mesh, position and rotation control as
 * well as rendering tools.</p>
 */
public class EntityObject {
    private  String TAG;

    protected TexturedMesh objectMesh;
    protected Texture objectTex;
    protected float[] objectModel;

    protected float[] modelView;
    protected float[] modelViewProjection;
    protected float[] modelRotation;

    /**
     * Initializes Entity Object.
     *
     * @param context Context for loading the .obj and texture files.
     * @param name Name of entity object (used for log).
     * @param objFilePath Path to .obj file.
     * @param texturePath Path to texture file.
     * @param positionAttrib The position attribute in the shader.
     * @param uvAttrib The UV attribute in the shader.
     * @param x inital X position.
     * @param y inital Y position.
     * @param z inital Z position.
     */
    EntityObject (Context context, String name, String objFilePath, String texturePath, int positionAttrib, int uvAttrib, float x, float y, float z) {
        TAG = name;
        objectModel = new float[16];

        modelView = new float[16];
        modelViewProjection = new float[16];
        modelRotation = new float[16];

        Matrix.setRotateEulerM(modelRotation, 0, 0, 0, 0);

        //Initialize model data.
        Matrix.setIdentityM(objectModel, 0);
        Matrix.translateM(objectModel, 0, x, y, z);

        try {
            objectMesh = new TexturedMesh(context, objFilePath, positionAttrib, uvAttrib);
            objectTex = new Texture(context, texturePath);
        } catch (IOException e) {
            Log.e (TAG, "Unable to initialize objects", e);
        }
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(objectModel, 0, x, y, z);
    }

    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(modelRotation, 0, pitch, 1, 0, 0);
        Matrix.rotateM(modelRotation, 0, yaw, 0, 1, 0);
        Matrix.rotateM(modelRotation, 0, roll, 0, 0, 1);
    }

    public void draw(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        float[] matrix = new float[16];

        // Build modelView and modelViewProjection.
        // This calculates the position to draw the object.
        Matrix.multiplyMM(modelView, 0, view, 0, objectModel, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        Matrix.multiplyMM(matrix, 0, modelViewProjection, 0, modelRotation, 0);

        // Draw the object.
        GLES20.glUseProgram(objectProgram);
        GLES20.glUniformMatrix4fv(objectModelViewProjectionParam, 1, false, matrix, 0);
        objectTex.bind();
        objectMesh.draw();
        Util.checkGLError("drawCRT");
    }
}
