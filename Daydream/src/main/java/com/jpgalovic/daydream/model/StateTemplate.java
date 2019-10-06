package com.jpgalovic.daydream.model;

import android.content.Context;

public class StateTemplate extends State {
    // Object Data
    // TODO: Add Object Data Here

    // State Data
    // TODO: Add State Data Here

    public StateTemplate(Context context) {
        super("STATE_TEMPLATE", context);
    }

    @Override
    public void init(int positionAttribute, int uvAttribute) {
        // TODO: Initialise each Object and StateData.
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
}
