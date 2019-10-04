package com.jpgalovic.daydream.model.util;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.util.Log;

import java.util.Random;

import static android.opengl.GLU.gluErrorString;

/**
 * Utility Functions
 */
public class Util {
    private static final String TAG = "Util";

    private static Random rand = new Random();

    /** Flag to enable debug builds to fail quickly. IMPORTANT: Disable flag for release version. */
    private  static final boolean HALT_ON_GL_ERROR = true;

    /** Class only contains static utility methods */
    private Util(){}

    /**
     * Checks GLES20.glGetError and fails quickly if the state isn't GL_NO_ERROR.
     *
     * @param   label           Label to report in case of error.
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
     * The vertex and fragment shader is passed as arrays of strings in order to make compilation
     * issues easier.
     *
     * @param   vertexCode      GELS20 vertex shader program.
     * @param   fragmentCode    GLES20 fragment shader program.
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

    /**
     * Calculates angel between two vectors.
     */
    public static float angleBetweenVectors(float[] vec1, float[] vec2) {
        float cosOfAngel = dotProduct(vec1, vec2) / (vectorNorm(vec1) * vectorNorm(vec2));
        return (float) Math.acos(Math.max(-1.0f, Math.min(1.0f, cosOfAngel)));
    }

    private static float dotProduct(float[] vec1, float[]vec2) {
        return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
    }

    private static float vectorNorm(float[] vec) {
        return Matrix.length(vec[0], vec[1], vec[2]);
    }

    /**
     * Calculates a random position within the scope of the distances set by MAX_YAW, MAX_PITCH and MAX_TARGET_DISTANCE/MIN_TARGET_DISTANCE
     * @return                  Matrix with random position.
     */
    public static float[] randomPosition() {
        // Calculate random yaw, pitch and distance values.
        float theta = (float) Math.toRadians((rand.nextFloat() - 0.5f) * 2.0f * Values.MAX_YAW);
        float phi = (float) Math.toRadians((rand.nextFloat() - 0.5f) * 2.0f * Values.MAX_PITCH);
        float magnitude = rand.nextFloat() * (Values.MAX_TARGET_DISTANCE - Values.MIN_TARGET_DISTANCE) + Values.MIN_TARGET_DISTANCE;

        return calculatePosition(magnitude, theta, phi);
    }

    /**
     * Calculates vector components from angles theta and phi, and magnitude.
     * @param   magnitude       Magnitude of Vector
     * @param   theta           Angle Theta of Vector
     * @param   phi             Angle Phi of Vector
     * @return                  Matrix with calculated position.
     */
    public static float[] calculatePosition(float magnitude, float theta, float phi) {
        float[] result = new float[16];

        float x = (float) (magnitude * Math.sin(theta) * Math.cos(phi));
        float y = (float) (magnitude * Math.sin(theta) * Math.sin(phi));
        float z = (float) (magnitude * Math.cos(theta));

        Matrix.setIdentityM(result, 0);
        Matrix.translateM(result, 0, x, y, z);

        return result;
    }
}
