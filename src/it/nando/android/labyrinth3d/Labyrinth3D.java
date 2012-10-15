package it.nando.android.labyrinth3d;

import com.google.ads.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Labyrinth3D extends Activity{
	LabyrinthView mView;
    private static Context mContext;
    GLWorld world;
    
    public static final int MENU_RESET = 0;
    public static final int MENU_QUIT = 1;
    public static final int MENU_RANDOM = 2;
    public static final int MENU_OPTIONS = 3;
    
    public int OPACITY_OUTER_WALLS = 0x2000;
    public int OPACITY_INNER_WALLS = 0x4000;
    
    private float G = SensorManager.GRAVITY_MOON;
    
    private void setColorCube(Cube[] mCubes,Labirinto lab) {
        for (int iz=0; iz<lab.z; iz++) {
            for (int iy=0; iy<lab.y; iy++) {
                for (int ix=0; ix<lab.x; ix++) {
                	int i = ix + (iy*lab.x) + (iz*(lab.x*lab.y));
					setColorCube(mCubes[i],ix,iy,iz,lab);
                }
            }
        }
    }
    private void setColorCube(Cube mCubes,int ix,int iy,int iz,Labirinto lab) {
        int alpha;
    	for (int j=0; j<6; j++) {
			int r = (int) (0x10000 * 0.1f);
			int g = (int) (0x10000 * 0.2f);
			int b = (int) (0x10000 * 0.3f);
			alpha = OPACITY_INNER_WALLS;
			switch (j) {
				case Cube.kFront:
					if (iz == lab.z - 1)
						alpha = OPACITY_OUTER_WALLS;
					break;
				case Cube.kBack:
					if (iz == 0)
						alpha = OPACITY_OUTER_WALLS;
					break;
				case Cube.kBottom:
					if (iy == 0)
						alpha = OPACITY_OUTER_WALLS;
					break;
				case Cube.kTop:
					if (iy == lab.y - 1)
						alpha = OPACITY_OUTER_WALLS;
					break;
				case Cube.kLeft:
					if (ix == 0)
						alpha = OPACITY_OUTER_WALLS;
					break;
				case Cube.kRight:
					if (ix == lab.x - 1)
						alpha = OPACITY_OUTER_WALLS;
					break;
			}
    		mCubes.setFaceColor(j,new GLColor(r,g,b,alpha));
		}
    }
	private GLWorld makeGLWorld(int x, int y, int z,
			String tb, String lr, String fb) {
    	Labirinto lab = new Labirinto(x,y,z,tb,lr,fb);
    	return makeGLWorld(lab);
	}
    private GLWorld makeGLWorld(int nLab) {
    	Labirinto lab;
    	if (nLab == 0)
    		lab = new Labirinto();
    	else
    		lab = new Labirinto(4,4,4);
    	return makeGLWorld(lab);
    }
    private GLWorld makeGLWorld(int x,int y,int z) {
    	return makeGLWorld(new Labirinto(x,y,z));
    }
    private GLWorld makeGLWorld(Labirinto lab) {
        GLWorld world = new GLWorld();
        Cube mCubes[];

        mCubes = new Cube[lab.x * lab.y * lab.z];
        for (int iz=0; iz<lab.z; iz++) {
            for (int iy=0; iy<lab.y; iy++) {
                for (int ix=0; ix<lab.x; ix++) {
                	int pareti = lab.getPareti(ix,iy,iz);
                	int i = ix + (iy*lab.x) + (iz*(lab.x*lab.y));
                	mCubes[i] = new Cube(world,
                			lab.margini_v[ix],
                			lab.margini_o[iy],
                			lab.margini_f[iz],
                			lab.margini_v[ix+1],
                			lab.margini_o[iy+1],
                			lab.margini_f[iz+1],
                			pareti);
					setColorCube(mCubes[i],ix,iy,iz,lab);
	                world.addShape(mCubes[i]);
                }
            }
        }
        world.mCubes = mCubes;
        
        world.mSphere = new Sphere(10); 
        world.mSphere.setCenter(
        		lab.margini_v[0] + lab.largh/2f,
        		lab.margini_o[0] + lab.largh/2f,
        		lab.margini_f[0] + lab.largh/2f);
        world.mSphere.setRadius(lab.largh/4f);
        
        world.mLabirinto = lab;
        
        float[] coords = {
        	world.mLabirinto.margini_v[0],world.mLabirinto.margini_o[0],world.mLabirinto.margini_f[0],
        	world.mLabirinto.margini_v[0],world.mLabirinto.margini_o[0],world.mLabirinto.margini_f[1],
        	world.mLabirinto.margini_v[1],world.mLabirinto.margini_o[0],world.mLabirinto.margini_f[0]
        };
         
        world.mStart = new Triangle(world,new GLColor(0x10000,0x10000,0x10000,0x10),coords);
        
        float[] coords2 = {
           	world.mLabirinto.margini_v[world.mLabirinto.x],world.mLabirinto.margini_o[world.mLabirinto.y],world.mLabirinto.margini_f[world.mLabirinto.z],
           	world.mLabirinto.margini_v[world.mLabirinto.x],world.mLabirinto.margini_o[world.mLabirinto.y],world.mLabirinto.margini_f[world.mLabirinto.z-1],
           	world.mLabirinto.margini_v[world.mLabirinto.x-1],world.mLabirinto.margini_o[world.mLabirinto.y],world.mLabirinto.margini_f[world.mLabirinto.z]
        };

        world.mEnd   = new Triangle(world,new GLColor(0x10000,0,0,0x10),coords2);

        world.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        world.generate();

        return world;
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		mContext = this;

		SharedPreferences pref = getPreferences(0);
		OPACITY_OUTER_WALLS = pref.getInt("opacity_outer_walls", OPACITY_OUTER_WALLS);
		OPACITY_INNER_WALLS = pref.getInt("opacity_inner_walls", OPACITY_INNER_WALLS);
		G = pref.getFloat("gravity", G);
		String lab = pref.getString("lab", null);
        
		if (lab != null) {
        	//Log.w("lab",lab);
        	String cmp[] = lab.split(",");
        	if (cmp.length == 6) {
	           	world = makeGLWorld(
	           			Integer.parseInt(cmp[0]),
	           			Integer.parseInt(cmp[1]),
	           			Integer.parseInt(cmp[2]),
	           			cmp[3],cmp[4],cmp[5]);
        	}
        	else
            	world = makeGLWorld(0);
        }
        else
        	world = makeGLWorld(0);
        
        // We don't need a title either.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN );
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.main);

        // Create the adView
        AdView adView = new AdView(this, AdSize.BANNER, "a14cbd94013c33e");
        
        // Lookup your LinearLayout assuming it’s been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.adsense);
        // Add the adView to it
        layout.addView(adView);
        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
        request.setTesting(true);
        adView.loadAd(request);
                
        mView = (LabyrinthView) findViewById(R.id.master);
        mView.setWorld(world);

        mView.setGravity(G);
        mView.requestFocus();
        mView.setFocusableInTouchMode(true);
        
        if (savedInstanceState != null)
            mView.restoreState(savedInstanceState);
    }

	@Override public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_OPTIONS, 0, "Options");
        menu.add(0, MENU_RANDOM, 0, "New maze");
        menu.add(0, MENU_RESET, 0, "Reset");
        menu.add(0, MENU_QUIT, 0, "Quit");
        return super.onCreateOptionsMenu(menu);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case MENU_OPTIONS:
				if (resultCode == RESULT_OK) {
					Bundle res = data.getExtras();
					OPACITY_OUTER_WALLS = 0x10000 * res.getInt("opacity_outer_walls") / 100;
					OPACITY_INNER_WALLS = 0x10000 * res.getInt("opacity_inner_walls") / 100;
					G = res.getFloat("gravity");
					mView.setGravity(G);
					setColorCube(world.mCubes,world.mLabirinto);
					world.generate();
				}
				break;
			case MENU_RANDOM:
				if (resultCode == RESULT_OK) {
					Bundle res = data.getExtras();
					int x = res.getInt("x");
					int y = res.getInt("y");
					int z = res.getInt("z");

					if (x > 0 && y > 0 && z > 0) {
						world = makeGLWorld(x,y,z);
						mView.setWorld(world);
						mView.reset();
					}
				}
				break;
		} // end switch
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Bundle bundle;
		Intent i;
		switch (item.getItemId()) {
			case MENU_OPTIONS:
				i = new Intent(Labyrinth3D.this, Options.class);
				bundle = new Bundle();
				bundle.putInt("opacity_outer_walls", (int) (100L * OPACITY_OUTER_WALLS / 0x10000));
				bundle.putInt("opacity_inner_walls", (int) (100L * OPACITY_INNER_WALLS / 0x10000));
				bundle.putFloat("gravity", G);
				i.putExtras(bundle);
				startActivityForResult(i,MENU_OPTIONS);
				break;
			case MENU_RANDOM:
				i = new Intent(Labyrinth3D.this, RandomMaze.class);
				bundle = new Bundle();
				bundle.putInt("x", world.mLabirinto.x);
				bundle.putInt("y", world.mLabirinto.y);
				bundle.putInt("z", world.mLabirinto.z);
				i.putExtras(bundle);
				startActivityForResult(i,MENU_RANDOM);
				break;
			case MENU_RESET:
				mView.reset();
				break;
			case MENU_QUIT:
				finish();
				break;
		}
		return true;
	}

	@Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle map) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(map);
        
        mView.saveState(map);
    }

	public static Context getContext() {
        return mContext;
    }
	
    @Override
	protected void onStop() {
    	super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("opacity_outer_walls", OPACITY_OUTER_WALLS);
		editor.putInt("opacity_inner_walls", OPACITY_INNER_WALLS);
		editor.putFloat("gravity", G);
		
		// 3,3,3,0110101111,01001110101,01011101011
		editor.putString("lab", world.mLabirinto.toString());

		// Commit the edits!
		editor.commit();
	}
}
