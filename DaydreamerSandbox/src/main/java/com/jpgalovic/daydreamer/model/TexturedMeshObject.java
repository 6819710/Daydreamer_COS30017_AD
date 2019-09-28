package com.jpgalovic.daydreamer.model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.jpgalovic.daydreamer.model.util.Util;
import com.jpgalovic.daydreamer.model.util.Values;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Core Entity Object Model
 *
 * <p>Encapsulates all model data, including texture and mesh, position and rotation control as
 * well as rendering tools.</p>
 */
public class TexturedMeshObject {
    private  String TAG;

    private TexturedMesh objectMesh;
    private ArrayList<Texture> objectTex;
    private float[] objectModel;

    private float[] modelView;
    private float[] modelViewProjection;
    private float[] modelRotation;

    private float[] tempPosition;

    // Convenience Vector for extracting the position from a matrix via manipulation
    private static final float[] POS_MATRIX_MULTIPLY_VEC = {0.0f, 0.0f, 0.0f, 1.0f};
    private static final float[] FORWARD_VEC = {0.0f, 0.0f, -1.0f, 1.f};

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
    public TexturedMeshObject(Context context, String name, String objFilePath, String texturePath, int positionAttrib, int uvAttrib, float x, float y, float z) {
        InitObject(name, x, y, z);

        try {
            objectMesh = new TexturedMesh(context, objFilePath, positionAttrib, uvAttrib);
            objectTex.add(new Texture(context, texturePath));
            objectTex.add(new Texture(context, texturePath));
        } catch (IOException e) {
            Log.e (TAG, "Unable to initialize objects", e);
        }
    }

    public TexturedMeshObject(Context context, String name, String objFilePath, String texturePath, String altTexturePath, int positionAttrib, int uvAttrib, float x, float y, float z) {
        InitObject(name, x, y, z);

        try {
            objectMesh = new TexturedMesh(context, objFilePath, positionAttrib, uvAttrib);
            objectTex.add(new Texture(context, texturePath));
            objectTex.add(new Texture(context, altTexturePath));
        } catch (IOException e) {
            Log.e (TAG, "Unable to initialize objects", e);
        }
    }

    public TexturedMeshObject(Context context, String name, String objFilePath, String[] texturePath, int positionAttrib, int uvAttrib, float x, float y, float z) {
        InitObject(name, x, y, z);

        try {
            objectMesh = new TexturedMesh(context, objFilePath, positionAttrib, uvAttrib);
            for(String path : texturePath) {
                objectTex.add(new Texture(context, path));
            }
        } catch (IOException e) {
            Log.e (TAG, "Unable to initialize objects", e);
        }
    }

    public void InitObject(String name, float x, float y, float z) {
        TAG = name;
        objectModel = new float[16];

        modelView = new float[16];
        modelViewProjection = new float[16];
        modelRotation = new float[16];
        tempPosition = new float[16];

        Matrix.setRotateEulerM(modelRotation, 0, 0, 0, 0);

        //Initialize model data.
        Matrix.setIdentityM(objectModel, 0);
        Matrix.translateM(objectModel, 0, x, y, z);

        objectTex = new ArrayList<>();
    }

    public void setPosition(float x, float y, float z) {
        Matrix.setIdentityM(objectModel, 0);
        translate(x, y, z);
    }

    /**
     * Translates object by given x, y and z value
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(objectModel, 0, x, y, z);
    }

    /**
     * Rotates object about x, y and z axis by given yaw, pitch and roll.
     * @param pitch angle about x axis
     * @param yaw angle about y axis
     * @param roll angle about z axis
     */
    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(modelRotation, 0, pitch, 1, 0, 0);
        Matrix.rotateM(modelRotation, 0, yaw, 0, 1, 0);
        Matrix.rotateM(modelRotation, 0, roll, 0, 0, 1);
    }

    /**
     * Rotates object about a given vector (defined in i, j and k componants, value 0-1)
     * @param angle angel to rotate
     * @param i x component
     * @param j y component
     * @param k z component
     */
    public void roateAxis(float angle, float i, float j, float k) {
        Matrix.rotateM(modelRotation, 0, angle, i, j, k);
    }

    /**
     * Draws object using OpenGL. If object is been looked at, alternate texture will be displayed.
     * @param perspective perspective array (based on eye)
     * @param view view array.
     * @param objectProgram OpenGl program reference.
     * @param objectModelViewProjectionParam
     */
    public void draw(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        if(isLookedAt(headView)) {
            draw(perspective, view, 1, objectProgram, objectModelViewProjectionParam);
        } else {
            draw(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        }
    }

    public void draw(float[] perspective, float[] view, int textureIndex, int objectProgram, int objectModelViewProjectionParam) {
        float[] matrix = new float[16];

        // Build modelView and modelViewProjection.
        // This calculates the position to draw the object.
        Matrix.multiplyMM(modelView, 0, view, 0, objectModel, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        Matrix.multiplyMM(matrix, 0, modelViewProjection, 0, modelRotation, 0);

        // Draw the object.
        GLES20.glUseProgram(objectProgram);
        GLES20.glUniformMatrix4fv(objectModelViewProjectionParam, 1, false, matrix, 0);
        objectTex.get(textureIndex).bind();
        objectMesh.draw();
        Util.checkGLError("drawCRT");
    }

    /**
     * Checks if object is within field of view.
     * @param headView current head view.
     * @return true if object is within look threshold.
     */
    public boolean isLookedAt(float[] headView) {
        Matrix.multiplyMM(modelView, 0, headView, 0, objectModel, 0);
        Matrix.multiplyMV(tempPosition, 0, modelView, 0, POS_MATRIX_MULTIPLY_VEC, 0);

        float angle = Util.angleBetweenVectors(tempPosition, FORWARD_VEC);
        return angle < Values.ANGLE_THRESHOLD;
    }
}
