package it.nando.android.labyrinth3d;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RandomMaze extends Activity {
	Context mContext;
	
    public void onClickSave(View v) {
        Bundle stats = new Bundle();
        
        EditText edittext_x = (EditText) findViewById(R.id.x);
        EditText edittext_y = (EditText) findViewById(R.id.y);
        EditText edittext_z = (EditText) findViewById(R.id.z);

        int error = 1;
        if (edittext_x.getText().length() > 0 && edittext_y.getText().length() > 0 && edittext_z.getText().length() > 0) {
        	int x,y,z;
        	try {
            	x = new Integer(edittext_x.getText().toString());
            	y = new Integer(edittext_y.getText().toString());
            	z = new Integer(edittext_z.getText().toString());
            	if (x > 0 && y > 0 && z > 0) {
	            	if (x <= 20 && y <= 20 && z <= 20) {
	            		if ((x * y * z) < 1000) {
		            		stats.putInt("x",x); 
		            		stats.putInt("y",y); 
		            		stats.putInt("z",z); 
		            		setResult(RESULT_OK,getIntent().putExtras(stats));
		            		finish();
		            		error = 0;
	            		}
	            		else
	            			error = 2;
	            	}
            	}
        	}
        	catch (Exception e)  {
        		
        	}
        }
        if (error == 1)
        	Toast.makeText(mContext, "Enter values between 1 and 20", Toast.LENGTH_LONG).show();
        else if (error == 2)
        	Toast.makeText(mContext, "The labyrinth is too big", Toast.LENGTH_LONG).show();
    }
    public void onClickCancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bun = getIntent().getExtras();
        int x = bun.getInt("x",3);
        int y = bun.getInt("y",3);
        int z = bun.getInt("z",3);

        mContext = this;

        setContentView(R.layout.random_maze);

        EditText edittext_x = (EditText) findViewById(R.id.x);
        EditText edittext_y = (EditText) findViewById(R.id.y);
        EditText edittext_z = (EditText) findViewById(R.id.z);
        
        edittext_x.setText(""+x);
        edittext_y.setText(""+y);
        edittext_z.setText(""+z);
	}
}
