package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import com.jpgalovic.daydream.model.object.score.Score;
import com.jpgalovic.daydream.model.util.Values;

import java.util.ArrayList;

public class ScoreDisplay {
    private ArrayList<AlphaNumeric> alphaNumerics;

    public ScoreDisplay(Context context, Score score, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        alphaNumerics = new ArrayList<>();

        char[] numbers = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        };

        // Add letters of name.
        for(int i = 0; i < score.getName().length(); i++) {
            alphaNumerics.add(new AlphaNumeric(context, score.getName().charAt(i), false, positionAttribute, uvAttribute, x - Values.SCORE_DISPLAY_CENTER_OFFSET - ((score.getName().length() - i - 1) * Values.ALPHANUMERIC_OFFSET_H), y, z, pitch, yaw, roll));
        }

        // Add numbers of score.
        int value = score.getScore();
        int index = 0;
        while (value > 0) {
            alphaNumerics.add(new AlphaNumeric(context, numbers[value % 10], false, positionAttribute, uvAttribute, x + Values.SCORE_DISPLAY_CENTER_OFFSET + (index * Values.ALPHANUMERIC_OFFSET_H), y, z, pitch, yaw, roll));
            value = value / 10;
            index++;
        }
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        for(AlphaNumeric alphaNumeric : alphaNumerics) {
            alphaNumeric.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        }
    }
}
