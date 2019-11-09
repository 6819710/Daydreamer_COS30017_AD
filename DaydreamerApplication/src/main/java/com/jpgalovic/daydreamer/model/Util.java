package com.jpgalovic.daydreamer.model;

import android.opengl.GLES20;
import android.text.TextUtils;
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

    public static int compileProgram(String tag, String[] vetexCode, String[] fragmentCode) {
        checkGLError(tag, "Start of compileProgram");

        // prepare shaders and OpenGL program
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, TextUtils.join("\n", vetexCode));
        GLES20.glCompileShader(vertexShader);
        checkGLError(tag, "Compile vertex shader");

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, TextUtils.join("\n", fragmentCode));
        GLES20.glCompileShader(fragmentShader);
        checkGLError(tag, "Compile fragment shader");

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        // Link GL Program and check for errors.
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if(linkStatus[0] != GLES20.GL_TRUE) {
            String errorMsg = "Unable to link shader program: \n" + GLES20.glGetProgramInfoLog(program);
            Log.e(TAG, errorMsg);
            if (FLAG_HALT_ON_GL_ERROR) {
                throw new RuntimeException(errorMsg);
            }
        }
        checkGLError(tag, "End of compileProgram");

        return program;
    }
}
