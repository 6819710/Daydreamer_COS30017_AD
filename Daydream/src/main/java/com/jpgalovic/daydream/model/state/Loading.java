package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.Texture;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

public class Loading extends State {
    // Object Data
    TexturedMeshObject loadingBar;

    // State Data
    int index;

    public Loading(Context context) {
        super("STATE_LOADING", context);
    }

    @Override
    public void init() {
        index = 0;

        Texture[] tex = new Texture[]{
                Data.loading_textures.get(0), Data.loading_textures.get(1),
                Data.loading_textures.get(2), Data.loading_textures.get(3),
                Data.loading_textures.get(4), Data.loading_textures.get(5),
                Data.loading_textures.get(6), Data.loading_textures.get(7),
                Data.loading_textures.get(8), Data.loading_textures.get(9),
                Data.loading_textures.get(10),
        };

        loadingBar = new TexturedMeshObject("OBJ_LOADING_BAR", false, Data.loading_meshes.get(0), tex, 0.0f, 0.0f, -5.0f, 0.0f, 180.0f, 0.0f);
    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        // Load init other states, then load navigation state.
        if(Data.flag_textures_loaded && Data.flag_meshes_loaded) {
            for(int i = 0; i < connected.size(); i++) {
                connected.get(i).init();
            }
            connected.get(0).onDisplay();
            return connected.get(0);
        }

        index = Data.loadIndex;

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        loadingBar.render(perspective, view, index, objectProgram, objectModelViewProjectionParam);
    }
}
