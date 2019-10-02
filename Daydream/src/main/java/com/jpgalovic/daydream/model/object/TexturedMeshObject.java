package com.jpgalovic.daydream.model.object;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class TexturedMeshObject {
    private static String TAG;

    private Mesh objectMesh;
    private ArrayList<Texture> objectTex;

    private float[] modelPos;
    private float[] modelRot;

    /**
     * Initialises Textured Mesh Object with given parameters
     * @param context           Application Context.
     * @param name              Name of object (used for logging and lookup purposes).
     * @param objPath           File Path to .obj file.
     * @param texPath           Array of Pathes to obj Textures, (by default, {primary, secondary, ...}).
     * @param positionAttrib    Shader Position Attribute.
     * @param uvAttib           Shader UV Attribute.
     * @param x                 X coordinate of object.
     * @param y                 Y coordinate of object.
     * @param z                 Z coordinate of object.
     * @param pitch             Pitch rotation of object.
     * @param yaw               Yaw rotation of object.
     * @param roll              Roll rotation of object.
     */
    public TexturedMeshObject(Context context, String name, String objPath, String[] texPath, int positionAttrib, int uvAttib, float x, float y, float z, float pitch, float yaw, float roll) {
        init(name, x, y, z, yaw, pitch, roll);

        try {
            objectMesh = new Mesh(context, objPath, positionAttrib, uvAttib);
            for(String path : texPath) {
                objectTex.add(new Texture(context, path));
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to initialise objecs", e);
        }
    }

    /**
     * Initialises variables.
     * @param name              Name of object (used for logging and lookup purposes).
     * @param x                 X coordinate of object.
     * @param y                 Y coordinate of object.
     * @param z                 Z coordinate of object.
     * @param pitch             Pitch rotation of object.
     * @param yaw               Yaw rotation of object.
     * @param roll              Roll rotation of object.
     */
    public void init(String name, float x, float y, float z, float pitch, float yaw, float roll) {
        TAG = name;

        modelPos = new float[16];
        modelRot = new float[16];

        Matrix.setRotateEulerM(modelRot, 0, 0, 0, 0);
        Matrix.setIdentityM(modelPos, 0);

        translate(x, y, z);
        rotate(yaw, pitch, roll);
    }

    /**
     * Sets position of object (Absolute Coordinates)
     * @param x                 X coordinate of object.
     * @param y                 Y coordinate of object.
     * @param z                 Z coordinate of object.
     */
    public void setPosition(float x, float y, float z) {
        Matrix.setIdentityM(modelPos, 0);
        translate(x, y, z);
    }

    /**
     * Translates position of object (Relative Coordinates)
     * @param x                 X coordinate of object.
     * @param y                 Y coordinate of object.
     * @param z                 Z coordinate of object.
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(modelPos, 0, x, y, z);
    }

    /**
     * Rotates object (Relative Angles)
     * @param pitch             Pitch rotation of object.
     * @param yaw               Yaw rotation of object.
     * @param roll              Roll rotation of object.
     */
    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(modelRot, 0, pitch, 1,  0, 0);
        Matrix.rotateM(modelRot, 0, yaw, 0,  1, 0);
        Matrix.rotateM(modelRot, 0, roll, 0,  0, 1);
    }

    /**
     * Rotates object about a given point
     * @param x                 X coordinate of object.
     * @param y                 Y coordinate of object.
     * @param z                 Z coordinate of object.
     * @param pitch             Pitch rotation of object.
     * @param yaw               Yaw rotation of object.
     * @param roll              Roll rotation of object.
     */
    public void rotateAbout(float x, float y, float z, float pitch, float yaw, float roll) {
        // Calculate vector from current object location.
        float i = x - modelPos[12];
        float j = y - modelPos[13];
        float k = z - modelPos[14];

        // Calculate Radius, Theta and Phi TODO: Determine relationship between yaw pitch and roll and theta and phi
        float rad = (float) Math.sqrt(i*i + j*j + k*k);
        float theta = (float) Math.acos(k / rad);
        float phi = (float) Math.atan2(j, i);

        float pitchRad = (float)(pitch * Math.PI) / 180.0f;
        float yawRad = (float)(yaw * Math.PI) / 180.0f;

        // Calculate Relitive Position.
        float relX = (float) (rad * Math.sin(theta + pitchRad) * Math.cos(phi + yawRad));
        float relY = (float) (rad * Math.sin(theta + pitchRad) * Math.sin(phi + yawRad));
        float relZ = (float) (rad * Math.cos(theta + pitchRad));

        // Translate and rotate object.
        translate(relX, relY, relZ);
        rotate(pitch, yaw, roll);

    }
}
