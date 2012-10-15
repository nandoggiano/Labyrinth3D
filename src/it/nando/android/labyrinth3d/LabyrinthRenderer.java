package it.nando.android.labyrinth3d;

import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class LabyrinthRenderer implements GLSurfaceView.Renderer {
	View view;
	
    public LabyrinthRenderer(SurfaceHolder surfaceHolder,View view) {
    	this.view = view;
    	mWorld = null;
        mLastTime = SystemClock.uptimeMillis();
        mSurfaceHolder = surfaceHolder;
    }

    public LabyrinthRenderer(GLWorld world,SurfaceHolder surfaceHolder) {
        mWorld = world;
        mLastTime = SystemClock.uptimeMillis();
        mSurfaceHolder = surfaceHolder;
    }

    private double G = SensorManager.GRAVITY_EARTH / 100.0;
    private static final double ATTRITO = 0.6;
    private static final long timeVibration = 20;
    
    public void setGravity(float g) {
    	G = g / 100.0;
    }
    public void onDrawFrame(GL10 gl) {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear(). However we must make sure to set the scissor
         * correctly first. The scissor is always specified in window
         * coordinates:
         */

        gl.glClearColor(0.6f,0.6f,0.6f,1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        GLU.gluLookAt(gl,
        		0, 0, -3f,
        		0f, 0f, 0f,
        		0f, 1.0f, 0.0f);

        /*
         * Now we're ready to draw some 3D object
         */

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        int ix = 0;
        int iy = 0;
        int iz = 0;
        int i;
        long now = SystemClock.uptimeMillis();
        if (now > mLastTime) {
        	double elapsed = (now - mLastTime) / 1000.0;

        	GLVertex vx = mM4.multiply(new GLVertex(1,0,0));
        	GLVertex vy = mM4.multiply(new GLVertex(0,1,0));
        	GLVertex vz = mM4.multiply(new GLVertex(0,0,1));
        	
        	double posx = mWorld.mSphere.mCenterX;
        	double posy = mWorld.mSphere.mCenterY;
        	double posz = mWorld.mSphere.mCenterZ;
        	for (i=0; i<mWorld.mLabirinto.x; i++) {
            	if (posx < mWorld.mLabirinto.margini_v[i+1]) {
            		ix = i;
            		break;
            	}
            }
        	for (i=0; i<mWorld.mLabirinto.y; i++) {
            	if (posy < mWorld.mLabirinto.margini_o[i+1]) {
            		iy = i;
            		break;
            	}
            }
        	for (i=0; i<mWorld.mLabirinto.z; i++) {
            	if (posz < mWorld.mLabirinto.margini_f[i+1]) {
            		iz = i;
            		break;
            	}
            }
        	double a; // accelerazione

        	double moduloAsseVerticale = Math.sqrt(Math.pow(mSensorX,2)+Math.pow(mSensorY,2)+Math.pow(mSensorZ,2));
        	double moltiplicazioneAsseX =  mSensorX*vx.x + mSensorY*vx.y + mSensorZ*vx.z;
        	double moltiplicazioneAsseY =  mSensorX*vy.x + mSensorY*vy.y + mSensorZ*vy.z;
        	double moltiplicazioneAsseZ =  mSensorX*vz.x + mSensorY*vz.y + mSensorZ*vz.z;
        	
        	double x = moltiplicazioneAsseX / moduloAsseVerticale;
        	double y = moltiplicazioneAsseY / moduloAsseVerticale;
        	double z = moltiplicazioneAsseZ / moduloAsseVerticale;
        	
        	if (y != 0.0 || mWorld.mSphere.mVelocitaY != 0.0) {
           		mWorld.mSphere.mVelocitaY *= Math.pow(ATTRITO,elapsed);
               	a = G * -y;
                mWorld.mSphere.mVelocitaY += (a / elapsed);
                posy += (mWorld.mSphere.mVelocitaY * elapsed);
                int iyt = iy;
                
            	while (iyt > 0) {
            		i = ix + ((iyt-1)*mWorld.mLabirinto.x) + (iz*(mWorld.mLabirinto.x*(mWorld.mLabirinto.y-1)));
                	if (mWorld.mLabirinto.top_bottom[i] == 0)
                		iyt--;
                	else
                		break;
            	}
                if ((posy - mWorld.mSphere.mRadiusY) < mWorld.mLabirinto.margini_o[iyt]) {
                	posy = mWorld.mLabirinto.margini_o[iyt] + mWorld.mSphere.mRadiusY;
                	mWorld.mSphere.mVelocitaY = 0;
                	if (posy != mWorld.mSphere.mCenterY)
                    	mWorld.v.vibrate(timeVibration);
                }
                else {
                    iyt = iy;
                	while (iyt < (mWorld.mLabirinto.y - 1)) {
                		i = ix + (iyt*mWorld.mLabirinto.x) + (iz*(mWorld.mLabirinto.x*(mWorld.mLabirinto.y-1)));
                    	if (mWorld.mLabirinto.top_bottom[i] == 0)
                    		iyt++;
                    	else
                    		break;
                	}
                	if ((posy + mWorld.mSphere.mRadiusY) > mWorld.mLabirinto.margini_o[iyt+1]) {
                    	posy = mWorld.mLabirinto.margini_o[iyt+1] - mWorld.mSphere.mRadiusY; 
                    	mWorld.mSphere.mVelocitaY = 0;
                    	if (posy != mWorld.mSphere.mCenterY)
                        	mWorld.v.vibrate(timeVibration);
                    }
                }
            	for (i=0; i<mWorld.mLabirinto.y; i++) {
                	if (posy < mWorld.mLabirinto.margini_o[i+1]) {
                		iy = i;
                		break;
                	}
                }
            }
        	if (z != 0.0 || mWorld.mSphere.mVelocitaZ != 0.0) {
           		mWorld.mSphere.mVelocitaZ *= Math.pow(ATTRITO,elapsed);
                a = G * -z;
                mWorld.mSphere.mVelocitaZ += (a / elapsed);
                posz += (mWorld.mSphere.mVelocitaZ * elapsed);
                int izt = iz;
                
            	while (izt > 0) {
            		i = ix + (iy*mWorld.mLabirinto.x) + ((izt-1)*(mWorld.mLabirinto.x*mWorld.mLabirinto.y));
                	if (mWorld.mLabirinto.front_back[i] == 0)
                		izt--;
                	else
                		break;
            	}
                if ((posz - mWorld.mSphere.mRadiusZ) < mWorld.mLabirinto.margini_f[izt]) {
                	posz = mWorld.mLabirinto.margini_f[izt] + mWorld.mSphere.mRadiusZ;
                	mWorld.mSphere.mVelocitaZ = 0;
                	if (posz != mWorld.mSphere.mCenterZ)
                    	mWorld.v.vibrate(timeVibration);
                }
                else {
                    izt = iz;
                	while (izt < (mWorld.mLabirinto.z - 1)) {
                		i = ix + (iy*mWorld.mLabirinto.x) + (izt*(mWorld.mLabirinto.x*mWorld.mLabirinto.y));
                    	if (mWorld.mLabirinto.front_back[i] == 0)
                    		izt++;
                    	else
                    		break;
                	}
                    if ((posz + mWorld.mSphere.mRadiusZ) > mWorld.mLabirinto.margini_f[izt+1]) {
                    	posz = mWorld.mLabirinto.margini_f[izt+1] - mWorld.mSphere.mRadiusZ; 
                    	mWorld.mSphere.mVelocitaZ = 0;
                    	if (posz != mWorld.mSphere.mCenterZ)
                        	mWorld.v.vibrate(timeVibration);
                    }
                }
            	for (i=0; i<mWorld.mLabirinto.z; i++) {
                	if (posz < mWorld.mLabirinto.margini_f[i+1]) {
                		iz = i;
                		break;
                	}
                }
            }
            if (x != 0.0 || mWorld.mSphere.mVelocitaX != 0.0) {
           		mWorld.mSphere.mVelocitaX *= Math.pow(ATTRITO,elapsed);
                a = G * -x;
                mWorld.mSphere.mVelocitaX += (a / elapsed);
                posx += (mWorld.mSphere.mVelocitaX * elapsed);
                int ixt = ix;
                
            	while (ixt > 0) {
            		i = ixt - 1 + (iy*(mWorld.mLabirinto.x-1)) + (iz*((mWorld.mLabirinto.x-1)*mWorld.mLabirinto.y));
                	if (mWorld.mLabirinto.left_right[i] == 0)
                		ixt--;
                	else
                		break;
            	}
            	
                if ((posx - mWorld.mSphere.mRadiusX) < mWorld.mLabirinto.margini_v[ixt]) {
                	posx = mWorld.mLabirinto.margini_v[ixt] + mWorld.mSphere.mRadiusX;
                	mWorld.mSphere.mVelocitaX = 0;
                	if (posx != mWorld.mSphere.mCenterX)
                    	mWorld.v.vibrate(timeVibration);
                }
                else {
                    ixt = ix;
                	while (ixt < (mWorld.mLabirinto.x - 1)) {
                		i = ixt + (iy*(mWorld.mLabirinto.x-1)) + (iz*((mWorld.mLabirinto.x-1)*mWorld.mLabirinto.y));
                    	if (mWorld.mLabirinto.left_right[i] == 0)
                    		ixt++;
                    	else
                    		break;
                	}
                    if ((posx + mWorld.mSphere.mRadiusX) > mWorld.mLabirinto.margini_v[ixt+1]) {
                    	posx = mWorld.mLabirinto.margini_v[ixt+1] - mWorld.mSphere.mRadiusX; 
                    	mWorld.mSphere.mVelocitaX = 0;
                    	if (posx != mWorld.mSphere.mCenterX)
                        	mWorld.v.vibrate(timeVibration);
                    }
                }
            	for (i=0; i<mWorld.mLabirinto.x; i++) {
                	if (posx < mWorld.mLabirinto.margini_v[i+1]) {
                		ix = i;
                		break;
                	}
                }
            }
            mLastTime = now;
            
            boolean moved = false;
            if (mWorld.mSphere.mCenterY != posy) {
            	//mWorld.mSphere.setRotation(5, 1, 0, 1);
            	//Log.e("tmp","centerY="+mWorld.mSphere.mCenterY+" posy="+posy);
                mWorld.mSphere.mCenterY = posy;
                moved  = true;
            }
            if (mWorld.mSphere.mCenterZ != posz) {
            	//mWorld.mSphere.setRotation(5, 1, 1, 0);
            	//Log.e("tmp","centerZ="+mWorld.mSphere.mCenterZ+" posz="+posz);
                mWorld.mSphere.mCenterZ = posz;
                moved  = true;
            }
            if (mWorld.mSphere.mCenterX != posx) {
            	//mWorld.mSphere.setRotation(5, 0, 1, 1);
            	//Log.e("tmp","centerX="+mWorld.mSphere.mCenterX+" posx="+posx);
            	mWorld.mSphere.mCenterX = posx;
                moved  = true;
            }
            if (moved)
            	mWorld.mSphere.setCenter(posx,posy,posz);
        }
        if (mWorld.risolto == false) {
            if (ix == mWorld.mLabirinto.x - 1 && iy == mWorld.mLabirinto.y - 1 && iz == mWorld.mLabirinto.z - 1) {
            	mWorld.risolto = true;
            	
            	long pattern[] = new long[] { 0, 250, 100, 500};

            	mWorld.v.vibrate(pattern, -1);
            	
            	MediaPlayer mMediaPlayer = MediaPlayer.create(this.view.getContext(), R.raw.tada);
                mMediaPlayer.start();
            }
        }
        // gl.glScalef(0.6f, 0.6f, 0.6f);

        gl.glMultMatrixf(mM4.toArray(),0); // rotazioni

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mWorld.draw(gl);
        
        int width = view.getWidth(); 
        int height = view.getHeight();
        setFrustum(gl,width,height);
    }
    float zoomFactor = 0.5f;
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	setFrustum(gl,width,height);
    }
    private void setFrustum(GL10 gl,int width, int height) {
       	gl.glViewport(0, 0, width, height);
       	gl.glMatrixMode(GL10.GL_PROJECTION);
       	gl.glLoadIdentity();
       	if (width <= height ) {
       		float ratio = ((float) height) / ((float)width);
       		gl.glFrustumf(-1.0f * zoomFactor, 1.0f * zoomFactor, -ratio * zoomFactor,ratio * zoomFactor, 1.0f, 5.0f);
       	}
       	else {
       		float ratio  = ((float) width) / ((float)height);
       		gl.glFrustumf(-ratio * zoomFactor,ratio  * zoomFactor,-1.0f * zoomFactor, 1.0f * zoomFactor, 1.0f, 5.0f);
       	}
    }

    public void reset() {
        synchronized (mSurfaceHolder) {
	    	mWorld.mSphere.setCenter(
	    		mWorld.mLabirinto.margini_v[0] + mWorld.mLabirinto.largh/2f,
	    		mWorld.mLabirinto.margini_o[0] + mWorld.mLabirinto.largh/2f,
	    		mWorld.mLabirinto.margini_f[0] + mWorld.mLabirinto.largh/2f);
	    	mWorld.mSphere.mVelocitaX = 0;
	    	mWorld.mSphere.mVelocitaY = 0;
	    	mWorld.mSphere.mVelocitaZ = 0;
	    	
	    	mWorld.risolto = false;

	    	setIdentity();
        }
    }
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
       	if (mM4 == null) {
        	mM4 = new M4();
           	mM4.setIdentity();
       	}
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
    public void setRotation(int angle,int x,int y,int z) {
       	this.setRotation((float)angle,x,y,z);
    }
    public void setIdentity() {
    	if (mM4 != null)
    		mM4.setIdentity();
    }
    public void setWorld(GLWorld world) {
    	mWorld = world;
    }
    
    long mLastTime;
    private GLWorld mWorld;
    public float mSensorX = 0;
    public float mSensorY = 1;
    public float mSensorZ = 0;
    
    private M4 mM4 = null;

    SurfaceHolder mSurfaceHolder;

	public void restoreState(Bundle savedState) {
	}
	public void saveState(Bundle map) {
	}
	public void onResume() {
	}
	public void onPause() {
	}
}
