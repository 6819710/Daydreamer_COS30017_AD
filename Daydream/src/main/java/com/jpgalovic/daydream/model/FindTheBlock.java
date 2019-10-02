package com.jpgalovic.daydream.model;

import android.content.Context;

import com.jpgalovic.daydream.model.util.Timer;

public class FindTheBlock extends State {
    // Object Data
    // TODO: Add Object Data Here

    // State Data
    Timer timer;

    public FindTheBlock() {
        super("STATE_FIND_THE_BLOCK");
    }

    @Override
    public void onDisplay() {
        timer = new Timer(10);
        timer.start();
    }

    @Override
    public void init(Context context, int objectPositionParam, int objectUVParam) {

    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        if(timer.getCount() == 0) { // Exit Timer has expired.
            connected.get(0).onDisplay();
            return connected.get(0);
        }
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        // TODO: Render Scene
    }
}
