package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

public class Loading extends State {
    // Object Data
    TexturedMeshObject objectLoadingBar;

    // State Data
    int audioFileProgress;
    int objectFileProgress;

    int loadCount;
    int loadDivisor;

    int index;

    public Loading(Context context) {
        super("STATE_LOADING", context);
    }

    @Override
    public void init() {
        index = 0;
        loadCount = 0;
        loadDivisor = context.getResources().getStringArray(R.array.OBJ_MESH_FILES).length + context.getResources().getStringArray(R.array.ADO_FILES).length;

        Texture[] tex = new Texture[]{
                Data.loading_textures.get(0), Data.loading_textures.get(1),
                Data.loading_textures.get(2), Data.loading_textures.get(3),
                Data.loading_textures.get(4), Data.loading_textures.get(5),
                Data.loading_textures.get(6), Data.loading_textures.get(7),
                Data.loading_textures.get(8), Data.loading_textures.get(9),
                Data.loading_textures.get(10),
        };

        objectLoadingBar = new TexturedMeshObject("OBJ_LOADING_BAR", false, Data.loading_meshes.get(0), tex, 0.0f, 0.0f, -5.0f, 0.0f, 180.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        // Load init other states, then load navigation state.
        if(Data.FLAG_TEXTURES_LOADED && Data.FLAG_MESHES_LOADED && Data.FLAG_AUDIO_LOADED) {
            for(int i = 0; i < connected.size(); i++) {
                connected.get(i).init();
            }
            connected.get(0).onDisplay();
            return connected.get(0);
        }

        objectFileProgress = Data.loadMeshesProgress;
        audioFileProgress = Data.loadAudioFilesProgress;

        if(loadCount != audioFileProgress + objectFileProgress) {
            loadCount = audioFileProgress + objectFileProgress;

            if(loadCount <= (loadDivisor / 10)) {
                index = 0;
            } else if(loadCount <= (loadDivisor / 10) * 2) {
                index = 1;
            } else if(loadCount <= (loadDivisor / 10) * 3) {
                index = 2;
            } else if(loadCount <= (loadDivisor / 10) * 4) {
                index = 3;
            } else if(loadCount <= (loadDivisor / 10) * 5) {
                index = 4;
            } else if(loadCount <= (loadDivisor / 10) * 6) {
                index = 5;
            } else if(loadCount <= (loadDivisor / 10) * 7) {
                index = 6;
            } else if(loadCount <= (loadDivisor / 10) * 8) {
                index = 7;
            } else if(loadCount <= (loadDivisor / 10) * 9) {
                index = 8;
            } else if(loadCount <= loadDivisor) {
                index = 9;
            } else{
                index = 10;
            }
        }

        objectFileProgress = Data.loadMeshesProgress;
        audioFileProgress = Data.loadAudioFilesProgress;



        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        objectLoadingBar.render(perspective, view, index, objectProgram, objectModelViewProjectionParam);
    }
}
