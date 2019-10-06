package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.compound.AlphaNumeric;
import com.jpgalovic.daydream.model.object.compound.ScoreDisplay;
import com.jpgalovic.daydream.model.object.score.Score;
import com.jpgalovic.daydream.model.object.score.ScoreManager;
import com.jpgalovic.daydream.model.util.Timer;
import com.jpgalovic.daydream.model.util.Values;

import java.util.ArrayList;

public class HighScores extends State {
    // Object Data
    private ArrayList<ScoreDisplay> scoreDisplay;

    // State Data
    private ScoreManager scoreManager;
    private Timer exitTimer;

    public HighScores(Context context) {
        super("STATE_HIGH_SCORES", context);
        scoreManager = new ScoreManager(context, context.getResources().getString(R.string.file_find_the_block));
        scoreDisplay = new ArrayList<>();
    }

    @Override
    public void onDisplay(int positionAttribute, int uvAttribute) {
        scoreDisplay.clear();
        for(int i = 0; i < 12; i++) {
            scoreDisplay.add(new ScoreDisplay(context, scoreManager.getScore(i), positionAttribute, uvAttribute, 0.0f, -8.4f + (i * 1.4f), - 15.0f, 0.0f, 0.0f, 0.0f));
        }

        exitTimer = new Timer(10);
        exitTimer.start();
    }

    @Override
    public void init(int positionAttribute, int uvAttribute) {

    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update(int positionAttribute, int uvAttribute) {
        if(exitTimer.zero()) {
            connected.get(0).onDisplay(positionAttribute, uvAttribute);
            return connected.get(0);
        }

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        for(int i = 0; i < scoreDisplay.size(); i++) {
            scoreDisplay.get(i).render(perspective, view, objectProgram, objectModelViewProjectionParam);
        }
    }
}
