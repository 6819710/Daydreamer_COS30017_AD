package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.compound.ScoreDisplay;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.score.ScoreManager;

import java.util.ArrayList;

public class HighScores extends State {
    // Object Data
    private ArrayList<ScoreDisplay> objectScoreDisplay;
    private TexturedMeshObject objectBackButton;
    private TexturedMeshObject objectFindTheBlock;
    private TexturedMeshObject objectBlock;

    // State Data
    private ScoreManager scoreManager;

    private boolean FLAG_EXIT;

    public HighScores(Context context) {
        super("STATE_HIGH_SCORES", context);
        objectScoreDisplay = new ArrayList<>();
    }

    @Override
    public void onDisplay() {
        scoreManager = new ScoreManager(context, context.getResources().getString(R.string.file_find_the_block));

        objectScoreDisplay.clear();

        for(int i = 0; i < 12; i++) {
            objectScoreDisplay.add(new ScoreDisplay(context, scoreManager.getScore(i), 0.0f, -6.4f + (i * 1.4f), - 17.0f, 0.0f, 0.0f, 0.0f));
        }

        FLAG_EXIT = false;
    }

    @Override
    public void init() {
        objectBackButton = new TexturedMeshObject("OBJ_BACK", false, Data.getMesh(context, R.array.OBJ_LABEL_BACK), Data.getTextures(context, R.array.OBJ_LABEL_BACK), 0.0f, -3.4f, -5.0f, 0.0f, 0.0f, 0.0f);
        objectBackButton.setScale(0.5f, 0.5f, 0.5f);

        objectFindTheBlock = new TexturedMeshObject("OBJECT_FIND_THE_BLOCK_LABEL", false, Data.getMesh(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), Data.getTextures(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), 0.0f,  4.4f, -5.0f, 0.0f, 0.0f, 0.0f);

        objectBlock = new TexturedMeshObject("OBJECT_BLOCK", false, Data.getMesh(context, R.array.OBJ_FTB_BLOCK), Data.getTextures(context, R.array.OBJ_FTB_BLOCK), 0.0f, 4.8f, -6.0f, 0.0f, 0.0f, 0.0f);
        objectBlock.setScale(0.5f, 0.5f, 0.5f);
    }

    @Override
    public void input(float[] headView) {
        if(objectBackButton.isLookedAt(headView)) {
            FLAG_EXIT = true;
        }
    }

    @Override
    public State update() {
        if(FLAG_EXIT == true) {
            connected.get(0).onDisplay();
            return connected.get(0);
        }

        objectBlock.rotate(0.1f, 0.5f, -0.5f);

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        for(int i = 0; i < objectScoreDisplay.size(); i++) {
            objectScoreDisplay.get(i).render(perspective, view, objectProgram, objectModelViewProjectionParam);
        }
        objectBackButton.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);

        objectBlock.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        objectFindTheBlock.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
    }
}
