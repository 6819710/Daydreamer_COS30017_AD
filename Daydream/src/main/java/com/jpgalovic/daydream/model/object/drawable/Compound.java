package com.jpgalovic.daydream.model.object.drawable;

import android.opengl.Matrix;

import com.jpgalovic.daydream.model.object.ModelMatrix;
import com.jpgalovic.daydream.model.util.Util;

import java.util.ArrayList;

/**
 * Container for Compound Objects (indervidual objects that need position/rotation to be uniform/grouped)
 * Gets overidden by compound-type objects for implementation overiding.
 */
public class Compound {
    protected ModelMatrix modelMatrix;
    protected ArrayList<float[]> offsets;

    protected ArrayList<TexturedMeshObject> objects;

    public Compound(float x, float y, float z, float pitch, float yaw, float roll, float sX, float sY, float sZ) {
        modelMatrix = new ModelMatrix(x, y, z, pitch, yaw, roll, sX, sY, sZ);
    }

    public void setTranslation(float x, float y, float z) {
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).setTranslation(x + offsets.get(i)[12], y + offsets.get(i)[13], z + offsets.get(i)[14]);
        }
        modelMatrix.setRotation(x, y, z);
    }

    public void translate(float x, float y, float z) {
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).translate(x, y, z);
        }
        modelMatrix.translate(x, y, z);
    }

    public void rotate(float pitch, float yaw, float roll) {

    }

    public void setRotation(float pitch, float yaw, float roll) {
        float[] modelPos = modelMatrix.getPosition();

        // pitching rules:
        float cosPitch = (float) Math.cos(Math.toRadians(pitch));
        float sinPitch = (float) Math.sin(Math.toRadians(pitch));

        // yawing rules:
        float cosYaw = (float) Math.cos(Math.toRadians(yaw));
        float sinYaw = (float) Math.sin(Math.toRadians(yaw));

        // rolling rules:
        float cosRoll = (float) Math.cos(Math.toRadians(roll));
        float sinRoll = (float) Math.sin(Math.toRadians(roll));

        for(int i = 0; i < objects.size(); i++) {
            float[] pos = offsets.get(i);

            float x0 = pos[12];
            float y0 = pos[13];
            float z0 = pos[14];

            // pitch (about x)
            float x1 = x0;
            float y1 = y0 * cosPitch - z0 * sinPitch;
            float z1 = y0 * sinPitch + z0 * cosPitch;

            // yaw (about y)
            float x2 = x1 * cosYaw + z1 * sinYaw;
            float y2 = y1;
            float z2 = -x1 * sinYaw + z1 * cosYaw;

            // roll (about z)
            float x3 = x2 * cosRoll - y2 * sinRoll;
            float y3 = x2 * sinRoll + y2 * cosRoll;
            float z3 = z2;

            objects.get(i).setTranslation(x3 + modelPos[12], y3 + modelPos[13], z3 + modelPos[14]);
            objects.get(i).setRotation(pitch, yaw, roll);
        }
    }

    public void setAudio(int index, String audioFile) {
        objects.get(index).setAudio(audioFile);
    }

    public void playAudio(int index, boolean loop) {
        objects.get(index).playAudio(loop);
    }

    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int modelViewProjParam) {
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).render(perspective, view, headView, objectProgram, modelViewProjParam);
        }
    }

    public void render(float[] perspective, float[] view, int textureIndex, int objectProgram, int modelViewProjParam) {
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).render(perspective, view, textureIndex, objectProgram, modelViewProjParam);
        }
    }

    public boolean isLookedAt(int index, float[] headView) {
        return objects.get(index).isLookedAt(headView);
    }
}
