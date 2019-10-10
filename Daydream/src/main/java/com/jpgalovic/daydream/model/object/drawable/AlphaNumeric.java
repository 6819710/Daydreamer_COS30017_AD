package com.jpgalovic.daydream.model.object.drawable;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.object.compound.AlphaSelector;

import java.util.ArrayList;

public class AlphaNumeric {
    private static final String TAG = "OBJ_ALPHA_NUMERIC";

    private static final char[] ALPHA_NUMERIC_CHAR = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    private static final int[] ID = {
            R.array.OBJ_ALPHANUMERIC_0, R.array.OBJ_ALPHANUMERIC_1, R.array.OBJ_ALPHANUMERIC_2,
            R.array.OBJ_ALPHANUMERIC_3, R.array.OBJ_ALPHANUMERIC_4, R.array.OBJ_ALPHANUMERIC_5,
            R.array.OBJ_ALPHANUMERIC_6, R.array.OBJ_ALPHANUMERIC_7, R.array.OBJ_ALPHANUMERIC_8,
            R.array.OBJ_ALPHANUMERIC_9,

            R.array.OBJ_ALPHANUMERIC_A, R.array.OBJ_ALPHANUMERIC_B, R.array.OBJ_ALPHANUMERIC_C,
            R.array.OBJ_ALPHANUMERIC_D, R.array.OBJ_ALPHANUMERIC_E, R.array.OBJ_ALPHANUMERIC_F,
            R.array.OBJ_ALPHANUMERIC_G, R.array.OBJ_ALPHANUMERIC_H, R.array.OBJ_ALPHANUMERIC_I,
            R.array.OBJ_ALPHANUMERIC_J, R.array.OBJ_ALPHANUMERIC_K, R.array.OBJ_ALPHANUMERIC_L,
            R.array.OBJ_ALPHANUMERIC_M, R.array.OBJ_ALPHANUMERIC_N, R.array.OBJ_ALPHANUMERIC_0,
            R.array.OBJ_ALPHANUMERIC_P, R.array.OBJ_ALPHANUMERIC_Q, R.array.OBJ_ALPHANUMERIC_R,
            R.array.OBJ_ALPHANUMERIC_S, R.array.OBJ_ALPHANUMERIC_T, R.array.OBJ_ALPHANUMERIC_U,
            R.array.OBJ_ALPHANUMERIC_V, R.array.OBJ_ALPHANUMERIC_W, R.array.OBJ_ALPHANUMERIC_X,
            R.array.OBJ_ALPHANUMERIC_Y, R.array.OBJ_ALPHANUMERIC_Z,
    };

    private TexturedMeshObject object;

    public AlphaNumeric(Context context, char alphaNumericChar, float x, float y, float z, float pitch, float yaw, float roll) {
        object = new TexturedMeshObject("OBJ_"+String.valueOf(alphaNumericChar), true, Data.getMesh(context, ID[getIndex(alphaNumericChar)]), Data.getTextures(context, ID[getIndex(alphaNumericChar)]), x, y, z, pitch, yaw, roll);
    }

    private int getIndex(char alphaNumeric) throws RuntimeException {
        for(int i = 0; i < 36; i++) {
            if(this.ALPHA_NUMERIC_CHAR[i] == alphaNumeric) {
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
