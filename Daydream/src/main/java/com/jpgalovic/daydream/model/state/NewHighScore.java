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
    private AlphaSelector alphaSelectorA;
    private AlphaSelector alphaSelectorB;
    private AlphaSelector alphaSelectorC;

    private TexturedMeshObject save;

    // State Data
    private ScoreManager scoreManager;

    private String pathString;
    private int score;

    private boolean flag_save;

    @Override
    public void onDisplay() {
        flag_save = false;
    }

    public NewHighScore(Context context) {
        super("STATE_NEW_HIGH_SCORE", context);
        pathString = new String();
        score = 0;
    }

    @Override
    public void init() {
        scoreManager = new ScoreManager(context, pathString);

        alphaSelectorA = new AlphaSelector(context, -Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        alphaSelectorB = new AlphaSelector(context, 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        alphaSelectorC = new AlphaSelector(context, Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);

        save = new TexturedMeshObject("OBJ_SAVE", true, Data.getMesh(context, R.array.OBJ_LABEL_SAVE), Data.getTextures(context, R.array.OBJ_LABEL_SAVE), 0.0f, -2.0f, -5.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        alphaSelectorA.next(headView);
        alphaSelectorA.prev(headView);
        alphaSelectorB.next(headView);
        alphaSelectorB.prev(headView);
        alphaSelectorC.next(headView);
        alphaSelectorC.prev(headView);

        if(save.isLookedAt(headView)) {
            flag_save = true;
        }
    }

    @Override
    public State update() {
        if(flag_save) {
            ScoreManager manager = new ScoreManager(context, pathString);
            String name = String.valueOf(alphaSelectorA.getChar()) + String.valueOf(alphaSelectorB.getChar()) + String.valueOf(alphaSelectorC.getChar());
            manager.setNewScore(name, score);
            connected.get(0).onDisplay();
            return connected.get(0);
        }

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        alphaSelectorA.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        alphaSelectorB.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        alphaSelectorC.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);

        save.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
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
