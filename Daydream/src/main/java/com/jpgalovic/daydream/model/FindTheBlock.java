package com.jpgalovic.daydream.model;

import android.content.Context;

import com.jpgalovic.daydream.model.object.compound.SevenSegmentTimer;
import com.jpgalovic.daydream.model.util.Timer;

public class FindTheBlock extends State {
    // Object Data
    private SevenSegmentTimer sevenSegmentTimer;

    // State Data
    private Timer timer;

    private boolean flagExit;

    public FindTheBlock() {
        super("STATE_FIND_THE_BLOCK");
    }

    @Override
    public void onDisplay() {
        sevenSegmentTimer.start(10);
        timer = new Timer();
        flagExit = false;
    }

    @Override
    public void init(Context context, int objectPositionParam, int objectUVParam) {
        sevenSegmentTimer = new SevenSegmentTimer(context, objectPositionParam, objectUVParam, 0.0f, 0.0f, -10.0f);
    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        if(timer.zero()) { // Exit Timer has expired.
            connected.get(0).onDisplay();
            return connected.get(0);
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
    }
}
