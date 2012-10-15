package it.nando.android.labyrinth3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Sphere {
	public double mRadiusX = 1.0;
	public double mRadiusY = 1.0;
	public double mRadiusZ = 1.0;
	public double mCenterX = 0.0;
	public double mCenterY = 0.0;
	public double mCenterZ = 0.0;
	public double mVelocitaX = 0.0;
	public double mVelocitaY = 0.0;
	public double mVelocitaZ = 0.0;
	int nVerteX;
	int nVerteY;
	
	public Sphere(int nVertex) {
		this.nVerteX = nVertex;
		this.nVerteY = nVertex * 2;
	}
	public Sphere() {
		this.nVerteX = 10;
		this.nVerteY = 20;
	}
	public void setCenter(double x,double y,double z) {
        mCenterX = x;
		mCenterY = y;
		mCenterZ = z;
		//MoveTo(x,y,z);
	}
	public void setRadius(float rx,float ry,float rz) {
		mRadiusX = rx;
		mRadiusY = ry;
		mRadiusZ = rz;
	}
	public void setRadius(float r) {
		mRadiusX = r;
		mRadiusY = r;
		mRadiusZ = r;
	}
	public void MoveTo(double x,double y, double z) {
		int i,j;
		
		if (mVertexBuffer != null) {
			mVertexBuffer.position(0);
			
			for (i=0; i<=nVerteX; i++) {
	            double angleV = Math.PI * i / nVerteX;

	            double cosV = Math.cos(angleV);
	            double sinV = Math.sin(angleV);
	            for (j=0; j<nVerteY; j++) {
	                double angleU = Math.PI * 2 * j / nVerteY;
	                double cosU = Math.cos(angleU);
	                double sinU = Math.sin(angleU);
	                
	                double x1 = mRadiusX*(sinV * cosU);
	                double y1 = mRadiusY*(cosV);
	                double z1 = mRadiusZ*(sinV * sinU);

	                mVertexBuffer.put((float) (x1 + x));
	                mVertexBuffer.put((float) (y1 + y));
	                mVertexBuffer.put((float) (z1 + z));

	                if (i == 0 || i == nVerteX)
	                	break;
	            }
			}
		}
	}
	public void generate() {
		int i,j;
		
        int nVertices = 2 + ((nVerteX - 1) * nVerteY);
		ByteBuffer vbb = ByteBuffer.allocateDirect(nVertices * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.position(0);
		ByteBuffer vbb2 = ByteBuffer.allocateDirect(nVertices * 3 * 4);
        vbb2.order(ByteOrder.nativeOrder());
        mVertexLocalBuffer = vbb2.asFloatBuffer();
        mVertexLocalBuffer.position(0);
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(nVertices * 4 * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.position(0);

        nTriangles = 2 * nVerteY * (nVerteX - 1);
		ByteBuffer ibb = ByteBuffer.allocateDirect(nTriangles * 3 * 4);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.position(0);
        
		for (i=0; i<=nVerteX; i++) {
            double angleV = Math.PI * i / nVerteX;

            double cosV = Math.cos(angleV);
            double sinV = Math.sin(angleV);
            for (j=0; j<nVerteY; j++) {
                double angleU = Math.PI * 2 * j / nVerteY;
                double cosU = Math.cos(angleU);
                double sinU = Math.sin(angleU);
                
                double x = mRadiusX*(sinV * cosU);
                double y = mRadiusY*(cosV);
                double z = mRadiusZ*(sinV * sinU);

                //Log.e("tmp","x="+(float)x+" y="+(float)y+" z="+(float)z);
                
                mVertexBuffer.put((float) x);
                mVertexBuffer.put((float) y);
                mVertexBuffer.put((float) z);
                
                mVertexLocalBuffer.put((float) x);
                mVertexLocalBuffer.put((float) y);
                mVertexLocalBuffer.put((float) z);

                mColorBuffer.put((int) (0x10000 * i / nVerteX));
               	mColorBuffer.put((int) (0x10000 * i / nVerteX));
               	mColorBuffer.put((int) (0x10000 * i / nVerteX));
                /*
                mColorBuffer.put((int) (0x10000 * Math.random()));
               	mColorBuffer.put((int) (0x10000 * Math.random()));
               	mColorBuffer.put((int) (0x10000 * Math.random()));
               	*/
               	/*
                mColorBuffer.put((int) (0x10000 * (x + 1) / 2));
               	mColorBuffer.put((int) (0x10000 * (y + 1) / 2));
               	mColorBuffer.put((int) (0x10000 * (z + 1) / 2));
               	*/
               	mColorBuffer.put(0x10000);
                
                if (i == 0 || i == nVerteX)
                	break;
			}
		}
		for (i=0; i<nVerteX; i++) {
			for (j=0; j<nVerteY; j++) {
				if (i == 0) {
					mIndexBuffer.put((short) 0);
					mIndexBuffer.put((short) (j + 1));
					if (j == (nVerteY - 1))
						mIndexBuffer.put((short) 1);
					else
						mIndexBuffer.put((short) (j + 2));
				}
				else if (i == (nVerteX - 1)) {
					mIndexBuffer.put((short) ((nVerteY * (i - 1)) + j + 1));
					mIndexBuffer.put((short) ((nVerteY * (nVerteX - 1)) + 1));
					if (j == (nVerteY - 1))
						mIndexBuffer.put((short) ((nVerteY * (i - 1)) + 1));
					else
						mIndexBuffer.put((short) ((nVerteY * (i - 1)) + j + 2));
				}
				else {
					mIndexBuffer.put((short) ((nVerteY * (i - 1)) + j + 1));
					mIndexBuffer.put((short) ((nVerteY * i) + j + 1));
					if (j == (nVerteY - 1))
						mIndexBuffer.put((short) ((nVerteY * i) + 1));
					else
						mIndexBuffer.put((short) ((nVerteY * i) + j + 2));
					
					mIndexBuffer.put((short) ((nVerteY * (i - 1)) + j + 1));
					if (j == (nVerteY - 1)) {
						mIndexBuffer.put((short) ((nVerteY * i) + 1));
						mIndexBuffer.put((short) ((nVerteY * (i - 1)) + 1));
					}
					else {
						mIndexBuffer.put((short) ((nVerteY * i) + j + 2));
						mIndexBuffer.put((short) ((nVerteY * (i - 1)) + j + 2));
					}
				}
			}
		}
        mVertexBuffer.position(0);
        mVertexLocalBuffer.position(0);
        mColorBuffer.position(0);
        mIndexBuffer.position(0);

        mIndexBuffer.position(0);
        
        mM4 = new M4();
        mM4.setIdentity();
	}
    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        
        //gl.glMultMatrixf(mM4.toArray(),0); // rotazioni

        gl.glTranslatef((float)mCenterX,(float)mCenterY,(float)mCenterZ);
        
        gl.glDrawElements(GL10.GL_TRIANGLES, nTriangles * 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    }
    public void setRotation(float angle,int x,int y,int z) {
    	if (mM4 != null) {
        	double d = Math.toRadians(angle);
        	float s = (float) Math.sin(d);
        	float c = (float) Math.cos(d);
        	
        	M4 m4 = new M4();
        	m4.m[0][0] = x*(1f-c) + c;
        	m4.m[1][0] = (1f-c)*x*y - (s*z);
        	m4.m[2][0] = (1f-c)*x*z + (s*y);
        	
        	m4.m[0][1] = (1f-c)*x*y + (s*z);
        	m4.m[1][1] = y*(1f-c) + c;
        	m4.m[2][1] = (1f-c)*y*z - (s*x);
        	
        	m4.m[0][2] = (1f-c)*x*z - (s*y);
        	m4.m[1][2] = (1f-c)*y*z + (s*x);
        	m4.m[2][2] = z*(1f-c) + c;
        	
        	m4.m[3][0] = m4.m[3][1] = m4.m[3][2] = 0f;
        	m4.m[0][3] = m4.m[1][3] = m4.m[2][3] = 0f;
        	m4.m[3][3] = 1f;
        	
        	mM4 = mM4.multiply(m4);
    	}
    }
    private int nTriangles;
    private FloatBuffer mVertexBuffer = null;
    private FloatBuffer mVertexLocalBuffer = null;
    private IntBuffer   mColorBuffer = null;
    private ShortBuffer  mIndexBuffer = null;
    public  M4 mM4;
}
