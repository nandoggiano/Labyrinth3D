package it.nando.android.labyrinth3d;

public class Cube extends GLShape {
	public Cube(GLWorld world, float left, float bottom, float back, float right, float top, float front,int floor) {
		super(world);
       	GLVertex leftBottomBack   = addVertex(left, bottom, back);
        GLVertex rightBottomBack  = addVertex(right, bottom, back);
       	GLVertex leftTopBack      = addVertex(left, top, back);
        GLVertex rightTopBack     = addVertex(right, top, back);
       	GLVertex leftBottomFront  = addVertex(left, bottom, front);
        GLVertex rightBottomFront = addVertex(right, bottom, front);
       	GLVertex leftTopFront     = addVertex(left, top, front);
        GLVertex rightTopFront    = addVertex(right, top, front);

        // vertices are added in a clockwise orientation (when viewed from the outside)
        // bottom
        if ((floor & cBOTTOM) > 0)
        	addFace(new GLFace(leftBottomBack, leftBottomFront, rightBottomFront, rightBottomBack));
        else
        	addFace(null);
        // front
        if ((floor & cFRONT) > 0)
        	addFace(new GLFace(leftBottomFront, leftTopFront, rightTopFront, rightBottomFront));
        else
        	addFace(null);
        // left
        if ((floor & cLEFT) > 0)
        	addFace(new GLFace(leftBottomBack, leftTopBack, leftTopFront, leftBottomFront));
        else
        	addFace(null);
        // right
        if ((floor & cRIGHT) > 0)
        	addFace(new GLFace(rightBottomBack, rightBottomFront, rightTopFront, rightTopBack));
        else
        	addFace(null);
        // back
        if ((floor & cBACK) > 0)
        	addFace(new GLFace(leftBottomBack, rightBottomBack, rightTopBack, leftTopBack));
        else
        	addFace(null);
        // top
        if ((floor & cTOP) > 0)
        	addFace(new GLFace(leftTopBack, rightTopBack, rightTopFront, leftTopFront));
        else
        	addFace(null);
	}
	
    public static final int kBottom = 0;
    public static final int kFront = 1;
    public static final int kLeft = 2;
    public static final int kRight = 3;
    public static final int kBack = 4;
    public static final int kTop = 5;

    public final static int cTOP    = 0x01;
    public final static int cBOTTOM = 0x02;
    public final static int cLEFT   = 0x04;
    public final static int cRIGHT  = 0x08;
    public final static int cFRONT  = 0x10;
    public final static int cBACK   = 0x20;
    public final static int cALL    = 0x3f;
}
