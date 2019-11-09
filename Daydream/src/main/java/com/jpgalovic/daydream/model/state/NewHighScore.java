package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.compound.AlphaSelector;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.score.ScoreManager;
import com.jpgalovic.daydream.model.util.Values;

public class NewHighScore extends State {
    // Object Data
    private AlphaSelector objectSelectorA;
    private AlphaSelector objectSelectorB;
    private AlphaSelector objectSelectorC;

    private TexturedMeshObject objectSave;

    // State Data
    private ScoreManager scoreManager;

    private String pathString;
    private int score;

    private boolean FLAG_SAVE;

    @Override
    public void onDisplay() {
        FLAG_SAVE = false;
    }

    public NewHighScore(Context context) {
        super("STATE_NEW_HIGH_SCORE", context);
        pathString = new String();
        score = 0;
    }

    @Override
    public void init() {
        scoreManager = new ScoreManager(context, pathString);

        objectSelectorA = new AlphaSelector(context, -Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        objectSelectorB = new AlphaSelector(context, 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        objectSelectorC = new AlphaSelector(context, Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);

        objectSave = new TexturedMeshObject("OBJ_SAVE", true, Data.getMesh(context, R.array.OBJ_LABEL_SAVE), Data.getTextures(context, R.array.OBJ_LABEL_SAVE), 0.0f, -2.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        objectSave.setScale(0.5f, 0.5f, 0.5f);
    }

    @Override
    public void input(float[] headView) {
        objectSelectorA.next(headView);
        objectSelectorA.prev(headView);
        objectSelectorB.next(headView);
        objectSelectorB.prev(headView);
        objectSelectorC.next(headView);
        objectSelectorC.prev(headView);

        if(objectSave.isLookedAt(headView)) {
            FLAG_SAVE = true;
        }
    }

    @Override
    public State update() {
        if(FLAG_SAVE) {
            ScoreManager manager = new ScoreManager(context, pathString);
            String name = String.valueOf(objectSelectorA.getChar()) + String.valueOf(objectSelectorB.getChar()) + String.valueOf(objectSelectorC.getChar());
            manager.setNewScore(name, score);
            connected.get(0).onDisplay();
            return connected.get(0);
        }

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        objectSelectorA.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectSelectorB.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectSelectorC.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);

        objectSave.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }

    /**
     * State expects to be passed a score.
     * @param   value                           score.
     */
    @Override
    public void passData(int value) {
        score = value;
    }

    /**
     * state expects to be passed a file path
     * @param   string                          path.
     */
    @Override
    public void passData(String string) {
        pathString = string;
    }
}
