package com.jpgalovic.daydream.model.object.compound;

import android.content.Context;
import android.opengl.Matrix;

import com.jpgalovic.daydream.Data;
import com.jpgalovic.daydream.R;
import com.jpgalovic.daydream.model.object.drawable.Compound;
import com.jpgalovic.daydream.model.object.drawable.TexturedMeshObject;

import java.util.ArrayList;

public class Piano extends Compound {

    public Piano(Context context, float x, float y, float z, float pitch, float yaw, float roll) {
        super(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

        objects = new ArrayList<>();
        offsets = new ArrayList<>();

        float[] data = new float[16];
        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.81f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("C", true, Data.getMesh(context, R.array.OBJ_LEFT_KEY), Data.getTextures(context, R.array.OBJ_LEFT_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.675f, 0.0f, -0.25f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("C#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.54f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("D", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.405f, 0.0f, -0.25f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("D#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, -0.27f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("E", true, Data.getMesh(context, R.array.OBJ_RIGHT_KEY), Data.getTextures(context, R.array.OBJ_RIGHT_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.0f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("F", true, Data.getMesh(context, R.array.OBJ_LEFT_KEY), Data.getTextures(context, R.array.OBJ_LEFT_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.135f, 0.0f, -0.25f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("F#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.27f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("G", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.405f, 0.0f, -0.25f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("G#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.54f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("A", true, Data.getMesh(context, R.array.OBJ_MIDDLE_KEY), Data.getTextures(context, R.array.OBJ_MIDDLE_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.675f, 0.0f, -0.25f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("A#", true, Data.getMesh(context, R.array.OBJ_SHARP_KEY), Data.getTextures(context, R.array.OBJ_SHARP_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        Matrix.setIdentityM(data, 0);
        Matrix.translateM(data, 0, 0.81f, 0.0f, 0.0f);
        offsets.add(data.clone());
        objects.add(new TexturedMeshObject("B", true, Data.getMesh(context, R.array.OBJ_RIGHT_KEY), Data.getTextures(context, R.array.OBJ_RIGHT_KEY), data[12], data[13], data[14], 0.0f, 0.0f, 0.0f));

        translate(x, y, z);
        setRotation(pitch, yaw, roll);
    }
}
