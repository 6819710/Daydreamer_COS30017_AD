package com.jpgalovic.daydream.model.object.compound;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

import java.util.ArrayList;

public class AlphaNumeric {
    private static final String TAG = "AlphaNumeric";

    private static final char[] alphaNumeric = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    private ArrayList<TexturedMeshObject> object;

    private int index;

    private boolean flag_load_alpha;
    private boolean flag_load_numeric;

    public AlphaNumeric(boolean flag_load_alpha, boolean flag_load_numeric, char alphaNumeric, float x, float y, float z, float pitch, float yaw, float roll) {
        this.flag_load_alpha = flag_load_alpha;
        this.flag_load_numeric = flag_load_numeric;

        object = new ArrayList<>();

        if(flag_load_numeric) {
            for(int i = 0; i < 10; i++) {
                object.add(new TexturedMeshObject(String.valueOf(this.alphaNumeric[i]), true, Data.selectorMeshes.get(i), new Texture[]{ Data.selectorTextures.get(i)}, x, y, z, pitch, yaw, roll));
            }
        }

        if(flag_load_alpha){
            for(int i = 10; i < 36; i++) {
                object.add(new TexturedMeshObject(String.valueOf(this.alphaNumeric[i]), true, Data.selectorMeshes.get(i), new Texture[]{ Data.selectorTextures.get(i)}, x, y, z, pitch, yaw, roll));
            }
        }

        if(!(flag_load_alpha || flag_load_numeric)) {
            object.add(new TexturedMeshObject(String.valueOf(alphaNumeric), true, Data.selectorMeshes.get(getIndex(alphaNumeric)), new Texture[]{ Data.selectorTextures.get(getIndex(alphaNumeric))}, x, y, z, pitch, yaw, roll));
        }

        index = getIndex(alphaNumeric);
    }

    public void next() {
        index++;
        if(index >= 10 && !flag_load_alpha) {
            if(flag_load_numeric) {
                index = 0;
            }
        } else if(index >=36) {
            if(flag_load_numeric) {
                index = 0;
            } else if (flag_load_alpha) {
                index = 10;
            }
        }

    }

    public void prev() {
        index--;
        if(index < 10 && !flag_load_numeric) {
            if(flag_load_alpha) {
                index = 35;
            }
        } else if(index < 0) {
            if(flag_load_alpha) {
                index = 35;
            } else if (flag_load_numeric) {
                index = 9;
            }
        }
    }

    public void setChar(char alphaNumeric) {
        index = getIndex(alphaNumeric);
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
        for(int i = 0; i < object.size(); i++) {
            object.get(i).setPosition(x, y, z);
        }
    }

    public void translate(float x, float y, float z) {
        for(int i = 0; i < object.size(); i++) {
            object.get(i).translate(x, y, z);
        }
    }

    public void setRotation(float pitch, float yaw, float roll) {
        for(int i = 0; i < object.size(); i++) {
            object.get(i).setRotation(pitch, yaw, roll);
        }
    }

    public void rotate(float pitch, float yaw, float roll) {
        for(int i = 0; i < object.size(); i++) {
            object.get(i).rotate(pitch, yaw, roll);
        }
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        if(flag_load_numeric) {
            object.get(index).render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        } else if (flag_load_alpha) {
            object.get(index - 10).render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        } else {
            object.get(0).render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        }

    }

    public boolean isLookedAt(float[] headView) {
        return object.get(0).isLookedAt(headView);
    }
}
