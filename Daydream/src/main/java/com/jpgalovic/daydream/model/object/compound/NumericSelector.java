package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import com.jpgalovic.daydream.model.object.drawable.AlphaNumeric;

import java.util.ArrayList;

public class NumericSelector {
    private static final String TAG = "OBJ_NUMERIC_SELECTOR";

    private int index;

    char[] numbers = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    private ArrayList<AlphaNumeric> numericObjects;

    public NumericSelector(Context context, float x, float y, float z, float pitch, float yaw, float roll) {
        numericObjects = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            numericObjects.add(new AlphaNumeric(context, numbers[i], x, y, z, pitch, yaw, roll));
        }

        this.index = index;
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        numericObjects.get(index).render(perspective, view, objectProgram, objectModelViewProjectionParam);
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
