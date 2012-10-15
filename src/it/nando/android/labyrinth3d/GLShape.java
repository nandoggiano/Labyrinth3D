package it.nando.android.labyrinth3d;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class GLShape {
	protected ArrayList<GLFace>		mFaceList = new ArrayList<GLFace>();
	protected ArrayList<GLVertex>	mVertexList = new ArrayList<GLVertex>();
	protected ArrayList<Integer>	mIndexList = new ArrayList<Integer>();	// make more efficient?
	protected GLWorld mWorld;
	public GLShape(GLWorld world) {
		mWorld = world;
	}
	
	public void addFace(GLFace face) {
		mFaceList.add(face);
	}
	
	public void setFaceColor(int face, GLColor color) {
		GLFace glFace = mFaceList.get(face);
		if (glFace != null)
			glFace.setColor(color);
	}
			
	public void putIndices(ShortBuffer buffer) {
		Iterator<GLFace> iter = mFaceList.iterator();
		while (iter.hasNext()) {
			GLFace face = iter.next();
			if (face != null)
				face.putIndices(buffer);
		}		
	}
	
	public int getIndexCount() {
		int count = 0;
		Iterator<GLFace> iter = mFaceList.iterator();
		while (iter.hasNext()) {
			GLFace face = iter.next();
			if (face != null)
				count += face.getIndexCount();
		}		
		return count;
	}

	public GLVertex addVertex(float x, float y, float z) {
		
		// look for an existing GLVertex first
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			if (vertex.x == x && vertex.y == y && vertex.z == z) {
				return vertex;
			}
		}
		
		// doesn't exist, so create new vertex
		GLVertex vertex = mWorld.addVertex(x, y, z);
		mVertexList.add(vertex);
		return vertex;
	}
}
