package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.object.TexturedMeshObject;

public class AlphaNumeric {
    private static final String TAG = "AlphaNumeric";

    private static final char[] alphaNumeric = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    private TexturedMeshObject object;

    public AlphaNumeric(Context context, int index, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        object = new TexturedMeshObject(context, String.valueOf(alphaNumeric[index]), true, context.getResources().getStringArray(R.array.obj_char_obj)[index], new String[]{context.getResources().getStringArray(R.array.obj_char_tex)[index]}, positionAttribute, uvAttribute, x, y, z, pitch, yaw, roll);
    }

    public AlphaNumeric(Context context, char alphaNumeric, boolean flagFine, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        object = new TexturedMeshObject(context, String.valueOf(alphaNumeric), flagFine, context.getResources().getStringArray(R.array.obj_char_obj)[getIndex(alphaNumeric)], new String[]{context.getResources().getStringArray(R.array.obj_char_tex)[getIndex(alphaNumeric)]}, positionAttribute, uvAttribute, x, y, z, pitch, yaw, roll);
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
