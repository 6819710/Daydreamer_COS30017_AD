package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.compound.AlphaSelector;
import com.jpgalovic.daydream.model.score.ScoreManager;
import com.jpgalovic.daydream.model.util.Values;

public class NewHighScore extends State {
    // Object Data
    AlphaSelector alphaSelectorA;
    AlphaSelector alphaSelectorB;
    AlphaSelector alphaSelectorC;

    // State Data
    private ScoreManager scoreManager;

    private String pathString;
    private int score;

    public NewHighScore(Context context) {
        super("STATE_NEW_HIGH_SCORE", context);
        pathString = new String();
        score = 0;
    }

    @Override
    public void init(int positionAttribute, int uvAttribute) {
        scoreManager = new ScoreManager(context, pathString);

        alphaSelectorA = new AlphaSelector(-Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        alphaSelectorB = new AlphaSelector(0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        alphaSelectorC = new AlphaSelector(Values.ALPHANUMERIC_OFFSET_H, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        alphaSelectorA.next(headView);
        alphaSelectorA.prev(headView);
        alphaSelectorB.next(headView);
        alphaSelectorB.prev(headView);
        alphaSelectorC.next(headView);
        alphaSelectorC.prev(headView);
    }

    @Override
    public State update(int positionAttribute, int uvAttribute) {
        // TODO: Process State Updates.
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        alphaSelectorA.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        alphaSelectorB.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        alphaSelectorC.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
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
