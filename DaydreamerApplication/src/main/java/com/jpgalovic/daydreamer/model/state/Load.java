package com.jpgalovic.daydreamer.model.state;

import com.jpgalovic.daydreamer.R;
import com.jpgalovic.daydreamer.model.State;

public class Load extends State {
    // Object Data

    // State Data
    private int loadProgressAudio;
    private int loadProgressObject;

    private int loadProgressCount;
    private int loadProgressTarget;

    private int textureIndex;

    @Override
    public void init() {
        textureIndex = 0;
        loadProgressCount = 0;
        loadProgressTarget = context.getResources().getStringArray(R.array.OBJECT_MESH_FILES).length + context.getResources().getStringArray(R.array.AUDIO_FILES).length;

        //Pre-load Required Objects.

        //Load Meshes and Audio (async thread)

        //Load Textures (this tread)
    }

    @Override
    public void input(float[] headView) {
    }

    @Override
    public State update() {
        // Check if data has been loaded.

        // Update status of data loading.


        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int GLESObjectProgram, int GLESViewProjectionAttribute) {

    }
}
