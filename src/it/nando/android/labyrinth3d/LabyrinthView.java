package it.nando.android.labyrinth3d;

import java.util.List;

import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

//class SurfaceView extends GLSurfaceView  implements AccelerometerListener {
public class LabyrinthView extends GLSurfaceView  implements SurfaceHolder.Callback {
    List<Sensor> list;
    SensorManager sm;

    public LabyrinthView(Context context, AttributeSet attrs) {
        super(context,attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mRenderer = new LabyrinthRenderer(holder,this);
        setRenderer(mRenderer);

        init();
    }
    public LabyrinthView(Context context,GLWorld world) {
        super(context);
        //setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mRenderer = new LabyrinthRenderer(world,holder);
        setRenderer(mRenderer);

        init();
	}
    private void init() {
        sm = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        // Get list of accelerometers 
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if (list.size()>0)
        	sm.registerListener(sel,(Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    	
    }

	public SensorEventListener sel = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			/* Isn't required for this example */

		}
		public void onSensorChanged(SensorEvent event) {
        	mRenderer.mSensorX = -event.values[0];
        	mRenderer.mSensorY =  event.values[1];
        	mRenderer.mSensorZ = -event.values[2];
		}
	};
    @Override public boolean  onKeyUp  (int keyCode, KeyEvent event) {
    	boolean flag = false;
    	switch (keyCode) {
    		case KeyEvent.KEYCODE_VOLUME_DOWN:
    		case KeyEvent.KEYCODE_VOLUME_UP:
    			flag = true;
    			break;
    	}
    	return flag;
    }
    @Override public boolean  onKeyDown  (int keyCode, KeyEvent event) {
    	int i = event.isShiftPressed() ? -1 : 1;
    	switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (mRenderer.zoomFactor < 1.5f)
					mRenderer.zoomFactor += 0.1f;
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (mRenderer.zoomFactor > 0.1)
					mRenderer.zoomFactor -= 0.1f;
				break;
    		case KeyEvent.KEYCODE_X:
   				mRenderer.setRotation(1 * i,1,0,0);
    			break;
    		case KeyEvent.KEYCODE_Y:
   				mRenderer.setRotation(1 * i,0,1,0);
    			break;
    		case KeyEvent.KEYCODE_Z:
   				mRenderer.setRotation(1 * i,0,0,1);
    			break;
    		case KeyEvent.KEYCODE_DPAD_UP:
            	mRenderer.setRotation(1 * i,1,0,0);
    			break;
    		case KeyEvent.KEYCODE_DPAD_DOWN:
            	mRenderer.setRotation(-1 * i,1,0,0);
    			break;
    		case KeyEvent.KEYCODE_DPAD_LEFT:
            	mRenderer.setRotation(-1 * i,0,1,0);
    			break;
    		case KeyEvent.KEYCODE_DPAD_RIGHT:
            	mRenderer.setRotation(1 * i,0,1,0);
    			break;
    		default:
    	    	return false;
    	}
		//requestRender();
    	return true;
    }

    void restoreState(Bundle savedState) {
    	mRenderer.restoreState(savedState);
    }
    void saveState(Bundle map) {
    	mRenderer.saveState(map);
    }

    float mPreviousX;
    float mPreviousY;
    @Override public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	mPreviousX = x;
        	mPreviousY = y;
        	break;
        case MotionEvent.ACTION_MOVE:
        	int d = (int) (x - mPreviousX); // rotazione asse Y
        	if (d != 0)
        		mRenderer.setRotation(d,0,1,0);
            d = (int) (mPreviousY - y); // rotazione asse X
        	if (d != 0)
        		mRenderer.setRotation(d,1,0,0);
            //requestRender();
            
            mPreviousX = x;
            mPreviousY = y;
        }
        return true;
    }
    
    public void reset() {
    	mRenderer.reset();
    }
    
    public void setWorld(GLWorld world) {
    	mRenderer.setWorld(world);
    }
    public void setGravity(float g) {
    	mRenderer.setGravity(g);
    }
    
    
    private LabyrinthRenderer mRenderer;
	
    /*
    @Override
    public void onAccelerationChanged(float x, float y, float z) {
    	mRenderer.mSensorX = Math.max(Math.min(x,10f),-10f) / 10f;
    	mRenderer.mSensorY = Math.max(Math.min(y,10f),-10f) / 10f;
    	mRenderer.mSensorZ = Math.max(Math.min(z,10f),-10f) / 10f;
	}


	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onStop() {
		// Always a good idea to unregister, disconnect, close, etc things 
		if(list.size()>0){
			sm.unregisterListener(sel);
		}
		super.onStop();
	}
	*/
	@Override public void onResume() {
		super.onResume();
		mRenderer.onResume();
	}
    @Override
    public void onPause() {
        super.onPause();
		mRenderer.onPause();
    }
}
