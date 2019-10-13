package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

public class SoundBytes extends State {
    // Object Data
    private TexturedMeshObject speakerFrontLeft;
    private TexturedMeshObject speakerFrontRight;
    private TexturedMeshObject speakerBackLeft;
    private TexturedMeshObject speakerBackRight;

    // State Data
    int count;

    public SoundBytes(Context context) {
        super("STATE_SOUND_BYTES", context);
        count = 0;
    }

    @Override
    public void init() {
        speakerFrontLeft = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), -5.0f, 0.0f, -5.0f, 0.0f, 45.0f, 0.0f);
        speakerFrontRight = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), 5.0f, 0.0f, -5.0f, 0.0f, -45.0f, 0.0f);
        speakerBackLeft = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), -5.0f, 0.0f, 5.0f, 0.0f, 135.0f, 0.0f);
        speakerBackRight = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), 5.0f, 0.0f, 5.0f, 0.0f, -135.0f, 0.0f);

        speakerFrontLeft.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[0]); // C
        speakerFrontRight.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[2]); // D
        speakerBackLeft.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[4]); // E
        speakerBackRight.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[5]); // F

    }

    @Override
    public void input(float[] headView) {
        // TODO: Process User Inputs.
    }

    @Override
    public State update() {
        count++;

        if (count == 0) {
            speakerFrontLeft.playAudio(false);
        } else if (count == 100) {
            speakerFrontRight.playAudio(false);
        } else if (count == 200) {
            speakerBackLeft.playAudio(false);
        } else if (count == 300) {
            speakerBackRight.playAudio(false);
        } else if (count == 400) {
            count = -1;
        }
        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        speakerFrontLeft.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        speakerFrontRight.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        speakerBackLeft.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        speakerBackRight.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
    }
}
