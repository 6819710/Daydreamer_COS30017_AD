package com.jpgalovic.daydream.model.state;

import android.content.Context;
import android.opengl.Matrix;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.drawable.Compound;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

import java.util.ArrayList;

public class Navigation extends State {
    // Object Data
    private TexturedMeshObject objectCRT;
    private TexturedMeshObject objectTable;
    private TexturedMeshObject objectHighScores;
    private TexturedMeshObject objectFindTheBlock;
    private TexturedMeshObject objectBlock;

    private Compound piano;

    // State Data
    boolean flag_load_scores;
    boolean flag_load_find_the_block;

    public Navigation(Context context) {
        super("STATE_NAVIGATION", context);
        flag_load_scores = false;
        flag_load_find_the_block = false;
    }

    @Override
    public void onDisplay() {
        flag_load_scores = false;
        flag_load_find_the_block = false;
    }

    @Override
    public void init() {
        objectCRT = new TexturedMeshObject("OBJECT_CRT", false, Data.getMesh(context, R.array.OBJ_NAV_CRT), Data.getTextures(context, R.array.OBJ_NAV_CRT), 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectTable = new TexturedMeshObject("OBJECT_TABLE", false, Data.getMesh(context, R.array.OBJ_NAV_TABLE), Data.getTextures(context, R.array.OBJ_NAV_TABLE), 0.0f, -3.5f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectHighScores = new TexturedMeshObject("OBJECT_HIGH_SCORES_LABEL", false, Data.getMesh(context, R.array.OBJ_LABEL_HIGH_SCORES), Data.getTextures(context, R.array.OBJ_LABEL_HIGH_SCORES), 0.0f, 1.4f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectFindTheBlock = new TexturedMeshObject("OBJECT_FIND_THE_BLOCK_LABEL", false, Data.getMesh(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), Data.getTextures(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), 3.4641f,  0.0f, -2.0f, 0.0f, -60.0f, 0.0f);
        objectBlock = new TexturedMeshObject("OBJECT_BLOCK", false, Data.getMesh(context, R.array.OBJ_FTB_BLOCK), Data.getTextures(context, R.array.OBJ_FTB_BLOCK), 5.19615f, 0.0f, -3.0f, 0.0f, 0.0f, 0.0f);

        ArrayList<TexturedMeshObject> keyboard = new ArrayList<>();
        ArrayList<float[]> offsets = new ArrayList<>();

        float[] data = new float[16];
        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.81f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("A", true, Data.getMesh(context, R.array.OBJ_LEFT_KEY), Data.getTextures(context, R.array.OBJ_LEFT_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.675f, 0.0f, -0.25f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("A#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.54f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("B", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.405f, 0.0f, -0.25f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("B#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.27f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("C", true, Data.getMesh(context, R.array.OBJ_RIGHT_KEY), Data.getTextures(context, R.array.OBJ_RIGHT_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.0f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("D", true, Data.getMesh(context, R.array.OBJ_LEFT_KEY), Data.getTextures(context, R.array.OBJ_LEFT_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.135f, 0.0f, -0.25f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("D#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.27f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("E", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.405f, 0.0f, -0.25f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("E#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.54f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("F", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.675f, 0.0f, -0.25f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("F#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.81f, 0.0f, 0.0f);
        offsets.add(data.clone());
        keyboard.add(new TexturedMeshObject("G", true, Data.getMesh(context, R.array.OBJ_RIGHT_KEY), Data.getTextures(context, R.array.OBJ_RIGHT_KEY), data[12], data[13], data[14] - 2.0f, 0.0f, 0.0f, 0.0f));

        piano = new Compound(keyboard, offsets, 0.0f, 0.0f, -2.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

        piano.setRotation(45.0f, 45.0f, 0.0f);
        piano.translate(0.0f, -1.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        if(objectCRT.isLookedAt(headView)) {
            flag_load_scores = true;
        } else if (objectFindTheBlock.isLookedAt(headView)){
            flag_load_find_the_block = true;
        }
    }

    @Override
    public State update() {
        if(flag_load_scores) { // Load High Scores.
            connected.get(0).onDisplay();
            return connected.get(0);
        } else if (flag_load_find_the_block) { // Load Find The Block.
            connected.get(1).onDisplay();
            return connected.get(1);
        }

        objectCRT.rotate(0.0f, 0.5f, 0.0f);
        objectBlock.rotate(0.1f, 0.5f, -0.5f);
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        //objectCRT.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        //objectTable.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        //objectHighScores.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        //objectFindTheBlock.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        //objectBlock.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);

        piano.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }
}
