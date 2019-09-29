package com.jpgalovic.daydreamer.model.game.text;

import android.content.Context;

import com.jpgalovic.daydreamer.model.TexturedMeshObject;

import java.util.ArrayList;

public class NumberObject {
    private static final String TAG = "Number";
    private static final char[] numberCharArray = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    private ArrayList<TexturedMeshObject> numberObjects;
    private boolean allLoaded;
    private int number;

    public NumberObject(Context context, int objectPositionParam, int objectUvParam, float x, float y, float z, int number, boolean loadAll) {
        numberObjects = new ArrayList<>();

        String[] numberObj = {
                "obj/num/0.obj", "obj/num/1.obj", "obj/num/2.obj", "obj/num/3.obj", "obj/num/4.obj",
                "obj/num/5.obj", "obj/num/6.obj", "obj/num/7.obj", "obj/num/8.obj", "obj/num/9.obj",
        };

        String[] numberTex = {
                "obj/num/0.png", "obj/num/1.png", "obj/num/2.png", "obj/num/3.png", "obj/num/4.png",
                "obj/num/5.png", "obj/num/6.png", "obj/num/7.png", "obj/num/8.png", "obj/num/9.png",
        };

        this.number = number;
        allLoaded = loadAll;

        // If loadAll is true, all numbers will be loaded, otherwise only the given number will be loaded (to save memory);
        if(loadAll) {
            for(int i = 0; i < 10; i++) {
                numberObjects.add(new TexturedMeshObject(context, Character.toString(numberCharArray[i]), numberObj[i], numberTex[i], objectPositionParam, objectUvParam, x, y, z));
            }
        } else {
            numberObjects.add(new TexturedMeshObject(context, Character.toString(numberCharArray[this.number]), numberObj[this.number], numberTex[this.number], objectPositionParam, objectUvParam, x, y, z));
        }
    }

    public char getNumberChar() {
        return numberCharArray[number];
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void draw(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        if(allLoaded) {
            numberObjects.get(number).draw(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        } else {
            numberObjects.get(0).draw(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        }
    }
}
