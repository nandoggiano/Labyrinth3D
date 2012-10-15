package it.nando.android.labyrinth3d;

import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

public class Options extends Activity {
    public int OPACITY_OUTER_WALLS;
    public int OPACITY_INNER_WALLS;
    float G;

    public void onClickSave(View v) {
        Bundle stats = new Bundle();
        stats.putInt("opacity_outer_walls",OPACITY_OUTER_WALLS); 
        stats.putInt("opacity_inner_walls",OPACITY_INNER_WALLS); 
        stats.putFloat("gravity",G); 
        setResult(RESULT_OK,getIntent().putExtras(stats));
        finish();
    }
    public void onClickCancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle bun = getIntent().getExtras();
        OPACITY_OUTER_WALLS = bun.getInt("opacity_outer_walls");
        OPACITY_INNER_WALLS = bun.getInt("opacity_inner_walls");
        G = bun.getFloat("gravity");

        setContentView(R.layout.options);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        for (int i=0; i<mFloats.length; i++) {
        	if (mFloats[i] == G) {
        		mSpinner.setSelection(i);
        		break;
        	}
        }
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				G = mFloats[arg2]; 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        SeekBar mSeekBar = (SeekBar) findViewById(R.id.opacity_outer_walls);
        mSeekBar.setProgress(OPACITY_OUTER_WALLS);
        mSeekBar.setOnSeekBarChangeListener(
    		new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar,
						int progress, boolean fromUser) {
					OPACITY_OUTER_WALLS = progress;
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
    		}
        );

        mSeekBar = (SeekBar) findViewById(R.id.opacity_inner_walls);
        mSeekBar.setProgress(OPACITY_INNER_WALLS);
        mSeekBar.setOnSeekBarChangeListener(
    		new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar,
						int progress, boolean fromUser) {
					OPACITY_INNER_WALLS = progress;
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
    		}
        );
        /*
        Button button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(
        	new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
		            Bundle stats = new Bundle();
		            stats.putInt("opacity_outer_walls",OPACITY_OUTER_WALLS); 
		            stats.putInt("opacity_inner_walls",OPACITY_INNER_WALLS); 
		            stats.putFloat("gravity",G); 
		            setResult(RESULT_OK,getIntent().putExtras(stats));
		            finish();
				}
        	}
     	);
        button = (Button) findViewById(R.id.button_quit);
        button.setOnClickListener(
        	new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
		            setResult(RESULT_CANCELED);
		            finish();
				}
        	}
     	);
     	*/
    }

    private static final Float[] mFloats = {
	    SensorManager.GRAVITY_MOON,
	    SensorManager.GRAVITY_MERCURY,
	    SensorManager.GRAVITY_VENUS,
	    SensorManager.GRAVITY_EARTH,
	    SensorManager.GRAVITY_MARS,
	    SensorManager.GRAVITY_JUPITER,
	    SensorManager.GRAVITY_SATURN,
	    SensorManager.GRAVITY_URANUS,
	    SensorManager.GRAVITY_NEPTUNE,
	    SensorManager.GRAVITY_PLUTO
    };
    private static final String[] mStrings = {
    	"Moon",	  "Mercury", "Venus",
    	"Earth",  "Mars",    "Jupiter", 
	    "Saturn", "Uranus",  "Neptune",
	    "Pluto"
    };
}
