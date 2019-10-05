package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.TexturedMeshObject;

public class Navigation extends State {
    // Object Data
    private TexturedMeshObject objectCRT;
    private TexturedMeshObject objectTable;
    //private TexturedMeshObject objectFindTheBlock;
    //private TexturedMeshObject objectBlock;

    // State Data
    boolean loadCRT;

    public Navigation(Context context) {
        super("STATE_NAVIGATION", context);
        loadCRT = false;
    }

    @Override
    public void onDisplay() {
        loadCRT = false;
    }

    @Override
    public void init(int objectPositionParam, int objectUVParam) {
        objectCRT = new TexturedMeshObject(context, "OBJECT_CRT", false, context.getResources().getString(R.string.obj_crt_moniter_obj), context.getResources().getStringArray(R.array.obj_crt_monitor_tex), objectPositionParam, objectUVParam, 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f);
        objectTable = new TexturedMeshObject(context, "OBJECT_TABLE", false, context.getResources().getString(R.string.obj_table_obj), context.getResources().getStringArray(R.array.obj_table_tex), objectPositionParam, objectUVParam, 0.0f, -3.5f, -4.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        if(objectCRT.isLookedAt(headView)) {
            loadCRT = true;
        }
    }

    @Override
    public State update() {
        if(loadCRT) {
            connected.get(1).onDisplay();
            return connected.get(1);
        }

        objectCRT.rotate(0.0f, 0.5f, 0.0f);
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        objectCRT.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
        objectTable.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
    }
}
