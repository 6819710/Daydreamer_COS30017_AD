package com.jpgalovic.daydream.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

public abstract class State {
    protected static String TAG;

    protected ArrayList<State> connected;

    protected Random rand = new Random();
    protected Context context;

    /**
     * Default constructor.
     */
    public State() {
        TAG = "State";
        connected = new ArrayList<>();
    }

    /**
     * Basic constructor. Sets object TAG.
     * @param   tag                             TAG of state.
     */
    public State(String tag, Context context) {
        this.TAG = tag;
        this.context = context;
        connected = new ArrayList<>();
    }

    /**
     * Adds given state to array list of connected states.
     * @param   state                           Connected state.
     */
    public void addConnection(State state) {
        connected.add(state);
    }

    /**
     * Method to be run on displayed.
     * NOTE: Should only be called by connected states.
     * Expected usage:
     * connected.get().onDisplay();
     * return connected.get();
     */
    public void onDisplay() {}

    /**
     * Initialises state.
     */
    public abstract void init();

    /**
     * Processees User Inputs. TODO: Determine types of input to expose to method.
     */
    public abstract void input(float[] headView);

    /**
     * Updates the state.
     */
    public abstract State update();

    /**
     * Renders the view for a given perspective.
     * @param   perspective                     Perspective Array.
     * @param   view                            View Array.
     * @param   headView                        Head View Array.
     * @param   objectProgram                   Object Program ID.
     * @param   objectModelViewProjectionParam  Object Model View Projection Parameter.
     */
    public abstract void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam);

    /**
     * Allows state to be passed an integer.
     * @param   value                           Value to pass.
     */
    public void passData(int value) {}

    /**
     * Allows state to be passed a string.
     * @param   string                          String to pass.
     */
    public void passData(String string) {}
}
