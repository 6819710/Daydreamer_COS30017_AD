package com.jpgalovic.daydream.model.object.drawable;

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

    public Compound(ArrayList<TexturedMeshObject> objects, ArrayList<float[]> offsets, float x, float y, float z, float pitch, float yaw, float roll, float sX, float sY, float sZ) {
        modelMatrix = new ModelMatrix(x, y, z, pitch, yaw, roll, sX, sY, sZ);

        this.objects = objects;
        this.offsets = offsets;
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
        for(int i = 0; i < objects.size(); i++) {
            // Calculate new Offset Position based on Current Offsets to determine new vector.
            float offX = offsets.get(i)[12];
            float offY = offsets.get(i)[13];
            float offZ = offsets.get(i)[14];

            float magnitude = Util.calulateMagnitude(offX, offY, offZ);
            float setYaw = Util.calulateYaw(offX, offY);
            float setPitch = Util.calulatePitch(offX, offY, offZ);

            setYaw = setYaw + yaw;
            setPitch = setPitch + pitch;

            float[] offset = Util.calculatePosition(magnitude, setPitch, setYaw);

            offsets.set(i, offset);

            // Set new position and rotation of object.
            objects.get(i).setTranslation(offset[12] + modelMatrix.getX(), offset[13] + modelMatrix.getY(), offset[14] + modelMatrix.getZ());
            objects.get(i).rotate(pitch, yaw, roll);
        }
    }

    public void setRotation(float pitch, float yaw, float roll) {
        for(int i = 0; i < objects.size(); i++) {
            // Calculate new Offset Position based on Current Offsets to determine new vector.
            float offX = offsets.get(i)[12];
            float offY = offsets.get(i)[13];
            float offZ = offsets.get(i)[14];

            float magnitude = Util.calulateMagnitude(offX, offY, offZ);
            float setYaw = Util.calulateYaw(offX, offY);
            float setPitch = Util.calulatePitch(offX, offY, offZ);

            setYaw = setYaw + yaw;
            setPitch = setPitch + pitch;

            float[] offset = Util.calculatePosition(magnitude, setPitch, setYaw);

            offsets.set(i, offset);

            // Set new position and rotation of object.
            objects.get(i).setTranslation(offset[12] + modelMatrix.getX(), offset[13] + modelMatrix.getY(), offset[14] + modelMatrix.getZ());
            objects.get(i).setRotation(pitch, yaw, roll);
        }
    }
}
