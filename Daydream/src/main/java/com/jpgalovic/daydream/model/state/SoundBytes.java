package com.jpgalovic.daydream.model.state;

import android.content.Context;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.State;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;
import com.jpgalovic.daydream.model.util.Timer;

public class SoundBytes extends State {
    // Object Data
    private TexturedMeshObject speakerFrontLeft;
    private TexturedMeshObject speakerFrontRight;

    private TexturedMeshObject backButton;

    // State Data
    private Timer timer;

    private boolean FLAG_EXIT;

    public SoundBytes(Context context) {
        super("STATE_SOUND_BYTES", context);
    }

    @Override
    public void onDisplay() {
        timer = new Timer(3);
        timer.start();

        FLAG_EXIT = false;
    }

    @Override
    public void init() {
        speakerFrontLeft = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), -5.0f, 0.0f, -5.0f, 0.0f, 30.0f, 0.0f);
        speakerFrontRight = new TexturedMeshObject("OBJ_SPEAKER_FRONT_LEFT", false, Data.getMesh(context, R.array.OBJ_SPEAKER), Data.getTextures(context, R.array.OBJ_SPEAKER), 5.0f, 0.0f, -5.0f, 0.0f, -30.0f, 0.0f);

        speakerFrontLeft.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[0]); // Left Sample File
        speakerFrontRight.setAudio(context.getResources().getStringArray(R.array.ADO_FILES)[1]); // Right Audio Sample File

        backButton = new TexturedMeshObject("OBJ_BACK", false, Data.getMesh(context, R.array.OBJ_LABEL_BACK), Data.getTextures(context, R.array.OBJ_LABEL_BACK), 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f);
        backButton.setScale(0.5f, 0.5f, 0.5f);
    }

    @Override
    public void input(float[] headView) {
        if(backButton.isLookedAt(headView)) {
            FLAG_EXIT = true;
        }
    }

    @Override
    public State update() {
        if(timer.zero()) {
            timer = new Timer(10); // 10 second timer (rough length of audio track)
            timer.start();
            speakerFrontLeft.playAudio(false);
            speakerFrontRight.playAudio(false);
        }

        if(FLAG_EXIT == true) {
            speakerFrontLeft.stopAudio();
            speakerFrontRight.stopAudio();

            connected.get(0).onDisplay();
            return connected.get(0);
        }

        return this;
    }

    @Override
    public void render(float[] perspective, float[] view, float[] headView, int objectProgram, int objectModelViewProjectionParam) {
        speakerFrontLeft.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        speakerFrontRight.render(perspective, view, 0, objectProgram, objectModelViewProjectionParam);
        backButton.render(perspective, view, headView, objectProgram, objectModelViewProjectionParam);
    }
}
