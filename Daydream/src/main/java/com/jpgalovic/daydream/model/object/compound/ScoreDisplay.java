package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;

import com.jpgalovic.daydream.model.score.Score;
import com.jpgalovic.daydream.model.util.Values;

import java.util.ArrayList;

public class ScoreDisplay {
    private ArrayList<AlphaNumeric> alphaNumerals;
    private Score score;

    public ScoreDisplay(Context context, Score score, int positionAttribute, int uvAttribute, float x, float y, float z, float pitch, float yaw, float roll) {
        alphaNumerals = new ArrayList<>();

        this.score = score;

        char[] numbers = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        };

        // Add letters of name.
        for(int i = 0; i < score.getName().length(); i++) {
            float xPos = x - Values.SCORE_DISPLAY_CENTER_OFFSET - ((score.getName().length() - i - 1) * Values.ALPHANUMERIC_OFFSET_H);
            alphaNumerals.add(new AlphaNumeric(score.getName().charAt(i), xPos, y, z, pitch, yaw, roll));
        }

        // Add numbers of score.
        int value = score.getScore();
        ArrayList<Integer> values = new ArrayList<>();

        while(value > 0) {
            values.add(value % 10);
            value = value / 10;
        }

        for(int i = 0; i < values.size(); i++) {
            float xPos = x + Values.SCORE_DISPLAY_CENTER_OFFSET + ((score.getName().length() - i - 1) * Values.ALPHANUMERIC_OFFSET_H);
            alphaNumerals.add(new AlphaNumeric(numbers[values.get(i)], xPos, y, z, pitch, yaw, roll));
        }
    }

    public void render(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        for(AlphaNumeric alphaNumeric : alphaNumerals) {
            alphaNumeric.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        }
    }
}
