package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.compound.ScoreTicker;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.object.compound.SevenSegmentTimer;
import com.jpgalovic.daydream.model.score.ScoreManager;
import com.jpgalovic.daydream.model.util.Timer;
import com.jpgalovic.daydream.model.util.Util;

public class FindTheBlock extends State {
    // Object Data
    private SevenSegmentTimer objectSevenSegmentTimer;
    private TexturedMeshObject block;

    // State Data
    private Timer timer;
    private ScoreTicker ticker;

    private boolean FLAG_EXIT;
    private boolean FLAG_BLOCK_FOUND;

    private int score;

    public FindTheBlock(Context context) {
        super("STATE_FIND_THE_BLOCK", context);
    }

    @Override
    public void onDisplay() {
        objectSevenSegmentTimer.start(30);
        timer = new Timer();

        ticker = new ScoreTicker(context, 0.0f, 2.0f, -10.0f, 0.0f, 0.0f, 0.0f);

        FLAG_EXIT = false;
        FLAG_BLOCK_FOUND = false;

        block.enableDisplay();

        score = 0;
    }

    @Override
    public void init() {
        objectSevenSegmentTimer = new SevenSegmentTimer(context, 0.0f, 0.0f, -10.0f);
        ticker = new ScoreTicker(context, 0.0f, 2.0f, -10.0f, 0.0f, 0.0f, 0.0f);
        block = new TexturedMeshObject("OBJECT_BLOCK", false, Data.getMesh(context, R.array.OBJ_FTB_BLOCK), Data.getTextures(context, R.array.OBJ_FTB_BLOCK), 0.0f, 0.0f, -8.0f, 0.0f, 0.0f, 0.0f);

        float[] position = Util.randomPosition();
        block.setTranslation(position[12], position[13], -position[14]);
    }

    @Override
    public void input(float[] headView) {
        if(block.isLookedAt(headView)) {
            FLAG_BLOCK_FOUND = true;
        }
    }

    @Override
    public State update() {
        if(FLAG_BLOCK_FOUND == true && FLAG_EXIT == false) {
            score += rand.nextInt((20 - 10) + 1) + 10;
            ticker.setScore(score);
            float[] position = Util.randomPosition();
            block.setTranslation(position[12], position[13], -position[14]);
            FLAG_BLOCK_FOUND = false;
        }

        if(timer.zero()) { // Exit Timer has expired.
            ScoreManager scoreManager = new ScoreManager(context, context.getResources().getString(R.string.file_find_the_block));
            if(scoreManager.isValidScore(score)) { // new high score, load new high score state.
                connected.get(0).onDisplay();
                connected.get(0).passData(score);
                connected.get(0).passData(context.getResources().getString(R.string.file_find_the_block));
                return connected.get(0);
            } else { // load high scores state.
                connected.get(1).onDisplay();
                return connected.get(1);
            }
        } else if(objectSevenSegmentTimer.zero() && FLAG_EXIT == false) { // Game timer expired, set exit timer. TODO: Enable Display of Game Over.
            block.disableDisplay(); // stop showing block at end of game.

            timer = new Timer(3);
            timer.start();
            
            FLAG_EXIT = true;
        }

        block.rotate(0.1f, 0.5f, 0.1f);

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        objectSevenSegmentTimer.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        ticker.render(perspective, view, objectProgram, objectModelViewProjectionParam);
        block.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }
}
