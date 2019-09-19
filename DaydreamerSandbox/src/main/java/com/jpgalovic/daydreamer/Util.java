package com.jpgalovic.daydreamer;

import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

import static android.opengl.GLU.gluErrorString;

/**
 * Utility Functions
 */
public class Util {
    private static final String TAG = "Util";

    /** Flag to enable debug builds to fail quickly. IMPORTANT: Disable flag for release version. */
    private  static final boolean HALT_ON_GL_ERROR = true;

    /** Class only contains static utility methods */
    private Util(){}

    /**
     * Checks GLES20.glGetError and fails quickly if the state isn't GL_NO_ERROR.
     *
     * @param label Lable to report in case of error.
     */
    public static void checkGLError(String label) {
        int error = GLES20.glGetError();
        int lastError;
        if(error != GLES20.GL_NO_ERROR) {
            do {
                lastError = error;
                Log.e(TAG, label + ": glError " + gluErrorString(lastError));
                error = GLES20.glGetError();
            } while (error != GLES20.GL_NO_ERROR);

            if (HALT_ON_GL_ERROR) {
                throw new RuntimeException("glError" + gluErrorString(lastError));
            }
        }
    }

    /**
     * Builds GL shader program from a vertex and fragment shader code.
     * The vertex and fragment shaders are passed as arrays of strings in order to make compilation
     * issues easier.
     *
     * @param vertexCode GELS20 vertex shader program.
     * @param fragmentCode GLES20 fragment shader program.
     * @return GLES20 Program ID.
     */
    public static int compileProgram(String[] vertexCode, String[] fragmentCode) {
        checkGLError("Start of compileProgram");

        // prepare shaders and OpenGL program
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, TextUtils.join("\n",vertexCode));
        GLES20.glCompileShader(vertexShader);
        checkGLError("Compile vertex shader");

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, TextUtils.join("\n", fragmentCode));
        GLES20.glCompileShader(fragmentShader);
        checkGLError("Compile fragment shader");

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
            if (HALT_ON_GL_ERROR) {
                throw new RuntimeException(errorMsg);
            }
        }
        checkGLError("End of compileProgram");

        return program;
    }
}
