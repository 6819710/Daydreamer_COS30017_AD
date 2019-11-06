package com.jpgalovic.daydreamer.model;

import android.content.Context;

import java.util.ArrayList;

public abstract class State {
    protected static String TAG;

    protected ArrayList<State> connectedStates;
    protected Context context;

    public State() {
        TAG = "STATE";
        connectedStates = new ArrayList<>();
    }

    public State(String tag, Context context) {
        this.TAG = tag;
        this.context = context;
    }

    /**
     * Connects to state if connection does not already exist.
     * @param   state           State to connect.
     */
    public void addConnection(State state) {
        if(!hasConnection(state.TAG)) {
            connectedStates.add(state);
        }
    }

    /**
     * Checks if connection exists to given state tag.
     * @param   tag             Tag of state to check.
     * @return                  True if state exists in connection list.
     */
    protected boolean hasConnection(String tag) {
        for(int i = 0; i < connectedStates.size(); i++) {
            if(connectedStates.get(i).TAG == tag) {
                return true;
            }
        }
        return false;
    }

    public void onDisplay() {}

    public abstract void init();
    public abstract void input(float[] headView);
    public abstract State update();
    public abstract void render(float[] perspective, float[] view, float[] headView, int GLESObejctProgram, int GLESViewProjectionAttribute);

    public void pass(String tag, int value){}
    public void pass(String tag, String string){}
}
