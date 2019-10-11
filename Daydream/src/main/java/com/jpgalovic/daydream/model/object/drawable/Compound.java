package com.jpgalovic.daydream.model.object.drawable;

import com.jpgalovic.daydream.model.object.ModelMatrix;

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

    


}
