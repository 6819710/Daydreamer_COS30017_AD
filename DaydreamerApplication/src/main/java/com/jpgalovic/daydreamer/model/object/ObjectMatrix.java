package com.jpgalovic.daydreamer.model.object;

import android.opengl.Matrix;

/**
 * Object Matrix Definitions
 * Contains Object Position, Rotation and Scale data.
 */
public class ObjectMatrix {
    private float[] translation;
    private float[] rotation;
    private float[] scale;

    private float[] matrix;

    /**
     * Default constructor.
     */
    public ObjectMatrix() {
        init(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Constructor variant using only a position vector.
     * @param   x               x component of position vector.
     * @param   y               y component of position vector.
     * @param   z               z component of position vector.
     */
    public ObjectMatrix(float x, float y, float z) {
        init(x, y, z, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Constructor variant using a position vector and rotation angles (in degrees)
     * @param   x               x component of position vector.
     * @param   y               y component of position vector.
     * @param   z               z component of position vector.
     * @param   pitch           pitch angle (about x).
     * @param   yaw             yaw angle (about y).
     * @param   roll            roll angle (about z).
     */
    public ObjectMatrix(float x, float y, float z, float pitch, float yaw, float roll) {
        init(x, y, z, pitch, yaw, roll, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Constructor variant using a position vector, rotation angles (in degrees) and a scale component.
     * @param   x               x component of position vector.
     * @param   y               y component of position vector.
     * @param   z               z component of position vector.
     * @param   pitch           pitch angle (about x).
     * @param   yaw             yaw angle (about y).
     * @param   roll            roll angle (about z).
     * @param   sX              scale in x direction.
     * @param   sY              scale in y direction.
     * @param   sZ              scale in z direction.
     */
    public ObjectMatrix(float x, float y, float z, float pitch, float yaw, float roll, float sX, float sY, float sZ) {
        init(x, y, z, pitch, yaw, roll, sX, sY, sZ);
    }

    /**
     * Initialises the position vector, rotation matrix and scale vector.
     * @param   x               x component of position vector.
     * @param   y               y component of position vector.
     * @param   z               z component of position vector.
     * @param   pitch           pitch angle (about x).
     * @param   yaw             yaw angle (about y).
     * @param   roll            roll angle (about z).
     * @param   sX              scale in x direction.
     * @param   sY              scale in y direction.
     * @param   sZ              scale in z direction.
     */
    private void init(float x, float y, float z, float pitch, float yaw, float roll, float sX, float sY, float sZ) {
        translation = new float[16];
        rotation = new float[16];
        scale = new float[16];

        matrix =  new float[16];

        Matrix.setIdentityM(translation, 0);
        Matrix.translateM(translation, 0, x, y, z);

        Matrix.setIdentityM(rotation, 0);
        Matrix.rotateM(rotation, 0, pitch, 1,  0, 0);
        Matrix.rotateM(rotation, 0, yaw, 0,  1, 0);
        Matrix.rotateM(rotation, 0, roll, 0,  0, 1);

        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, sX, sY, sZ);

        Matrix.setIdentityM(matrix, 0);
        buildObjectModel();
    }

    /**
     * Sets the position of the translation vector, in absolute units from origin.
     * @param   x               x component of position vector.
     * @param   y               y component of position vector.
     * @param   z               z component of position vector.
     */
    public void setPosition(float x, float y, float z) {
        Matrix.setIdentityM(translation, 0);
        translate(x, y, z);
    }

    /**
     * Translates the translation vector by the given position vector (relative).
     * @param   x               x component of translation vector.
     * @param   y               y component of translation vector.
     * @param   z               z component of translation vector.
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(translation, 0, x, y, z);
        buildObjectModel();
    }

    /**
     * Sets the rotation of the rotation matrix, in absolute units from the forward vector.
     * @param   pitch           pitch angle (about x).
     * @param   yaw             yaw angle (about y).
     * @param   roll            roll angle (about z).
     */
    public void setRotation(float pitch, float yaw, float roll) {
        Matrix.setIdentityM(rotation, 0);
        rotate(pitch, yaw, roll);
    }

    /**
     * Rotates the rotation matrix by the given angles (relative)
     * @param   pitch           pitch angle (about x).
     * @param   yaw             yaw angle (about y).
     * @param   roll            roll angle (about z).
     */
    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(rotation, 0, pitch, 1,  0, 0);
        Matrix.rotateM(rotation, 0, yaw, 0,  1, 0);
        Matrix.rotateM(rotation, 0, roll, 0,  0, 1);
        buildObjectModel();
    }

    /**
     * Sets the scale using the given ratios
     * @param   x               scale in x direction.
     * @param   y               scale in y direction.
     * @param   z               scale in z direction.
     */
    public void setScale(float x, float y, float z) {
        Matrix.setIdentityM(scale, 0);
        scale(x, y, z);
    }

    /**
     * Scales??? TODO: Refine description of this method.
     * @param   x               scale in x direction.
     * @param   y               scale in y direction.
     * @param   z               scale in z direction.
     */
    public void scale(float x, float y, float z) {
        Matrix.scaleM(scale, 0, x, y, z);
        buildObjectModel();
    }

    /**
     * Build object model (object matrix) from translation, rotaiton and scale.
     */
    private void buildObjectModel() {
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, translation, 0, rotation, 0);
        Matrix.multiplyMM(matrix, 0, temp, 0, scale, 0);
    }

    /**
     * Gets object matrix (hybrid of translation, rotation and scale)
     * @return                  float array containing object matrix.
     */
    public float[] getMatrix() {
        return matrix;
    }

    /**
     * gets position (translation) vector.
     * @return                  float array containing position vector.
     */
    public float[] getPosition() {
        return translation;
    }

    /**
     * gets rotation matrix.
     * @return                  float array containing rotation matrix.
     */
    public float[] getRotation() {
        return rotation;
    }

    /**
     * gets scale vector.
     * @return                  float array containing scale vector.
     */
    public float[] getScale() {
        return scale;
    }

    /**
     * Gets x position of translation vector.
     * @return                  float containing x position.
     */
    public float getX() {
        return  translation[12];
    }

    /**
     * Gets y position of translation vector.
     * @return                  float containing y position.
     */
    public float getY() {
        return  translation[13];
    }

    /**
     * Gets z position of translation vector.
     * @return                  float containing z position.
     */
    public float getZ() {
        return  translation[14];
    }
}
