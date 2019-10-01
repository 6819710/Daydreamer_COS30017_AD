package com.jpgalovic.daydream.model;

import android.content.Context;

import java.util.ArrayList;

public abstract class State {
    protected static String TAG;

    protected ArrayList<State> connected;

    /**
     * Default constructor.
     */
    public State() {
        TAG = "State";
    }

    /**
     * Basic constructor. Sets object TAG.
     * @param tag
     */
    public State(String tag) {
        this.TAG = tag;
    }

    /**
     * Adds given state to array list of connected states.
     * @param state                         Connected state.
     */
    public void addConnection(State state) {
        connected.add(state);
    }

    /**
     * Initialises state.
     * @param context                       Application Context.
     * @param objectPositionParam           Object Position Parameter.
     * @param objectUVParam                 Object UV Parameter.
     */
    public abstract void init(Context context, int objectPositionParam, int objectUVParam);

    /**
     * Processees User Inputs. TODO: Determine types of imput to expose to method.
     */
    public abstract void input();

    /**
     * Updates the state.
     */
    public abstract State update();

    /**
     * Renders the view for a given perspective.
     * @param perspective                       Perspective Array.
     * @param view                              View Array.
     * @param headView                          Head View Array.
     * @param objectProgram                     Object Program ID.
     * @param objectModelViewProjectionParam    Object Model View Projection Parameter.
     */
    public abstract void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam);
}
