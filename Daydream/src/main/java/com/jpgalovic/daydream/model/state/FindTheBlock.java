package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.object.compound.SevenSegmentTimer;
import com.jpgalovic.daydream.model.score.ScoreManager;
import com.jpgalovic.daydream.model.util.Timer;
import com.jpgalovic.daydream.model.util.Util;

public class FindTheBlock extends State {
    // Object Data
    private SevenSegmentTimer sevenSegmentTimer;
    private TexturedMeshObject block;

    // State Data
    private Timer timer;

    private boolean flagExit;
    private boolean flagBlockFound;

    private int score;

    public FindTheBlock(Context context) {
        super("STATE_FIND_THE_BLOCK", context);
    }

    @Override
    public void onDisplay(int positionAttribute, int uvAttribute) {
        sevenSegmentTimer.start(30);
        timer = new Timer();

        flagExit = false;
        flagBlockFound = false;

        score = 0;
    }

    @Override
    public void init(int objectPositionParam, int objectUVParam) {
        sevenSegmentTimer = new SevenSegmentTimer(context, objectPositionParam, objectUVParam, 0.0f, 0.0f, -10.0f);
        block = new TexturedMeshObject("OBJECT_BLOCK", false, Data.blockMeshes.get(0), new Texture[]{Data.blockTextures.get(0), Data.blockTextures.get(1)}, 0.0f, 0.0f, -8.0f, 0.0f, 0.0f, 0.0f);

        float[] position = Util.randomPosition();
        block.setPosition(position[12], position[13], -position[14]);
    }

    @Override
    public void input(float[] headView) {
        if(block.isLookedAt(headView)) {
            flagBlockFound = true;
        }
    }

    @Override
    public State update(int positionAttribute, int uvAttribute) {
        if(flagBlockFound == true && flagExit == false) {
            score += rand.nextInt((20 - 10) + 1) + 10;
            float[] position = Util.randomPosition();
            block.setPosition(position[12], position[13], -position[14]);
            flagBlockFound = false;
        }

        if(timer.zero()) { // Exit Timer has expired.
            ScoreManager scoreManager = new ScoreManager(context, context.getResources().getString(R.string.file_find_the_block));
            if(scoreManager.isValidScore(score)) { // new high score, load new high score state.
                connected.get(0).onDisplay(positionAttribute, uvAttribute);
                connected.get(0).passData(score);
                connected.get(0).passData(context.getResources().getString(R.string.file_find_the_block));
                return connected.get(0);
            } else { // load high scores state.
                connected.get(1).onDisplay(positionAttribute, uvAttribute);
                return connected.get(1);
            }
        } else if(sevenSegmentTimer.zero() && flagExit == false) { // Game timer expired, set exit timer. TODO: Enable Display of Game Over.
            timer = new Timer(3);
            timer.start();
            flagExit = true;
        }

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        sevenSegmentTimer.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        block.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }
}
