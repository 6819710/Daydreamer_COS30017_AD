package com.jpgalovic.daydreamerrelease.model.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;

/**
 * Object Texture.
 * Contains required methods to contain a texture.
 */
public class Texture {
    private final int[] textureID = new int[1];

    /**
     * Constructs Texture.
     * @param   context         App context.
     * @param   path            Path to texture.
     * @throws  IOException     If IOException occurs.
     */
    public Texture (Context context, String path) throws IOException {
        GLES20.glGenTextures(1,textureID, 0);
        bind();
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        Bitmap textureBitmap = BitmapFactory.decodeStream(context.getAssets().open(path));
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
        textureBitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }

    /**
     * Binds texture to GL_TEXTURES.
     */
    public void bind() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
    }
}
