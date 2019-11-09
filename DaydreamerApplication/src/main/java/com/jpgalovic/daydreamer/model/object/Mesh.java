package com.jpgalovic.daydreamer.model.object;

import android.content.Context;
import android.opengl.GLES20;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

public class Mesh {
    private final FloatBuffer vertices;
    private final FloatBuffer uv;
    private final ShortBuffer indices;
    private final int positionAttribute;
    private final int uvAttribute;

    /**
     * Constructs mesh from .obj file.
     * @param   context                 App context.
     * @param   path                    File path.
     * @param   positionAttribute       Position Attribute.
     * @param   uvAttribute             UV Attribute.
     * @throws  IOException             If IOException occurs.
     */
    public Mesh(Context context, String path, int positionAttribute, int uvAttribute) throws IOException {
        InputStream objInputStream = context.getAssets().open(path);
        Obj obj = ObjUtils.convertToRenderable(ObjReader.read(objInputStream));
        objInputStream.close();

        IntBuffer intIndices = ObjData.getFaceVertexIndices(obj, 3); // Gets buffer of face vertex indices
        vertices = ObjData.getVertices(obj); // gets vertices of mesh
        uv = ObjData.getTexCoords(obj, 2); // gets coordinates for texture.

        // Convert intIndices into shorts (GLES doesn't support int indices)
        indices = ByteBuffer.allocateDirect(2 * intIndices.limit()).order(ByteOrder.nativeOrder()).asShortBuffer();
        while (intIndices.hasRemaining()) {
            indices.put((short) intIndices.get());
        }
        indices.rewind();

        this.positionAttribute = positionAttribute;
        this.uvAttribute = uvAttribute;
    }

    /**
     * Renders the mesh.
     * Note: before this is called a u_MVP should be set with glUniformMatrix4fv(), and a texture should be bound to GL_TEXTURE0.
     */
    public void render() {
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertices);
        GLES20.glEnableVertexAttribArray(uvAttribute);
        GLES20.glVertexAttribPointer(uvAttribute, 2, GLES20.GL_FLOAT,false, 0, uv);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.limit(), GLES20.GL_UNSIGNED_SHORT, indices);
    }
}
