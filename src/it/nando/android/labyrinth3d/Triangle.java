package it.nando.android.labyrinth3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Triangle {
    private short[] mIndicesArray = {0, 1, 2};
    private int _nrOfVertices = 3;

    public Triangle(GLWorld world,GLColor color,float[] coords) {
        // float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
     
        // short has 4 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
     
        // float has 4 bytes, 4 colors (RGBA) * number of vertices * 4 bytes
        ByteBuffer cbb = ByteBuffer.allocateDirect(4 * _nrOfVertices * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
     
        float[] colors = {
            color.red, color.green, color.blue, color.alpha, // point 1
            color.red, color.green, color.blue, color.alpha, // point 2
            color.red, color.green, color.blue, color.alpha, // point 3
        };
     
        mFVertexBuffer.put(coords);
        mIndexBuffer.put(mIndicesArray);
        mColorBuffer.put(colors);
     
        mFVertexBuffer.position(0);
        mIndexBuffer.position(0);
        mColorBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS,
        		GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    }

    private int VERTS = 3;

    private FloatBuffer mFVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ShortBuffer mIndexBuffer;
}
