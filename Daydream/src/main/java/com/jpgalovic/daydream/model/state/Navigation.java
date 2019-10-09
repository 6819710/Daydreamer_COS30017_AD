package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.Mesh;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

public class Navigation extends State {
    // Object Data
    private TexturedMeshObject objectCRT;
    private TexturedMeshObject objectTable;
    private TexturedMeshObject objectHighScores;
    private TexturedMeshObject objectFindTheBlock;
    private TexturedMeshObject objectBlock;

    // State Data
    boolean loadCRT;
    boolean loadFindTheBlock;

    public Navigation(Context context) {
        super("STATE_NAVIGATION", context);
        loadCRT = false;
        loadFindTheBlock = false;
    }

    @Override
    public void onDisplay(int positionAttribute, int uvAttribute) {
        loadCRT = false;
    }

    @Override
    public void init(int positionAttribute, int uvAttribute) {
        objectCRT = new TexturedMeshObject("OBJECT_CRT", false, Data.getMesh(context, R.array.OBJ_NAV_CRT), Data.getTextures(context, R.array.OBJ_NAV_CRT), 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectTable = new TexturedMeshObject("OBJECT_TABLE", false, Data.getMesh(context, R.array.OBJ_NAV_TABLE), Data.getTextures(context, R.array.OBJ_NAV_TABLE), 0.0f, -3.5f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectHighScores = new TexturedMeshObject("OBJECT_HIGH_SCORES_LABEL", false, Data.getMesh(context, R.array.OBJ_LABEL_HIGH_SCORES), Data.getTextures(context, R.array.OBJ_LABEL_HIGH_SCORES), 0.0f, 1.4f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectFindTheBlock = new TexturedMeshObject("OBJECT_FIND_THE_BLOCK_LABEL", false, Data.getMesh(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), Data.getTextures(context, R.array.OBJ_LABEL_FIND_THE_BLOCK), 3.4641f,  0.0f, -2.0f, 0.0f, -60.0f, 0.0f);
        objectBlock = new TexturedMeshObject("OBJECT_BLOCK", false, Data.getMesh(context, R.array.OBJ_FTB_BLOCK), Data.getTextures(context, R.array.OBJ_FTB_BLOCK), 5.19615f, 0.0f, -3.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        if(objectCRT.isLookedAt(headView)) {
            loadCRT = true;
        } else if (objectFindTheBlock.isLookedAt(headView)){
            loadFindTheBlock = true;
        }
    }

    @Override
    public State update(int positionAttribute, int uvAttribute) {
        if(loadCRT) { // Load High Scores.
            connected.get(0).onDisplay(positionAttribute, uvAttribute);
            return connected.get(0);
        } else if (loadFindTheBlock) { // Load Find The Block.
            connected.get(1).onDisplay(positionAttribute, uvAttribute);
            return connected.get(1);
        }

        objectCRT.rotate(0.0f, 0.5f, 0.0f);
        objectBlock.rotate(0.1f, 0.5f, -0.5f);
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        objectCRT.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectTable.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        objectHighScores.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        objectFindTheBlock.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectBlock.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }
}
