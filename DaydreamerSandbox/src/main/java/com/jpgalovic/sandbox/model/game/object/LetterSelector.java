package com.jpgalovic.sandbox.model.game.object;

import android.content.Context;

import com.jpgalovic.sandbox.model.TexturedMeshObject;
import com.jpgalovic.sandbox.model.game.text.Letter;

public class LetterSelector {
    private Letter letter;
    private TexturedMeshObject up;
    private TexturedMeshObject down;

    public LetterSelector(Context context, int objectPositionParam, int objectUvParam, float x, float y, float z) {
        float offset = 1.0f;

        letter = new Letter(context, objectPositionParam, objectUvParam, x, y, z, 'A', true);
        up = new TexturedMeshObject(context, "up", "obj/arrow.obj", "obj/arrow.png", "obj/arrow_selected.png", objectPositionParam, objectUvParam, x, y + offset, z);
        down = new TexturedMeshObject(context, "down", "obj/arrow.obj", "obj/arrow.png", "obj/arrow_selected.png", objectPositionParam, objectUvParam, x, y - offset, z);

        down.rotate(0.0f, 0.0f, 180.0f);
    }

    /**
     * Gets current letter.
     * @return string containing current letter.
     */
    public String getLetter() {
        return Character.toString(letter.getLetter());
    }

    public void draw(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        letter.draw(perspective, view, objectProgram, objectModelViewProjectionParam);
        up.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        down.draw(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    /**
     * Handles is looked at calls.
     * @param headView current head view.
     */
    public void isLookedAt(float[] headView) {
        if(up.isLookedAtFine(headView)) {
            letter.next();
        } else if (down.isLookedAtFine(headView)) {
            letter.prev();
        }
    }

}
