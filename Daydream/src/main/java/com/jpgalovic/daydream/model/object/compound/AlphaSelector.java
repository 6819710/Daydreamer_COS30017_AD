package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.AlphaNumeric;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.util.Values;

import java.util.ArrayList;

public class AlphaSelector {
    private static final String TAG = "OBJ_ALPHA_SELECTOR";
    private static final char[] alpha = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    private int index;

    private ArrayList<AlphaNumeric> alphaObjects;
    private TexturedMeshObject nextObject;
    private TexturedMeshObject prevObejct;

    public AlphaSelector(Context context, float x, float y, float z, float pitch, float yaw, float roll) {
        alphaObjects = new ArrayList<>();
        for(int i = 0; i < 26; i++) {
            alphaObjects.add(new AlphaNumeric(context, alpha[i], x, y, z, pitch, yaw, roll));
        }
        nextObject = new TexturedMeshObject("OBJ_NEXT", true, Data.getMesh(context, R.array.OBJ_LABEL_ARROW), Data.getTextures(context, R.array.OBJ_LABEL_ARROW), x, y + 1.0f, z, pitch, yaw, roll);
        prevObejct = new TexturedMeshObject("OBJ_NEXT", true, Data.getMesh(context, R.array.OBJ_LABEL_ARROW), Data.getTextures(context, R.array.OBJ_LABEL_ARROW), x, y - 1.0f, z, pitch + 180.0f, yaw, roll);

        index = 0;
    }

    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        alphaObjects.get(index).render(perspective, view, objectProgram, objectModelViewProjectionParam);
        nextObject.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        prevObejct.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    public void next(float[] headView) {
        if(nextObject.isLookedAt(headView)) {
            ++index;
            if(index == 26) {
                index = 0;
            }
        }
    }

    public void prev(float[] headView) {
        if(prevObejct.isLookedAt(headView)) {
            --index;
            if(index < 0) {
                index = 25;
            }
        }
    }

    public char getChar() {
        return alpha[index];
    }
}
