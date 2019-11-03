package com.jpgalovic.daydreamer.model;

import android.opengl.GLES20;
import android.util.Log;

import java.util.Random;

import static android.opengl.GLU.gluErrorString;

public class Util {
    private static final String TAG = "UTIL";
    private static Random rand = new Random();

    private static final boolean FLAG_HALT_ON_GL_ERROR = true;

    private Util(){}

    /**
     * Checks for GLES20 errors
     * @param   tag                         Tag of entity checking for errors.
     * @param   label                       Label to report in case of error.
     * @return                              true if and only if error has occurred.
     * @throws  RuntimeException            if GLES20 error has occurred and FLAG_HALT_ON_GL_ERROR is true.
     */
    public static boolean checkGLError(String tag, String label) throws RuntimeException {
        int error = GLES20.glGetError();
        int lastError;

        if(error != GLES20.GL_NO_ERROR) {
            do {
                lastError = error;
                Log.e(TAG, tag + "." + label + ": GLError " + gluErrorString(lastError));
                error = GLES20.glGetError();
            } while (error != GLES20.GL_NO_ERROR);

            if(FLAG_HALT_ON_GL_ERROR) {
                throw new RuntimeException(tag + "." + label + ": GLError " + gluErrorString(lastError));
            }
            return true;
        }
        return false;
    }
}
