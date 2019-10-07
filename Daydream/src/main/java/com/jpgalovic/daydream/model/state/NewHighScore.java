package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.score.ScoreManager;

public class NewHighScore extends State {
    // Object Data

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
    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update(int positionAttribute, int uvAttribute) {
        // TODO: Process State Updates.
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        // TODO: Render Scene
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
