package com.jpgalovic.daydream.model;

import android.content.Context;

public class StateTemplate extends State {
    // Object Data
    // TODO: Add Object Data Here

    // State Data
    // TODO: Add State Data Here


    @Override
    public void init(Context context, int objectPositionParam, int objectUVParam) {
        // TODO: Initialise each Object and StateData.
    }

    @Override
    public void input() {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        // TODO: Process State Updates.
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        // TODO: Render Scene
    }
}
