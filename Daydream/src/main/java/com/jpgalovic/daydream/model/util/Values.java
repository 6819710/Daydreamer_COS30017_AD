package com.jpgalovic.daydream.model.util;

public class Values {
    public static final float ANGLE_THRESHOLD = 0.2f;
    public static final float ANGLE_THRESHOLD_FINE = 0.1f;

    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 20.0f;

    public static final float MAX_YAW = 100.0f;
    public static final float MAX_PITCH = 25.0f;

    public static final float MIN_TARGET_DISTANCE = 2.0f;
    public static final float MAX_TARGET_DISTANCE = 10.0f;

    public static final String[] OBJECT_VERTEX_SHADER =
            new String[] {
                "uniform mat4 u_MVP;",
                "attribute vec4 a_Position;",
                "attribute vec2 a_UV;",
                "varying vec2 v_UV;",
                "",
                "void main() {",
                "  v_UV = a_UV;",
                "  gl_Position = u_MVP * a_Position;",
                "}",
            };

    public static final String[] OBJECT_FRAGMENT_SHADER =
            new String[] {
                "precision mediump float;",
                "varying vec2 v_UV;",
                "uniform sampler2D u_Texture;",
                "",
                "void main() {",
                "  // The y coordinate of this sample's textures is reversed compared to",
                "  // what OpenGL expects, so we invert the y coordinate.",
                "  gl_FragColor = texture2D(u_Texture, vec2(v_UV.x, 1.0 - v_UV.y));",
                "}",
            };
}
