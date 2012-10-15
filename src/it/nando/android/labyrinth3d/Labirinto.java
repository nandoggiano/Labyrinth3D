package it.nando.android.labyrinth3d;

import java.util.Random;

class Labirinto {
    public int x;
    public int y;
    public int z;
    public int top_bottom[];
    public int left_right[];
    public int front_back[];
    float largh; 
    public float margini_o[]; // margini top - bottom
    public float margini_v[]; // margini left - right
    public float margini_f[]; // margini front - back
    
    /*
    private void LabirintoStatico2() {
        x = 4;
        y = 4;
        z = 4;
        top_bottom = new int[] { // 4*4*3
        	// 1p       2p      3p
            1,0,0,0, 0,0,0,0, 0,1,0,0, // z = 0
            0,0,0,0, 1,1,1,0, 1,0,1,1, // z = 1
            0,0,1,0, 0,0,0,1, 0,1,0,1, // z = 2
            0,0,0,1, 0,0,1,0, 0,1,0,1, // z = 3
        };
        left_right = new int[] { // 3*4*4
        	// 0p    1p     2p     3p
            0,1,1, 1,1,1, 0,1,1, 1,0,1,
            0,1,1, 0,0,0, 1,0,1, 1,0,0,
            1,0,1, 1,1,0, 1,1,1, 0,0,1,
            0,1,0, 1,1,0, 1,0,1, 0,0,0,
        };
        front_back = new int[] { // 4*3*4
        	// 0p      1p       2p       3p
            1,1,0,0, 0,1,1,1, 0,1,0,0, 0,0,1,1,
            0,1,1,0, 1,1,1,1, 0,0,1,0, 0,1,1,0,
            1,0,1,0, 1,1,1,1, 1,1,1,0, 1,1,1,0,
            };
    }
    private void LabirintoStatico3() {
        x = 3;
        y = 3;
        z = 3;
        top_bottom = new int[] { // 3*3*2
        	0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0
        };
        left_right = new int[] { // 2*3*3
       		1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1
        };
        front_back = new int[] { // 3*2*3
        	0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1
        };
    }
    */
    private void LabirintoStatico() {
        x = 3;
        y = 2;
        z = 2;
        top_bottom = new int[] { // 3*2*1
        	0,0,0,0,1,0
        };
        left_right = new int[] { // 2*2*2
       		1,1,1,0,1,0,0,1
        };
        front_back = new int[] { // 3*1*2
        	0,0,1,1,0,1
        };
    }
    private void LabirintoRandom(int x,int y,int z) {
		class Cella {
			public int muri;
			public boolean visitata;
			
			public Cella(int muro,boolean visitata) {
				this.muri = muro;
				this.visitata = visitata;
			}
		}
		class poscorr {
			int x;
			int y;
			int z;
		}

		this.x = x;
		this.y = y;
		this.z = z;
		int posx,posy,posz;
		int i,j,k;

		Cella celle[][][] = new Cella[x][y][z];
		
		Random mRandom = new Random(System.currentTimeMillis());
		
		for (i=0; i<x; i++) {
			for (j=0; j<y; j++) {
				for (k=0; k<z; k++) {
					celle[i][j][k] = new Cella(Cube.cALL,false);
					/*
					celle[i][j][k].muri = new Integer(Cube.cALL);
					celle[i][j][k].visitata = new Boolean(false);
					*/
				}
			}
		}
		poscorr path[] = new poscorr[x * y * z];
		int pos = 0;
		path[pos] = new poscorr();
		path[pos].x = x - 1;
		path[pos].y = y - 1;
		path[pos].z = z - 1;
		while (true) {
			posx = path[pos].x;
			posy = path[pos].y;
			posz = path[pos].z;
			celle[posx][posy][posz].visitata = true;

			i = 0;
			int muri[][] = new int[6][5];
			if (posx > 0 && celle[posx-1][posy][posz].visitata == false) {
				muri[i][0] = posx - 1;
				muri[i][1] = posy;
				muri[i][2] = posz;
				muri[i][3] = Cube.cRIGHT;
				muri[i++][4] = Cube.cLEFT;
			}
			if (posx < x-1 && celle[posx+1][posy][posz].visitata == false) {
				muri[i][0] = posx + 1;
				muri[i][1] = posy;
				muri[i][2] = posz;
				muri[i][3] = Cube.cLEFT;
				muri[i++][4] = Cube.cRIGHT;
			}
			if (posy > 0 && celle[posx][posy-1][posz].visitata == false) {
				muri[i][0] = posx;
				muri[i][1] = posy-1;
				muri[i][2] = posz;
				muri[i][3] = Cube.cBOTTOM;
				muri[i++][4] = Cube.cTOP;
			}
			if (posy < y-1 && celle[posx][posy+1][posz].visitata == false) {
				muri[i][0] = posx;
				muri[i][1] = posy+1;
				muri[i][2] = posz;
				muri[i][3] = Cube.cTOP;
				muri[i++][4] = Cube.cBOTTOM;
			}
			if (posz > 0 && celle[posx][posy][posz-1].visitata == false) {
				muri[i][0] = posx;
				muri[i][1] = posy;
				muri[i][2] = posz-1;
				muri[i][3] = Cube.cBACK;
				muri[i++][4] = Cube.cFRONT;
			}
			if (posz < z-1 && celle[posx][posy][posz+1].visitata == false) {
				muri[i][0] = posx;
				muri[i][1] = posy;
				muri[i][2] = posz+1;
				muri[i][3] = Cube.cFRONT;
				muri[i++][4] = Cube.cBACK;
			}
			
			if (i > 0) {
				int r = mRandom.nextInt(i);
				
				celle[posx][posy][posz].muri -= muri[r][3];
				posx = muri[r][0];
				posy = muri[r][1];
				posz = muri[r][2];
				celle[posx][posy][posz].muri -= muri[r][4];  
				
				pos++;
				path[pos] = new poscorr();
				path[pos].x = posx;
				path[pos].y = posy;
				path[pos].z = posz;
				
				//Log.w(this.getClass().getName(),"pos="+pos);
				//Log.w(this.getClass().getName(),"posx="+posx+" posy="+posy+" posz="+posz);
			}
			else {
				if (pos > 0)
					pos--;
				else
					break;
			}
		}
		
		top_bottom = new int[x * (y - 1) * z];
		left_right = new int[(x - 1) * y  * z];
		front_back = new int[x  * y  * (z - 1)];
		pos = 0;
		for (k=0; k<z; k++) {
			for (j=1; j<y; j++) {
				for (i=0; i<x; i++) {
					if ((celle[i][j][k].muri & Cube.cBOTTOM) == Cube.cBOTTOM)
						top_bottom[pos] = 1;
					else
						top_bottom[pos] = 0;
					pos++;
				}
			}
		}
		pos = 0;
		for (k=0; k<z; k++) {
			for (j=0; j<y; j++) {
				for (i=1; i<x; i++) {
					if ((celle[i][j][k].muri & Cube.cRIGHT) == Cube.cRIGHT)
						left_right[pos] = 1;
					else
						left_right[pos] = 0;
					pos++;
				}
			}
		}
		pos = 0;
		for (k=1; k<z; k++) {
			for (j=0; j<y; j++) {
				for (i=0; i<x; i++) {
					if ((celle[i][j][k].muri & Cube.cBACK) == Cube.cBACK)
						front_back[pos] = 1;
					else
						front_back[pos] = 0;
					pos++;
				}
			}
		}
		
		/*
		String tp = new String();
		pos = 0;
		tp = "top_bottom = (";
		for (k=0; k<z; k++) {
			for (j=1; j<y; j++) {
				for (i=0; i<x; i++) {
					if (pos > 0) tp+=",";
					tp += top_bottom[pos];
					pos++;
				}
			}
		}
		tp+=")\n";
		pos = 0;
		tp += "left_right = (";
		for (k=0; k<z; k++) {
			for (j=0; j<y; j++) {
				for (i=1; i<x; i++) {
					if (pos > 0) tp+=",";
					tp += left_right[pos];
					pos++;
				}
			}
		}
		tp+=")\n";
		pos = 0;
		tp += "front_back = (";
		for (k=1; k<z; k++) {
			for (j=0; j<y; j++) {
				for (i=0; i<x; i++) {
					if (pos > 0) tp+=",";
					tp += front_back[pos];
					pos++;
				}
			}
		}
		tp+=")\n";
		Log.w(this.getClass().getName(),tp);
		*/
		/*
		for (k=0; k<z; k++) {
			for (j=0; j<y; j++) {
				for (i=0; i<x; i++) {
					Log.w(this.getClass().getName(),"i="+i+" j="+j+" k="+k);
					Log.w(this.getClass().getName(),""+(celle[i][j][k].muri & Cube.cBACK));
				}
			}
		}
		for (i=0; i<(x*(y-1)*z); i++) {
			Log.w(this.getClass().getName(),"front_back="+front_back[i]);
		}
		*/
	}
    public String toString() {
    	int i;
    	String str = ""+x+","+y+","+z+",";
    	if (top_bottom.length == 0)
			str += "-";
    	else {
    		for (i=0; i<top_bottom.length;i++)
    			str += top_bottom[i];
    	}
		str += ",";
    	if (left_right.length == 0)
			str += "-";
    	else {
    		for (i=0; i<left_right.length;i++)
    			str += left_right[i];
    	}
		str += ",";
    	if (front_back.length == 0)
			str += "-";
    	else {
    		for (i=0; i<front_back.length;i++)
    			str += front_back[i];
    	}
    	//Log.w("lab",str);
		return str;
    }
    public Labirinto(int x,int y,int z) {
    	LabirintoRandom(x,y,z);
    	init();
    }
    public Labirinto(int x,int y,int z,String tb,String lr,String fb) {
    	char c;
    	
        this.x = x;
        this.y = y;
        this.z = z;
        
        top_bottom = new int[x*(y-1)*z];
        for (int i=0; i<tb.length(); i++) {
        	c = tb.charAt(i);
        	if (c != '-')
        		top_bottom[i] = c - '0';
        }
        
        left_right = new int[(x-1)*y*z];
        for (int i=0; i<lr.length(); i++) {
        	c = lr.charAt(i);
        	if (c != '-')
        		left_right[i] = c - '0';
        }
        
        front_back = new int[x*y*(z-1)];
        for (int i=0; i<fb.length(); i++) {
        	c = fb.charAt(i);
        	if (c != '-')
        		front_back[i] = c - '0';
        }
        
    	init();
    }
    public Labirinto() {
   		LabirintoStatico();
    	init();
    }
    private void init() {
        int max = Math.max(x,y);
        max = Math.max(max,z);
        largh = 2.0f / max;
        margini_v = new float[x + 1];
        margini_o = new float[y + 1];
        margini_f = new float[z + 1];

        int i;
        for (i=0; i<=x; i++)
        	margini_v[i] = largh*(i-(x/2.0f));
        for (i=0; i<=y; i++)
        	margini_o[i] = largh*(i-(y/2.0f));
        for (i=0; i<=z; i++)
        	margini_f[i] = largh*(i-(z/2.0f));
    }
    public int getPareti(int ix,int iy,int iz) {
    	int pareti = 0;
    	if (iy > 0) {
        	if (top_bottom[ix + ((iy-1)*x) + (iz*(x*(y-1)))] == 1)
    			pareti += Cube.cBOTTOM;
    	}
    	else
    		pareti += Cube.cBOTTOM;
    	if (iy < y-1) {
    		// if (top_bottom[ix + (iy*x) + (iz*(x*(y-1)))] == 1)
    		// pareti += Cube.cTOP;
    	}
    	else
    		pareti += Cube.cTOP;
    	
    	if (ix > 0) {
    		 if (left_right[ix - 1 + (iy*(x-1)) + (iz*((x-1)*y))] == 1)
    			 pareti += Cube.cLEFT;
    	}
    	else
			pareti += Cube.cLEFT;
    	if (ix < x-1) {
    		// if (left_right[ix + (iy*(x-1)) + (iz*((x-1)*y))] == 1)
    			// pareti += Cube.cRIGHT;
    	}
    	else
			pareti += Cube.cRIGHT;
    	
    	if (iz > 0) {
    		if (front_back[ix + (iy*x) + ((iz-1)*(x*y))] == 1)
    			pareti += Cube.cBACK;
    	}
    	else
			pareti += Cube.cBACK;
    	if (iz < z-1) {
    		// if (front_back[ix + (iy*x) + (iz*(x*y))] == 1)
    			// pareti += Cube.cFRONT;
    	}
    	else
			pareti += Cube.cFRONT;
    	
    	return pareti;
    }
}
