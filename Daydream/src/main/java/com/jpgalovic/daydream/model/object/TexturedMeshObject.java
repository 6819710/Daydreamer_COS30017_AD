package com.jpgalovic.daydream.model.object;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.jpgalovic.daydream.model.util.Util;
import com.jpgalovic.daydream.model.util.Values;

import java.io.IOException;
import java.util.ArrayList;

public class TexturedMeshObject {
    private static String TAG;

    private Mesh objectMesh;
    private ArrayList<Texture> objectTex;

    private float[] modelPos;
    private float[] modelRot;

    private float[] modelView;
    private float[] modelViewProjection;

    private Boolean flagFine;

    public TexturedMeshObject(Context context, String name, boolean flagFine, String objPath, Texture[] textures, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        init(name, flagFine, x, y, z, yaw, pitch, roll);

        try {
            objectMesh = new Mesh(context, objPath, positionAttribute, uvAttribute);
        } catch (IOException e) {
            Log.e(TAG, "Unable to initialise objects", e);
        }

        for(int i = 0; i < textures.length; i++) {
            objectTex.add(textures[i]);
        }
    }

    /**
     * Initialises Textured Mesh Object with given parameters
     * @param   context                           Application Context.
     * @param   name                              Name of object (used for logging and lookup purposes).
     * @param   flagFine                          Flag fine angle.
     * @param   objPath                           File Path to .obj file.
     * @param   texPath                           Array of Pathes to obj Textures, (by default, {primary, secondary, ...}).
     * @param   positionAttribute                 Shader Position Attribute.
     * @param   uvAttribute                       Shader UV Attribute.
     * @param   x                                 X coordinate of object.
     * @param   y                                 Y coordinate of object.
     * @param   z                                 Z coordinate of object.
     * @param   pitch                             Pitch rotation of object.
     * @param   yaw                               Yaw rotation of object.
     * @param   roll                              Roll rotation of object.
     */
    public TexturedMeshObject(Context context, String name, boolean flagFine, String objPath, String[] texPath, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        init(name, flagFine, x, y, z, yaw, pitch, roll);

        try {
            objectMesh = new Mesh(context, objPath, positionAttribute, uvAttribute);
            for(String path : texPath) {
                objectTex.add(new Texture(context, path));
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to initialise objects", e);
        }
    }

    /**
     * Initialises variables.
     * @param   name                              Name of object (used for logging and lookup purposes).
     * @param   flagFine                          Flag fine angle.
     * @param   x                                 X coordinate of object.
     * @param   y                                 Y coordinate of object.
     * @param   z                                 Z coordinate of object.
     * @param   pitch                             Pitch rotation of object.
     * @param   yaw                               Yaw rotation of object.
     * @param   roll                              Roll rotation of object.
     */
    private void init(String name, boolean flagFine, float x, float y, float z, float pitch, float yaw, float roll) {
        TAG = name;

        this.flagFine = flagFine;

        modelPos = new float[16];
        modelRot = new float[16];

        modelView = new float[16];
        modelViewProjection = new float[16];

        objectTex = new ArrayList<>();

        Matrix.setRotateEulerM(modelRot, 0, 0, 0, 0);
        Matrix.setIdentityM(modelPos, 0);

        translate(x, y, z);
        rotate(yaw, pitch, roll);
    }

    /**
     * Sets position of object (Absolute Coordinates)
     * @param   x                                 X coordinate of object.
     * @param   y                                 Y coordinate of object.
     * @param   z                                 Z coordinate of object.
     */
    public void setPosition(float x, float y, float z) {
        Matrix.setIdentityM(modelPos, 0);
        translate(x, y, z);
    }

    /**
     * Translates position of object (Relative Coordinates)
     * @param   x                                 X coordinate of object.
     * @param   y                                 Y coordinate of object.
     * @param   z                                 Z coordinate of object.
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(modelPos, 0, x, y, z);
    }

    /**
     * Sets rotation of object.
     * @param   pitch                             Pitch rotation of object.
     * @param   yaw                               Yaw rotation of object.
     * @param   roll                              Roll rotation of object.
     */
    public void setRotation(float pitch, float yaw, float roll) {
        Matrix.setRotateEulerM(modelRot, 0, pitch, yaw, roll);
    }

    /**
     * Rotates object (Relative Angles)
     * @param   pitch                             Pitch rotation of object.
     * @param   yaw                               Yaw rotation of object.
     * @param   roll                              Roll rotation of object.
     */
    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(modelRot, 0, pitch, 1,  0, 0);
        Matrix.rotateM(modelRot, 0, yaw, 0,  1, 0);
        Matrix.rotateM(modelRot, 0, roll, 0,  0, 1);
    }

    /**
     * Renders the object. taking into account the headview to determine if the object is currently been looked at.
     * @param   perspective                       Perspective matrix.
     * @param   view                              View matrix.
     * @param   headView                          Head view matrix.
     * @param   objectProgram                     Object program.
     * @param   objectModelViewProjectionParam    Object model view projection parameter.
     */
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        if(isLookedAt(headView)) {
            render(perspective, view, 1, objectProgram, objectModelViewProjectionParam);
        } else {
            render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        }
    }

    /**
     * Renders the object.
     * @param   perspective                       Perspective matrix.
     * @param   view                              View matrix.
     * @param   textureIndex                      Texture index.
     * @param   objectProgram                     Object program.
     * @param   objectModelViewProjectionParam    Object model view projection parameter.
     */
    public void render(float[] perspective, float[] view, int textureIndex, int objectProgram, int objectModelViewProjectionParam) {
        float[] matrix = new float[16];

        // Build modelView and modelViewProjection.
        // This calculates the position to draw the object.
        Matrix.multiplyMM(modelView, 0, view, 0, modelPos, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);
        Matrix.multiplyMM(matrix, 0, modelViewProjection, 0, modelRot, 0);

        // Draw the object.
        GLES20.glUseProgram(objectProgram);
        GLES20.glUniformMatrix4fv(objectModelViewProjectionParam, 1, false, matrix, 0);

        objectTex.get(textureIndex).bind();
        objectMesh.render();
        Util.checkGLError("Draw"+TAG);
    }

    /**
     * Determines if object is been looked at.
     * @param   headView                          Head view matrix.
     * @return                                    True if object is looked at.
     */
    public boolean isLookedAt(float[] headView) {
        float[] tempPosition = new float[16];
        Matrix.multiplyMM(modelView, 0, headView, 0, modelPos, 0);
        Matrix.multiplyMV(tempPosition, 0, modelView, 0, Values.POS_MATRIX_MULTIPLY_VEC, 0);

        float angle = Util.angleBetweenVectors(tempPosition, Values.FORWARD_VEC);
        if(flagFine) {
            return angle < Values.ANGLE_THRESHOLD_FINE;
        } else {
            return angle < Values.ANGLE_THRESHOLD;
        }
    }
}
