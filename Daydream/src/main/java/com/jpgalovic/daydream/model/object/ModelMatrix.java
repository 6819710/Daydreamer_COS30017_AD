package com.jpgalovic.daydream.model.object;

import android.opengl.Matrix;

public class ModelMatrix {
    private float[] translation;
    private float[] rotation;
    private float[] scale;

    private float[] objectMatrix;

    public ModelMatrix(float x, float y, float z, float pitch, float yaw, float roll, float sX, float sY, float sZ) {
        translation = new float[16];
        rotation = new float[16];
        scale = new float[16];

        objectMatrix =  new float[16];

        Matrix.setIdentityM(translation, 0);
        translate(x, y, z);

        Matrix.setIdentityM(rotation, 0);
        rotate(pitch, yaw, roll);

        Matrix.setIdentityM(scale, 0);
        scale(sX, sY, sZ);

        Matrix.setIdentityM(objectMatrix, 0);
        buildObjectModel();
    }

    public void setTranslation(float x, float y, float z) {
        Matrix.setIdentityM(translation, 0);
        translate(x, y, z);
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(translation, 0, x, y, z);
        buildObjectModel();
    }

    public void setRotation(float pitch, float yaw, float roll) {
        Matrix.setIdentityM(rotation, 0);
        rotate(pitch, yaw, roll);
    }

    public void rotate(float pitch, float yaw, float roll) {
        Matrix.rotateM(rotation, 0, pitch, 1,  0, 0);
        Matrix.rotateM(rotation, 0, yaw, 0,  1, 0);
        Matrix.rotateM(rotation, 0, roll, 0,  0, 1);
        buildObjectModel();
    }

    public void setScale(float x, float y, float z) {
        Matrix.setIdentityM(scale, 0);
        scale(x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(scale, 0, x, y, z);
        buildObjectModel();
    }

    private void buildObjectModel() {
        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, translation, 0, rotation, 0);
        Matrix.multiplyMM(objectMatrix, 0, temp, 0, scale, 0);
    }

    public float[] matrix() {
        return objectMatrix;
    }

    public float[] getPosition() {
        return translation;
    }

    public float[] getRotation(){
        return rotation;
    }

    public float getX() {
        return  translation[12];
    }

    public float getY() {
        return  translation[13];
    }

    public float getZ() {
        return  translation[14];
    }
}
