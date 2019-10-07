package com.jpgalovic.daydream.model.object.compound;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

public class AlphaNumeric {
    private static final String TAG = "AlphaNumeric";

    private static final char[] alphaNumeric = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    private TexturedMeshObject object;

    public AlphaNumeric(int index, float x, float y, float z, float pitch, float yaw, float roll) {
        object = new TexturedMeshObject(String.valueOf(alphaNumeric[index]), true, Data.alphaNumericMeshes.get(index), new Texture[]{ Data.alphaNumericTextures.get(index)}, x, y, z, pitch, yaw, roll);
    }

    public AlphaNumeric(char alphaNumeric, float x, float y, float z, float pitch, float yaw, float roll) {
        object = new TexturedMeshObject(String.valueOf(alphaNumeric), true, Data.alphaNumericMeshes.get(getIndex(alphaNumeric)), new Texture[]{ Data.alphaNumericTextures.get(getIndex(alphaNumeric))}, x, y, z, pitch, yaw, roll);
    }

    private int getIndex(char alphaNumeric) throws RuntimeException {
        for(int i = 0; i < 36; i++) {
            if(this.alphaNumeric[i] == alphaNumeric) {
                return i;
            }
        }
        throw new RuntimeException("Unknown Character:" + alphaNumeric + ",  please use [0-9][A-Z]");
    }

    public void setPosition(float x, float y, float z) {
        object.setPosition(x, y, z);
    }

    public void translate(float x, float y, float z) {
        object.translate(x, y, z);
    }

    public void setRotation(float pitch, float yaw, float roll) {
        object.setRotation(pitch, yaw, roll);
    }

    public void rotate(float pitch, float yaw, float roll) {
        object.rotate(pitch, yaw, roll);
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        object.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
    }

    public boolean isLookedAt(float[] headView) {
        return object.isLookedAt(headView);
    }
}
