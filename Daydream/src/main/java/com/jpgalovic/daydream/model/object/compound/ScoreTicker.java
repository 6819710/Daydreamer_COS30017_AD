package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import java.util.ArrayList;

public class ScoreTicker {
    private static final String TAG = "OBJ_SCORE_TRACKER";

    private ArrayList<NumericSelector> numberObjects;

    public ScoreTicker(Context context, float x, float y, float z, float pitch, float yaw, float roll) {
        numberObjects = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            numberObjects.add(new NumericSelector(context, x + 2.0f - (i * 1.0f), y, z, pitch, yaw, roll));
        }
    }

    public void setScore(int score){
        int value = score;
        ArrayList<Integer> values = new ArrayList<>();

        while(value > 0) {
            values.add(value % 10);
            value = value / 10;
        }

        for(int i = 0; i < values.size(); i++) {
            numberObjects.get(i).setIndex(values.get(i));
        }
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        for(NumericSelector numericSelector : numberObjects) {
            numericSelector.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        }
    }
}
